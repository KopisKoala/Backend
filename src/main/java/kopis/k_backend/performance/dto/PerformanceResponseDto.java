package kopis.k_backend.performance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class PerformanceResponseDto {
    @Schema(description = "PerformaceResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PerformanceResDto {

        @Schema(description = "공연 id")
        private Long id;

        @Schema(description = "공연장 이름")
        private String hallName;

        @Schema(description = "공연 이름")
        private String title;

        @Schema(description = "공연 시작 날짜")
        private String startDate;

        @Schema(description = "공연 끝 날짜")
        private String endDate;

        @Schema(description = "공연 포스터")
        private String poster;

        @Schema(description = "평점 평균")
        private Double ratingAverage;

        @Schema(description = "리뷰 요약")
        private String reviewSummary;
    }

    @Schema(description = "PerformanceListResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PerformanceListResDto {

        @Schema(description = "공연 수")
        private Long performanceCount;

        @Schema(description = "공연 리스트")
        private List<PerformanceResDto> performanceList;
    }

    @Schema(description = "SimpleRankPerformanceDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SimpleRankPerformanceDto {

        @Schema(description = "공연 id")
        private Long perfId;

        @Schema(description = "공연 이름")
        private String title;

        @Schema(description = "공연 포스터")
        private String poster;

        @Schema(description = "공연 순위")
        private Integer rank;

        @Schema(description = "공연장 이름")
        private String hall;

        @Schema(description = "공연 상영 기간")
        private String duration;

    }

    @Schema(description = "RankPerformanceListDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RankPerformanceListDto {

        @Schema(description = "공연 리스트")
        private List<SimpleRankPerformanceDto> performanceList;

    }
}
