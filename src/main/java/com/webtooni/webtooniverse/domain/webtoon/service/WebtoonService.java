package com.webtooni.webtooniverse.domain.webtoon.service;


import com.webtooni.webtooniverse.domain.webtoon.domain.Webtoon;

import com.webtooni.webtooniverse.domain.webtoon.domain.WebtoonRepository;
import com.webtooni.webtooniverse.domain.webtoon.dto.response.MonthRankResponseDto;
import com.webtooni.webtooniverse.domain.webtoon.dto.response.PlatformRankResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;



import com.webtooni.webtooniverse.domain.genre.domain.Genre;
import com.webtooni.webtooniverse.domain.review.domain.Review;
import com.webtooni.webtooniverse.domain.webtoon.dto.response.SimilarGenreToonDto;
import com.webtooni.webtooniverse.domain.webtoon.dto.response.WebtoonDetailDto;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class WebtoonService {

    private final WebtoonRepository webtoonRepository;


    //이번달 웹투니버스 종합순위
    public List<MonthRankResponseDto> getMonthTotalRank(){
        List<Webtoon> monthTotalRank = webtoonRepository.getTotalRank();
        return monthTotalRank
                .stream()
                .map(MonthRankResponseDto::new)
                .collect(Collectors.toList());
    }

    //웹투니버스 네이버 웹툰 Top10
    public List<PlatformRankResponseDto> getMonthNaverRank(){
        List<Webtoon> monthNaverRank = webtoonRepository.getNaverRank();
        return monthNaverRank
                .stream()
                .map(PlatformRankResponseDto::new)
                .collect(Collectors.toList());
    }

    //웹투니버스 카카오 웹툰 Top10
    public List<PlatformRankResponseDto> getMonthKakaoRank(){
        List<Webtoon> monthKakaoRank = webtoonRepository.getKakaoRank();
        return monthKakaoRank
                .stream()
                .map(PlatformRankResponseDto::new)
                .collect(Collectors.toList());
    }


    /**
     * 웹툰의 기본정보들과 리뷰 리스트를 재공하는 구현체입니다.
     *
     * @param id 웹툰 id
     * @return WebtoonDetailDto
     */
    public WebtoonDetailDto getDetailAndReviewList(Long id) {
        //해당 웹툰 정보 찾기
        Webtoon webtoon = webtoonRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 id는 존재하지 않습니다.")
        );

        //해당 웹툰의 장르 찾기
        List<Genre> WebToonGenre = webtoonRepository.findWebToonGenre(webtoon);
        List<String> genreList = new ArrayList<>();
        genreList.add(WebToonGenre.get(0).getGenreType());
        genreList.add(WebToonGenre.get(1).getGenreType());

        //해당 웹툰의 리뷰 찾기
        List<Review> reviewList = webtoonRepository.findReviewByWebToonId(id);

        return new WebtoonDetailDto(webtoon, genreList, reviewList);
    }

    /**
     * 비슷한 장르의 웹툰을 추천하는 기능을 제공하는 구현체입니다.
     *
     * @param id 웹툰 id
     * @return List<SimilarGenreToonDto> 웹툰 리스트
     */
    public List<SimilarGenreToonDto> getSimilarGenre(Long id) {

        //웹툰 찾기
        Webtoon webtoon = webtoonRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 id의 웹툰은 존재하지 않습니다.")
        );

        List<Genre> genre = webtoonRepository.findWebToonGenre(webtoon);

        //비슷한 장르의 웹툰 찾기
        List<Webtoon> webtoonList = webtoonRepository.findSimilarWebtoonByGenre(genre.get(1).getGenreType(), webtoon);

        return webtoonList.stream()
                .map(SimilarGenreToonDto::new)
                .collect(Collectors.toList());
    }

}
