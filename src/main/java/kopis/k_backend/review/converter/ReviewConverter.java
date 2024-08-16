package kopis.k_backend.review.converter;

import kopis.k_backend.pair.domain.Pair;
import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.review.domain.Review;
import kopis.k_backend.review.dto.ReviewRequestDto.ReviewReqDto;
import kopis.k_backend.review.dto.ReviewResponseDto.MonthReviewListResDto;
import kopis.k_backend.review.dto.ReviewResponseDto.ReviewResDto;
import kopis.k_backend.review.dto.ReviewResponseDto.ReviewListResDto;
import kopis.k_backend.user.domain.User;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@NoArgsConstructor
public class ReviewConverter {
    public static Review saveReview(ReviewReqDto review, User user, Performance performance, Pair pair){
        return Review.builder()
                .writer(user)
                .performance(performance)
                .pair(pair)
                .writerName(user.getUsername())
                .likeCount(0L)
                .content(review.getContent())
                .pairRatings(review.getPairRating())
                .performanceRatings(review.getPerformanceRating())
                .hashtag(review.getHashTag())
                .performanceDate(review.getPerformanceDate())
                .build();
    }

    public static ReviewResDto simpleReviewDto(Review review, String ratingType, User user) {
        Boolean isWriter = (Objects.equals(user, review.getWriter()));

        Integer rating = 0;
        if(Objects.equals(ratingType, "pair")) rating = review.getPairRatings();
        else if(Objects.equals(ratingType, "performance")) rating = review.getPerformanceRatings();

        boolean isPressed = review.getReviewLikes().stream()
                .anyMatch(reviewLike -> reviewLike.getUser().equals(user));

        return ReviewResDto.builder()
                .id(review.getId())
                .writer(review.getWriter().getNickname())
                .writerProfileImage(review.getWriter().getProfileImage())
                .isWriter(isWriter)
                .rating(rating)
                .content(review.getContent())
                .likeCount(review.getLikeCount())
                .hashTag(review.getHashtag())
                .isPressed(isPressed)
                .writerRank(review.getWriter().getUserRank())
                .build();
    }

    public static ReviewListResDto reviewListResDto(List<Review> reviews, Long reviewCount, Double rating, String ratingType, List<String> hashtags, User user) {
        List<ReviewResDto> reviewResDtos = reviews.stream()
                .map(review -> simpleReviewDto(review, ratingType, user))
                .collect(Collectors.toList());

        return ReviewListResDto.builder()
                .averageRating(rating)
                .hashtags(hashtags)
                .reviewCount(reviewCount)
                .reviewList(reviewResDtos)
                .build();
    }

    public static MonthReviewListResDto monthReviewListResDto(List<Review> reviews, String ratingType, User user, Long reviewCount) {
        List<ReviewResDto> reviewResDtos = reviews.stream()
                .map(review -> simpleReviewDto(review, ratingType, user))
                .collect(Collectors.toList());

        return MonthReviewListResDto.builder()
                .reviewCount(reviewCount)
                .reviewList(reviewResDtos)
                .build();
    }

}
