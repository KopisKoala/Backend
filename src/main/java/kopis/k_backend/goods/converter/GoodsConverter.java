package kopis.k_backend.goods.converter;

import kopis.k_backend.goods.domain.Goods;
import kopis.k_backend.goods.dto.GoodsResponseDto.GoodsListResDto;
import kopis.k_backend.goods.dto.GoodsResponseDto.GoodsResDto;
import lombok.NoArgsConstructor;
import java.util.List;

@NoArgsConstructor
public class GoodsConverter {
    public static GoodsResDto simpleGoodsDto(Goods goods) {
        return GoodsResDto.builder()
                .id(goods.getId())
                .title(goods.getPerformance().getTitle())
                .name(goods.getName())
                .price(goods.getPrice())
                .image(goods.getImage())
                .build();
    }

    public static GoodsListResDto goodsListResDto(List<Goods> goodsList) {
        List<GoodsResDto> goodsResDtoList = goodsList.stream()
                .map(GoodsConverter::simpleGoodsDto)
                .toList();

        Long goodsListCount = (long) goodsList.size();

        return GoodsListResDto.builder()
                .goodsCount(goodsListCount)
                .goodsList(goodsResDtoList)
                .build();
    }
}
