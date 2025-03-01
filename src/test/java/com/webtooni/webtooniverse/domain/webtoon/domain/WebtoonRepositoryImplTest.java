//package com.webtooni.webtooniverse.domain.webtoon.domain;
//
//import com.webtooni.webtooniverse.domain.genre.domain.Genre;
//import com.webtooni.webtooniverse.domain.genre.domain.GenreRepository;
//import com.webtooni.webtooniverse.domain.review.domain.Review;
//import com.webtooni.webtooniverse.domain.review.domain.ReviewRepository;
//import com.webtooni.webtooniverse.domain.user.domain.User;
//import com.webtooni.webtooniverse.domain.user.domain.UserGenre;
//import com.webtooni.webtooniverse.domain.user.domain.UserGenreRepository;
//import com.webtooni.webtooniverse.domain.user.domain.UserRepository;
//import com.webtooni.webtooniverse.domain.user.dto.response.BestReviewerResponseDto;
//import com.webtooni.webtooniverse.domain.webtoon.dto.response.WebtoonAndGenreResponseDto;
//import com.webtooni.webtooniverse.domain.webtoonGenre.WebtoonGenre;
//import com.webtooni.webtooniverse.domain.webtoonGenre.WebtoonGenreRepository;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.transaction.Transactional;
//import java.time.DayOfWeek;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@Transactional
//class WebtoonRepositoryImplTest {
//
//    @Autowired
//    WebtoonRepository webtoonRepository;
//    @Autowired
//    UserGenreRepository userGenreRepository;
//    @Autowired
//    ReviewRepository reviewRepository;
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    GenreRepository genreRepository;
//    @Autowired
//    WebtoonGenreRepository webtoonGenreRepository;
//
//
//    @BeforeEach
//    void setUp() {
//        User user1 = new User("user1");
//        User user2 = new User("user2");
//        User user3 = new User("user3");
//        userRepository.save(user1);
//        userRepository.save(user2);
//        userRepository.save(user3);
//
//        Webtoon webtoon1 = new Webtoon("웹툰1", "작가1", "내용1", "이미지1", "월", "url", "15", "네이버", 4.0f,
//            0, 4, false);
//        Webtoon webtoon2 = new Webtoon("웹툰2", "작가1", "내용1", "이미지1", "월", "url", "15", "네이버", 4.0f,
//            0, 4, false);
//        Webtoon webtoon3 = new Webtoon("웹툰3", "작가1", "내용1", "이미지1", "월", "url", "15", "네이버", 4.0f,
//            0, 4, false);
//        Webtoon webtoon4 = new Webtoon("웹툰4", "작가1", "내용1", "이미지1", "월", "url", "15", "네이버", 13.0f,
//            0, 4, true);
//        Webtoon webtoon5 = new Webtoon("웹툰5", "작가1", "내용1", "이미지1", "월", "url", "15", "네이버", 15.0f,
//            0, 4, true);
//        Webtoon webtoon6 = new Webtoon("웹툰6", "작가1", "내용1", "이미지1", "월", "url", "15", "네이버", 14.5f,
//            0, 4, true);
//        webtoonRepository.save(webtoon1);
//        webtoonRepository.save(webtoon2);
//        webtoonRepository.save(webtoon3);
//        webtoonRepository.save(webtoon4);
//        webtoonRepository.save(webtoon5);
//        webtoonRepository.save(webtoon6);
//        Review review1 = createReview("리뷰 내용1", 5.0f, 1, user1, webtoon1);
//        Review review2 = createReview("리뷰 내용2", 5.0f, 2, user2, webtoon2);
//        Review review3 = createReview("리뷰 내용3", 4.0f, 3, user2, webtoon3);
//        Review review4 = createReview("리뷰 내용4", 6.5f, 4, user3, webtoon1);
//        Review review5 = createReview("리뷰 내용5", 8.0f, 5, user3, webtoon5);
//        Review review6 = createReview("리뷰 내용6", 7.0f, 6, user3, webtoon6);
//        reviewRepository.save(review1);
//        reviewRepository.save(review2);
//        reviewRepository.save(review3);
//        reviewRepository.save(review4);
//        reviewRepository.save(review5);
//        reviewRepository.save(review6);
//        Genre genre1 = new Genre("코믹");
//        Genre genre2 = new Genre("멜로");
//        Genre genre3 = new Genre("액션");
//        genreRepository.save(genre1);
//        genreRepository.save(genre2);
//        genreRepository.save(genre3);
//        UserGenre userGenre1 = new UserGenre(user1, genre1);
//        UserGenre userGenre2 = new UserGenre(user2, genre2);
//        UserGenre userGenre3 = new UserGenre(user2, genre3);
//        userGenreRepository.save(userGenre1);
//        userGenreRepository.save(userGenre2);
//        userGenreRepository.save(userGenre3);
//        WebtoonGenre webtoonGenre1 = new WebtoonGenre(genre1, webtoon1);
//        WebtoonGenre webtoonGenre2 = new WebtoonGenre(genre2, webtoon2);
//        WebtoonGenre webtoonGenre3 = new WebtoonGenre(genre3, webtoon3);
//        WebtoonGenre webtoonGenre4 = new WebtoonGenre(genre1, webtoon4);
//        WebtoonGenre webtoonGenre5 = new WebtoonGenre(genre2, webtoon5);
//        WebtoonGenre webtoonGenre6 = new WebtoonGenre(genre3, webtoon6);
//        webtoonGenreRepository.save(webtoonGenre1);
//        webtoonGenreRepository.save(webtoonGenre2);
//        webtoonGenreRepository.save(webtoonGenre3);
//        webtoonGenreRepository.save(webtoonGenre4);
//        webtoonGenreRepository.save(webtoonGenre5);
//        webtoonGenreRepository.save(webtoonGenre6);
//    }
//
//    @AfterEach
//    void tearDown() {
//        webtoonRepository.deleteAll();
//        userGenreRepository.deleteAll();
//        reviewRepository.deleteAll();
//        userRepository.deleteAll();
//        genreRepository.deleteAll();
//        webtoonGenreRepository.deleteAll();
//    }
//
//
//    @Test
//    @DisplayName("베스트 리뷰어 / 리뷰 수 / 좋아요 수 검색")
//    void findBestReviewer() {
//        List<BestReviewerResponseDto> best = webtoonRepository.findBestReviewerForMain();
//        for (BestReviewerResponseDto bestReviewerResponseDto : best) {
//            System.out.println(
//                "bestReviewerResponseDto = " + bestReviewerResponseDto.getUser().getUserName() + " "
//                    + bestReviewerResponseDto.getReviewCount() + " " + bestReviewerResponseDto
//                    .getLikeCount());
//        }
//    }
//
////    @Nested
////    @DisplayName("금주의 웹툰 평론가 추천")
////    class 금주의웹툰평론가추천{
////
////        @Test
////        @DisplayName("금주의 웹툰 평론가 일치 여부")
////        void 금주의_웹툰평론가_추천_평론가() {
////            //given
////            //when
////            User bestReviewer = webtoonRepository.findBestReviewer();
////            System.out.println("bestReviewer = " + bestReviewer.getUserName());
////            List<WebtoonAndGenreResponseDto> bestReviewerWebtoons = webtoonRepository.findBestReviewerWebtoon(bestReviewer);
////            //then
////            assertThat(bestReviewerWebtoons.size()).isEqualTo(3);
////        }
////
////        @Test
////        @DisplayName("웹툰 평가순으로 나열")
////        void 금주의_웹툰평론가_추천_평가순() {
////            //given
////            //when
////            User bestReviewer = webtoonRepository.findBestReviewer();
////            List<WebtoonAndGenreResponseDto> bestReviewerWebtoons = webtoonRepository.findBestReviewerWebtoon(bestReviewer);
////            //then
////            assertThat(bestReviewerWebtoons.get(0).getToonTitle()).isEqualTo("웹툰5");
////            assertThat(bestReviewerWebtoons.get(1).getToonTitle()).isEqualTo("웹툰6");
////            assertThat(bestReviewerWebtoons.get(2).getToonTitle()).isEqualTo("웹툰1");
////        }
////
////    }
//
//    @Nested
//    @DisplayName("완결 웹툰 추천")
//    class 완결웹툰추천 {
//
//        @Test
//        @DisplayName("완결작 여부")
//        void 완결웹툰추천_완결확인() {
//            //given
//            //when
//            List<WebtoonAndGenreResponseDto> finishedWebtoon = webtoonRepository
//                .findFinishedWebtoon();
//            //then
//            for (WebtoonAndGenreResponseDto webtoon : finishedWebtoon) {
//                assertThat(webtoon.isFinished()).isEqualTo(true);
//            }
//        }
//
//        @Test
//        @DisplayName("웹툰 평가순으로 나열")
//        void 완결웹툰추천_점수순확인() {
//            //given
//            //when
//            List<WebtoonAndGenreResponseDto> finishedWebtoon = webtoonRepository
//                .findFinishedWebtoon();
//            //then
//            assertThat(finishedWebtoon.get(0).getToonTitle()).isEqualTo("웹툰5");
//            assertThat(finishedWebtoon.get(1).getToonTitle()).isEqualTo("웹툰6");
//            assertThat(finishedWebtoon.get(2).getToonTitle()).isEqualTo("웹툰4");
//        }
//    }
//
////    @Nested
////    @DisplayName("유저 취향 랜덤 추천")
////    class 유저취향랜덤추천{
////
////        @Test
////        @DisplayName("유저 취향 장르")
////        void 유저취향랜덤추천_유저취향() {
////            //given
////            User user = userRepository.findByUserName("user1").get();
////            //when
////            List<Webtoon> webtoons = webtoonRepository.findUserGenreWebtoon(user);
////            //then
////            for (Webtoon webtoon : webtoons) {
////                System.out.println("webtoon - " + webtoon.getId() + " wetoontitle - " + webtoon.getToonTitle());
////                WebtoonGenre webtoonGenre = webtoonGenreRepository.findByWebtoon(webtoon).get();
////                String genreType = webtoonGenre.getGenre().getGenreType();
////                assertThat(genreType).isEqualTo("코믹");
////            }
////        }
////
////        @Test
////        @DisplayName("웹툰이 유저 취향 장르와 일치")
////        void 유저취향랜덤추천_웹툰취향() {
////            //given
////            User user = userRepository.findByUserName("user1").get();
////            //when
////            List<Webtoon> webtoons = webtoonRepository.findUserGenreWebtoon(user);
////            //then
////            assertThat(webtoons.get(0).getToonTitle() ).isEqualTo("웹툰1");
////            assertThat(webtoons.get(1).getToonTitle()).isEqualTo("웹툰4");
////        }
////    }
//
//
//    @Nested
//    @DisplayName("비슷한 취향 사용자 추천 웹툰")
//    class 비슷한취향사용자추천웹툰 {
//
//        @Test
//        @DisplayName("비슷한 취향의 사용자 있을 때")
//        void 비슷한취향사용자추천웹툰_비슷한취향사용자있을때() {
//            //given
//            User user = userRepository.findByUserName("user1").get();
//            //when
//            List<WebtoonAndGenreResponseDto> webtoons = webtoonRepository
//                .findSimilarUserWebtoon(user);
//            //then
//            assertThat(webtoons.get(0).getToonTitle()).isEqualTo("웹툰5");
//            assertThat(webtoons.get(1).getToonTitle()).isEqualTo("웹툰6");
//        }
//
//        @Test
//        @DisplayName("비슷한 취향의 사용자 없을 때")
//        void 비슷한취향사용자추천웹툰_비슷한취향사용자없을때() {
//            //given
//            User user = userRepository.findByUserName("user2").get();
//            //when
//            List<WebtoonAndGenreResponseDto> webtoons = webtoonRepository
//                .findSimilarUserWebtoon(user);
//            //then
//            assertThat(webtoons.get(0).getToonTitle()).isEqualTo("웹툰2");
//            assertThat(webtoons.get(1).getToonTitle()).isEqualTo("웹툰5");
//        }
//
//    }
//
//    private Review createReview(String reviewContent, float userPointNumber, int likeCount,
//        User user, Webtoon webtoon) {
//        return Review.builder()
//            .reviewContent(reviewContent)
//            .userPointNumber(userPointNumber)
//            .likeCount(likeCount)
//            .user(user)
//            .webtoon(webtoon)
//            .build();
//    }
//}
