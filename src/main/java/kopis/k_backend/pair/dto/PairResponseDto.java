package kopis.k_backend.pair.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class PairResponseDto {

    @Schema(description = "SimplePairResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SimplePairResDto {
        @Schema(description = "페어 id")
        private Long pairId;

        @Schema(description = "페어1 이름")
        private String actor1;

        @Schema(description = "페어2 이름")
        private String actor2;
    }

    @Schema(description = "PairListResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PairListResDto {
        @Schema(description = "페어들")
        private List<SimplePairResDto> pairList;
    }
}