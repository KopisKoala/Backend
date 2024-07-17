package kopis.k_backend.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
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

        @Schema(description = "공연 별점")
        private int performanceRating;

        @Schema(description = "페어링 별점")
        private int pairingRating;

        @Schema(description = "리뷰 작성자") // 닉네임
        private String reviewWriter;

        @Schema(description = "리뷰 작성자 프로필") // 이미지
        private String writerProfileImage;

        @Schema(description = "좋아요 수")
        private Long likeCount;

        @Schema(description = "해시 태그")
        private String hashTag;

        @Schema(description = "리뷰 내용")
        private String reviewContent;

        @Schema(description = "리뷰 작성일", example = "2024-07-17", pattern = "yyyy-MM-dd")
        private LocalDateTime reviewCreatedAt;

    }

    @Schema(description = "ReviewListResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReviewListResDto {

        @Schema(description = "리뷰 리스트")
        private List<ReviewResDto> reviewList;

    }
}