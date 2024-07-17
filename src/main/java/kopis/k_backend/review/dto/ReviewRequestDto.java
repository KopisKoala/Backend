package kopis.k_backend.review.dto;

import kopis.k_backend.review.dto.ReviewResponseDto.ReviewResDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

// 클라이언트 -> 서버

@NoArgsConstructor
public class ReviewRequestDto {
    @Schema(description = "ReviewReqDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReviewReqDto {

        @Schema(description = "리뷰 작성자")
        private String writer;

        @Schema(description = "공연 id")
        private Long performanceId;

        @Schema(description = "공연 별점")
        private Integer performanceRating;

        @Schema(description = "페어 id")
        private Long pairId;

        @Schema(description = "페어 별점")
        private Integer pairRating;

        @Schema(description = "해시 태그")
        private String hashTag;

        @Schema(description = "리뷰 내용")
        private String content;

    }

}