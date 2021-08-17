package com.webtooni.webtooniverse.domain.review.domain;

import com.webtooni.webtooniverse.domain.review.dto.response.ReviewResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {

    List<Review> findMyReviews(Long userId);

    List<ReviewResponseDto> getBestOrNewReview(ReviewStatus reviewStatus);

    List<ReviewResponseDto> getNewReviewWithPageable(Pageable pageable);
}
