package kopis.k_backend.review.converter;

import kopis.k_backend.pair.domain.Pair;
import kopis.k_backend.performance.domain.Actor;
import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.performance.repository.ActorRepository;
import kopis.k_backend.review.domain.Review;
import kopis.k_backend.review.domain.ViewingPartner;
import kopis.k_backend.review.dto.ReviewRequestDto.ReviewReqDto;
import kopis.k_backend.review.dto.ReviewResponseDto.MyReviewResDto;
import kopis.k_backend.review.dto.ReviewResponseDto.MonthReviewResDto;
import kopis.k_backend.review.dto.ReviewResponseDto.MonthReviewListResDto;
import kopis.k_backend.review.dto.ReviewResponseDto.ReviewResDto;
import kopis.k_backend.review.dto.ReviewResponseDto.ReviewListResDto;
import kopis.k_backend.user.domain.User;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReviewConverter {
    private final ActorRepository actorRepository;

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
                .viewingPartner(ViewingPartner.NULL)
                .memo("아직 작성된 메모가 없습니다.")
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
        List<ReviewResDto> reviewResDtoList = reviews.stream()
                .map(review -> simpleReviewDto(review, ratingType, user))
                .collect(Collectors.toList());

        String reviewSummary = null;

        if (Objects.equals(ratingType, "performance") && !reviews.isEmpty()) {
            System.out.println("test");
            reviewSummary = reviews.get(0).getPerformance().getReviewSummary();
        }
        else if (Objects.equals(ratingType, "pair") && !reviews.isEmpty()) {
            reviewSummary = reviews.get(0).getPair().getReviewSummary();
        }

        return ReviewListResDto.builder()
                .averageRating(rating)
                .hashtags(hashtags)
                .reviewCount(reviewCount)
                .reviewList(reviewResDtoList)
                .reviewSummary(reviewSummary)
                .build();
    }

    public static MonthReviewResDto monthReviewResDto(Review review) {

        return MonthReviewResDto.builder()
                .id(review.getId())
                .poster(review.getPerformance().getPoster())
                .performanceDate(String.valueOf(review.getPerformanceDate().getDayOfMonth()))
                .build();
    }

    public static MonthReviewListResDto monthReviewListResDto(List<Review> reviews, Long reviewCount) {
        List<MonthReviewResDto> monthReviewResDtoList = reviews.stream()
                .map(ReviewConverter::monthReviewResDto)
                .toList();

        return MonthReviewListResDto.builder()
                .reviewCount(reviewCount)
                .reviewList(monthReviewResDtoList)
                .build();
    }

    public MyReviewResDto myReviewResDto(Review review) {

        Actor actor1 = actorRepository.findById(review.getPair().getActor1())
                .orElseThrow(() -> new RuntimeException("actor not found"));

        Actor actor2 = actorRepository.findById(review.getPair().getActor2())
                .orElseThrow(() -> new RuntimeException("actor not found"));

        return MyReviewResDto.builder()
                .id(review.getId())
                .performanceName(review.getPerformance().getTitle())
                .poster(review.getPerformance().getPoster())
                .performanceType(review.getPerformance().getPerformanceType())
                .performanceDate(review.getPerformanceDate())
                .performanceRatings(review.getPerformanceRatings())
                .pairRatings(review.getPairRatings())
                .hashtag(review.getHashtag())
                .viewingPartner(review.getViewingPartner())
                .memo(review.getMemo())
                .content(review.getContent())
                .actor1Name(actor1.getActorName())
                .actor2Name(actor2.getActorName())
                .build();
    }
}
