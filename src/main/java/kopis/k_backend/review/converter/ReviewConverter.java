package kopis.k_backend.review.converter;

import kopis.k_backend.pair.domain.Pair;
import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.review.domain.Review;
import kopis.k_backend.review.dto.ReviewRequestDto.ReviewReqDto;
import kopis.k_backend.review.dto.ReviewResponseDto.ReviewResDto;
import kopis.k_backend.review.dto.ReviewResponseDto.ReviewListResDto;
import kopis.k_backend.user.domain.User;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class ReviewConverter {
    public static Review saveReview(ReviewReqDto review, User user, Performance performance, Pair pair){
        return Review.builder()
                .user(user)
                .writer(user.getNickname()) // username으로 저장
                .likeCount(0L)
                .performance(performance)
                .pair(pair)
                .content(review.getContent())
                .pairRatings(review.getPairRating())
                .hashtag(review.getHashTag())
                .performanceRatings(review.getPerformanceRating())
                .build();
    }

    public static ReviewResDto simpleReviewDto(Review review) {
        return ReviewResDto.builder()
                .id(review.getId())
                .writer(review.getWriter())
                .content(review.getContent())
                .likeCount(review.getLikeCount())
                .hashTag(review.getHashtag())
                .writerProfileImage(review.getUser().getProfileImage())
                .build();
    }

    public static ReviewListResDto reviewListResDto(List<Review> reviews) {
        List<ReviewResDto> reviewResDtos
                = reviews.stream().map(ReviewConverter::simpleReviewDto).toList();

        return ReviewListResDto.builder()
                .reviewList(reviewResDtos)
                .build();
    }
}
