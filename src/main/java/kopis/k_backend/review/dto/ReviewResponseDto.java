package kopis.k_backend.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kopis.k_backend.performance.domain.PerformanceType;
import kopis.k_backend.review.domain.ViewingPartner;
import kopis.k_backend.user.domain.UserRank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.swing.text.View;
import java.time.LocalDate;
import java.util.List;

// 서버 -> 클라이언트
@NoArgsConstructor
public class ReviewResponseDto {
    @Schema(description = "ReviewResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReviewResDto {

        @Schema(description = "리뷰 id")
        private Long id;

        @Schema(description = "리뷰 작성자") // username
        private String writer;

        @Schema(description = "리뷰 작성자 프로필") // image
        private String writerProfileImage;

        @Schema(description = "작성자 == 사용자 여부")
        private Boolean isWriter;

        @Schema(description = "공연 별점 or 페어링 별점")
        private Integer rating;

        @Schema(description = "해시 태그")
        private String hashTag;

        @Schema(description = "리뷰 내용")
        private String content;

        @Schema(description = "좋아요 수")
        private Long likeCount;

        @Schema(description = "좋아요 누른 여부")
        private Boolean isPressed;

        @Schema(description = "리뷰 작성자 랭크")
        private UserRank writerRank;
    }

    @Schema(description = "ReviewListResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReviewListResDto {

        @Schema(description = "리뷰 수")
        private Long reviewCount;

        @Schema(description = "별점 평점")
        private Double averageRating;

        @Schema(description = "해시태그들")
        private List<String> hashtags;

        @Schema(description = "리뷰 리스트")
        private List<ReviewResDto> reviewList;

        @Schema(description = "리뷰 요약")
        private String reviewSummary;
    }

    @Schema(description = "MonthReviewResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MonthReviewResDto {
        @Schema(description = "리뷰 id")
        private Long id;

        @Schema(description = "공연 포스터")
        private String poster;

        @Schema(description = "공연 관람 날짜 (일)")
        private String performanceDate;
    }

    @Schema(description = "MonthReviewListResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MonthReviewListResDto {
        @Schema(description = "리뷰 수")
        private Long reviewCount;

        @Schema(description = "리뷰 리스트")
        private List<MonthReviewResDto> reviewList;

    }

    @Schema(description = "MyReviewResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MyReviewResDto {
        @Schema(description = "리뷰 id")
        private Long id;

        @Schema(description = "리뷰 내용")
        private String content;

        @Schema(description = "공연 이름")
        private String performanceName;

        @Schema(description = "공연 포스터")
        private String poster;

        @Schema(description = "공연 종류")
        private PerformanceType performanceType;

        @Schema(description = "공연 관람 날짜")
        private LocalDate performanceDate;

        @Schema(description = "공연 만족도")
        private Integer performanceRatings;

        @Schema(description = "배우1 이름")
        private String actor1Name;

        @Schema(description = "배우2 이름")
        private String actor2Name;

        @Schema(description = "페어 만족도")
        private Integer pairRatings;

        @Schema(description = "해시 태그")
        private String hashtag;

        @Schema(description = "함께 본 사람")
        private ViewingPartner viewingPartner;

        @Schema(description = "메모")
        private String memo;
    }

}