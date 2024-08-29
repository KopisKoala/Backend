package kopis.k_backend.search.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kopis.k_backend.pair.dto.PairResponseDto.PairDetailListResDto;
import kopis.k_backend.performance.dto.ActorResponseDto.HomeSearchActorListResDto;
import kopis.k_backend.performance.dto.PerformanceResponseDto.HomeSearchPerformanceListResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class SearchResponseDto {

    @Schema(description = "HomeSearchResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HomeSearchResDto {
        @Schema(description = "공연 목록")
        private HomeSearchPerformanceListResDto performances;

        @Schema(description = "배우 목록")
        private HomeSearchActorListResDto actors;

    }

    @Schema(description = "PairSearchResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PairSearchResDto {
        @Schema(description = "총 페어 수")
        private Long totalPairCount;

        @Schema(description = "페어 목록들")
        private List<PairDetailListResDto> pairDetailListResDtos;
    }
}
