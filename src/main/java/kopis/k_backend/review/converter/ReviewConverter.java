package kopis.k_backend.review.converter;

import kopis.k_backend.pair.domain.Pair;
import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.review.domain.Review;
import kopis.k_backend.review.dto.ReviewRequestDto.ReviewReqDto;
import kopis.k_backend.user.domain.User;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class ReviewConverter {
    public static Review saveReview(ReviewReqDto review, User user, Performance performance, Pair pair){
        return Review.builder()
            .user(user)
            .reviewWriter(user.getNickname()) // username으로 저장
            .likeCount(0L)
            .performance(performance)
            .pair(pair)
            .content(review.getContent())
            .pairRatings(review.getPairRating())
            .performanceRatings(review.getPerformanceRating())
            .build();
    }
}
