package kopis.k_backend.search.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kopis.k_backend.performance.dto.ActorResponseDto.ActorListResDto;
import kopis.k_backend.performance.dto.PerformanceResponseDto.PerformanceListResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SearchResponseDto {

    @Schema(description = "SearchResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SearchResDto {
        @Schema(description = "공연 목록")
        private PerformanceListResDto performances;

        @Schema(description = "배우 목록")
        private ActorListResDto actors;

    }
}
