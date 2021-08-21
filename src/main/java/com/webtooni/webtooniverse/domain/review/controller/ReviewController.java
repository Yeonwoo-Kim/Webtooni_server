package com.webtooni.webtooniverse.domain.review.controller;

import com.webtooni.webtooniverse.domain.review.dto.request.ReviewContentRequestDto;
import com.webtooni.webtooniverse.domain.review.dto.request.WebtoonPointRequestDto;
import com.webtooni.webtooniverse.domain.review.dto.response.MyReviewResponseDto;
import com.webtooni.webtooniverse.domain.review.dto.response.ReviewCreateResponseDto;
import com.webtooni.webtooniverse.domain.review.dto.response.ReviewLikeResponseDto;
import com.webtooni.webtooniverse.domain.review.dto.response.ReviewMainResponseDto;
import com.webtooni.webtooniverse.domain.review.dto.response.ReviewStarResponseDto;
import com.webtooni.webtooniverse.domain.review.service.ReviewService;
import com.webtooni.webtooniverse.domain.user.domain.User;
import com.webtooni.webtooniverse.domain.user.security.UserDetailsImpl;
import java.util.List;
import javax.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@RestController
public class ReviewController {


    private final ReviewService reviewService;

    //리뷰 전체 목록 최신 순
    @GetMapping("reviews/new")
    public ReviewLikeResponseDto getNewReview(@PathParam("page") int page,
        @PathParam("size") int size
        , @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return reviewService.getNewReview(userDetails, page, size);
    }

    //리뷰 전체 목록 최신 순
    @GetMapping("reviews/best")
    public ReviewLikeResponseDto getBestReview(@PathParam("page") int page,
        @PathParam("size") int size
        , @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return reviewService.getBestReview(userDetails, page, size);
    }

    //메인페이지에 리뷰(최신순/베스트순) 불러오기
    @GetMapping("rank/reviews")
    public ReviewMainResponseDto getTotalReviews() {
        return reviewService.getMainReview();
    }

    //리뷰 작성(수정)
    @PutMapping("reviews/{id}")
    public ReviewCreateResponseDto updateReview(@PathVariable Long id,
        @RequestBody ReviewContentRequestDto reviewDto) {
        return reviewService.updateReview(id, reviewDto);
    }

    /**
     * 웹툰에 별점을 준다.
     *
     * @param reviewStarDto 웹툰 id,userPointNumber 담은 dto
     * @return 리뷰 id
     */
    @PutMapping("reviews/star")
    public ReviewStarResponseDto updateStar(@RequestBody WebtoonPointRequestDto reviewStarDto
        , @AuthenticationPrincipal UserDetailsImpl userDetails) {

        checkUser(userDetails);
        User user = userDetails.getUser();

        return reviewService.clickWebtoonPointNumber(reviewStarDto, user);
    }

    //내가 쓴 리뷰 목록
    @GetMapping("user/me/reviews")
    public List<MyReviewResponseDto> getMyReviews(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        checkUser(userDetails);
        return reviewService.getMyReviews(userDetails.getUser().getUserName());
    }

    //리뷰 삭제
    @DeleteMapping("reviews/{id}")
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }

    /**
     * 리뷰에 좋아요를 누른다.
     *
     * @param id          review id
     * @param userDetails user 정보
     */
    @PostMapping("reviews/{id}/like")
    public void clickReviewLike(@PathVariable Long id,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        checkUser(userDetails);
        User user = userDetails.getUser();

        reviewService.clickReviewLike(id, user);
    }

    private void checkUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유저 정보를 찾을 수 없습니다.");
        }

    }
}

