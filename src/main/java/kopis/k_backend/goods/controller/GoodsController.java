package kopis.k_backend.goods.controller;

import kopis.k_backend.global.api_payload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kopis.k_backend.global.api_payload.SuccessCode;
import kopis.k_backend.goods.converter.GoodsConverter;
import kopis.k_backend.goods.domain.Goods;
import kopis.k_backend.goods.service.GoodsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import kopis.k_backend.goods.dto.GoodsResponseDto.GoodsListResDto;

import java.util.List;


@Tag(name = "굿즈", description = "굿즈 관련 api 입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/goods")
public class GoodsController {
    private final GoodsService goodsService;

    @Operation(summary = "모든 굿즈 목록 반환", description = "존재하는 모든 굿즈 목록을 반환하는 메서드입닌다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "GOODS_2001", description = "모든 굿즈 목록을 반환 완료했습니다.")
    })
    @GetMapping(value = "/goods/list/all")
    public ApiResponse<GoodsListResDto> getGoodsListAll() {
        List<Goods> goodsList = goodsService.findAll();

        return ApiResponse.onSuccess(SuccessCode.GOODS_LIST_VIEW_SUCCESS, GoodsConverter.goodsListResDto(goodsList));
    }


}
