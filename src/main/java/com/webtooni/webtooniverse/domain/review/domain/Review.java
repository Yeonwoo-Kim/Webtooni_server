package com.webtooni.webtooniverse.domain.review.domain;
import com.webtooni.webtooniverse.domain.user.domain.User;
import com.webtooni.webtooniverse.domain.user.domain.UserGrade;
import com.webtooni.webtooniverse.domain.webtoon.domain.Webtoon;
import com.webtooni.webtooniverse.global.utils.TimeStamped;
import com.webtooni.webtooniverse.domain.review.dto.request.ReviewContentRequestDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private LocalDateTime createDate;

    private String reviewContent;

    private float userPointNumber;

    private int likeCount;

    @ManyToOne
    @JoinColumn(name = "toon_id")
    private Webtoon webtoon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Review(String reviewContent, float userPointNumber, int likeCount, Webtoon webtoon, User user) {
        this.reviewContent = reviewContent;
        this.userPointNumber = userPointNumber;
        this.likeCount = likeCount;
        this.webtoon = webtoon;
        this.user = user;
    }

    /**
     * 처음 웹툰에 별점을 등록하는 경우엔 별점을 등록
     */
    @Builder
    public Review(float userPointNumber, Webtoon webtoon, User user) {
        this.userPointNumber = userPointNumber;
        this.webtoon = webtoon;
        this.user = user;
    }

    public static Review of(float userPointNumber,Webtoon webtoon,User user)
    {
        return new Review(userPointNumber,webtoon,user);
    }

    /**
     * 기존에 별점이 존재하는 경우엔 별점을 수정한다.
     */
    public void changeUserPoint(float userPointNumber)
    {
        this.userPointNumber=userPointNumber;
    }


    /**
     *  Review에 User,Webtoon 정보 넣어준다.
     */
    public void insertWebToonAndUser(Webtoon webtoon, User user) {
        this.webtoon = webtoon;
        this.user = user;
    }

    /**
     * 리뷰를 작성(수정)한다.
     */
    public void changeReviewContent(ReviewContentRequestDto reviewDto) {
        this.reviewContent=reviewDto.getReviewContent();
        this.createDate=LocalDateTime.now();
    }

    /**
     * 리뷰를 삭제한다.
     */
    public void deleteReview()
    {
        this.reviewContent=null;
    }

    /**
     * 좋아요를 처음 누르는 사용자인 경우 좋아요수+1
     */
    public void plusLikeCount() {
        this.likeCount+=1;
    }

    /**
     * 전체 좋아요수를 1개 줄인다.
     */
    public void minusLikeCount()
    {
        this.likeCount-=1;
    }

}
