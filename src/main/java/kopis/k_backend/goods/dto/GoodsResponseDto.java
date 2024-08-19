package kopis.k_backend.goods.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kopis.k_backend.performance.domain.Performance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class GoodsResponseDto {
    @Schema(description = "GoodsResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GoodsResDto {
        @Schema(description = "굿즈 id")
        private Long id;

        @Schema(description = "공연 이름")
        private String title;

        @Schema(description = "굿즈 이름")
        private String name;

        @Schema(description = "굿즈 가격")
        private Integer price;

        @Schema(description = "굿즈 사진")
        private String image;
    }

    @Schema(description = "GoodsListResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GoodsListResDto {
        @Schema(description = "총 굿즈 개수")
        private Long goodsCount;

        @Schema(description = "굿즈 리스트")
        private List<GoodsResDto> goodsList;
    }
}
