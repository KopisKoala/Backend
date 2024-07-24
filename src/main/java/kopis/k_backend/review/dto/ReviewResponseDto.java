package kopis.k_backend.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    }

    @Schema(description = "ReviewListResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReviewListResDto {

        @Schema(description = "리뷰 수")
        private Long reviewCount;

        @Schema(description = "리뷰 리스트")
        private List<ReviewResDto> reviewList;

    }
}