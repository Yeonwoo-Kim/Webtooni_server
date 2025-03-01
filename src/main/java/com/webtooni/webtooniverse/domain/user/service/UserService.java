package com.webtooni.webtooniverse.domain.user.service;

import com.webtooni.webtooniverse.domain.genre.domain.Genre;
import com.webtooni.webtooniverse.domain.genre.domain.GenreRepository;
import com.webtooni.webtooniverse.domain.user.domain.User;
import com.webtooni.webtooniverse.domain.user.domain.UserGenre;
import com.webtooni.webtooniverse.domain.user.domain.UserGenreRepository;
import com.webtooni.webtooniverse.domain.user.domain.UserRepository;
import com.webtooni.webtooniverse.domain.user.dto.request.UserInfoRequestDto;
import com.webtooni.webtooniverse.domain.user.dto.request.UserOnBoardingRequestDto;
import com.webtooni.webtooniverse.domain.user.dto.response.BestReviewerResponseDto;
import com.webtooni.webtooniverse.domain.user.dto.response.UserInfoResponseDto;
import com.webtooni.webtooniverse.domain.user.security.JwtTokenProvider;
import com.webtooni.webtooniverse.domain.user.security.sociallogin.KakaoOAuth2;
import com.webtooni.webtooniverse.domain.user.security.sociallogin.NaverOAuth2;
import com.webtooni.webtooniverse.domain.user.security.sociallogin.SocialUserInfo;
import com.webtooni.webtooniverse.domain.webtoon.domain.WebtoonRepository;
import com.webtooni.webtooniverse.global.exception.ApiRequestException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private static final String ADMIN_TOKEN = "AAABnv/xRVklrfnYxKZ0aHgTBcXukedZygoC";
    private final KakaoOAuth2 kakaoOAuth2;
    private final NaverOAuth2 naverOAuth2;
    private final WebtoonRepository webtoonRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final GenreRepository genreRepository;
    private final UserGenreRepository userGenreRepository;

    /**
     * 카카오 소셜 로그인을 진행합니다.
     * @param authorizedCode 카카오 인증 코드
     * @return jwt token
     */
    public String kakaoLogin(String authorizedCode) {
        // 카카오 OAuth2 를 통해 카카오 사용자 정보 조회
        SocialUserInfo userInfo = kakaoOAuth2.getUserInfo(authorizedCode);
        String kakaoId = userInfo.getId().toString();
        // 패스워드 = 카카오 Id + ADMIN TOKEN
        String password = kakaoId + ADMIN_TOKEN;

        // DB 에 중복된 Kakao Id 가 있는지 확인
        User kakaoUser = userRepository.findBySocialId(kakaoId)
            .orElse(null);

        // 카카오 정보로 회원가입
        if (kakaoUser == null) {
            // 패스워드 인코딩
            String encodedPassword = passwordEncoder.encode(password);

            kakaoUser = User.builder().password(encodedPassword).socialId(kakaoId)
                .build();
            userRepository.save(kakaoUser);
        }

        Authentication kakaoUsernamePassword = new UsernamePasswordAuthenticationToken(kakaoId,
            password);
        Authentication authentication = authenticationManager.authenticate(kakaoUsernamePassword);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.createToken(kakaoId);
    }

    /**
     * 네이버 소셜 로그인을 진행합니다.
     * @param authorizedCode 네이버 인증 코드
     * @return jwt token
     */
    public String naverLogin(String authorizedCode) {
        // 네이버 OAuth2 를 통해 카카오 사용자 정보 조회
        SocialUserInfo userInfo = naverOAuth2.getUserInfo(authorizedCode);
        String naverId = userInfo.getId().toString();
        // 패스워드 = 네이버 Id + ADMIN TOKEN
        String password = naverId + ADMIN_TOKEN;

        // DB 에 중복된 naver Id 가 있는지 확인
        User naverUser = userRepository.findBySocialId(naverId)
            .orElse(null);

        // 네이버 정보로 회원가입
        if (naverUser == null) {
            // 패스워드 인코딩
            String encodedPassword = passwordEncoder.encode(password);
            naverUser = User.builder().password(encodedPassword).socialId(naverId)
                .build();
            userRepository.save(naverUser);
        }

        Authentication naverUsernamePassword = new UsernamePasswordAuthenticationToken(naverId,
            password);
        Authentication authentication = authenticationManager.authenticate(naverUsernamePassword);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.createToken(naverId);

    }

    /**
     * 유저 정보를 수정합니다.
     * @param id 유저 id
     * @param requestDto 수정할 유저 이미지, 유저 이름을 담은 dto
     */
    @Transactional
    public void updateInfo(Long id, UserInfoRequestDto requestDto) {
        User user = userRepository.findById(id).orElseThrow(
            () -> new NullPointerException("해당 회원이 존재하지 않습니다.")
        );
        String userName = requestDto.getUserName();
        Optional<User> findUser = userRepository.findByUserName(userName);
        if (user.getUserName().equals(requestDto.getUserName())) {
            user.update(requestDto);
        } else if (findUser.isPresent() && !user.getUserName().equals(requestDto.getUserName())) {
            throw new ApiRequestException("중복된 닉네임을 가진 유저가 존재합니다");
        } else {
            user.update(requestDto);
        }
    }

    /**
     * 베스트 리뷰어를 조회합니다.
     * @return 베스트 리뷰어 정보와 리뷰들을 담은 dto list
     */
    //베스트 리뷰어 가져오기
    public List<BestReviewerResponseDto> getBestReviewerRank() {
        return webtoonRepository.findBestReviewerForMain();
    }

    /**
     * 초기 유저 정보를 등록합니다.
     * @param user 유저 객체
     * @param requestDto 유저 선호 장르, 유저 이름, 유저 이미지를 dto
     */
    @Transactional
    public void pickGenre(User user, UserOnBoardingRequestDto requestDto) {
        ArrayList<String> pickedGenres = requestDto.getGenres();
        for (String pickedGenre : pickedGenres) {
            Genre genre = genreRepository.findByGenreType(pickedGenre);
            UserGenre userGenre = new UserGenre(user, genre);
            userGenreRepository.save(userGenre);
        }
        User newUser = userRepository.findById(user.getId()).orElseThrow(
            () -> new NullPointerException("해당 유저가 없습니다")
        );

        String userName = requestDto.getUserName();
        ;
        Optional<User> found = userRepository.findByUserName(userName);
        if (found.isPresent()) {
            throw new ApiRequestException("중복된 닉네임을 가진 유저가 존재합니다");
        }

        newUser.OnBoarding(requestDto);
    }

    /**
     * 유저 정보를 조회합니다.
     * @param userId 유저 id
     * @return 유저 정보
     */
    public UserInfoResponseDto getUserInfo(Long userId) {
        User findUser = userRepository.findById(userId).orElseThrow(
            () -> new NullPointerException("해당 유저를 찾지 못하였습니다.")
        );
        List<String> userGenre = userRepository.getUserGenre(userId);
        return new UserInfoResponseDto(findUser, userGenre);
    }

    /**
     * 유저 정보를 조회합니다.
     * @param userName 유저 이름
     * @return 유저 정보
     */
    public UserInfoResponseDto getUserInfoByUserName(String userName) {
        User findUser = userRepository.findByUserName(userName).orElseThrow(
            () -> new NullPointerException("해당 유저를 찾지 못하였습니다.")
        );
        List<String> userGenre = userRepository.getUserGenreByUserName(userName);
        return new UserInfoResponseDto(findUser, userGenre);
    }
}

