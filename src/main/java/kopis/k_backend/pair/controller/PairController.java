package kopis.k_backend.pair.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kopis.k_backend.global.api_payload.ApiResponse;
import kopis.k_backend.global.api_payload.SuccessCode;
import kopis.k_backend.pair.Service.PairService;
import kopis.k_backend.pair.converter.PairConverter;
import kopis.k_backend.pair.domain.Pair;
import kopis.k_backend.pair.dto.PairResponseDto.PopularPairListResDto;
import kopis.k_backend.pair.dto.PairResponseDto.PairListResDto;
import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.performance.service.PerformanceService;
import kopis.k_backend.user.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "페어", description = "페어 관련 api 입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/pair")
public class PairController {
    private final PairService pairService;
    private final PerformanceService performanceService;
    private final PairConverter pairConverter;
    @Operation(summary = "공연에 따른 페어 반환", description = "공연에 따른 페어들을 반환하는 메서드입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "PAIR_2001", description = "공연에 맞는 페어들을 반환 완료했습니다.")
    })
    @GetMapping(value = "/{performance-id}/pairs")
    public ApiResponse<PairListResDto> create(
            @PathVariable(name = "performance-id") Long id
    ){
        Performance performance = performanceService.findById(id);
        List<Pair> pairs = pairService.findPairsByPerformance(performance);

        return ApiResponse.onSuccess(SuccessCode.PERFORMANCE_MATCH_PAIRS_SUCCESS, pairConverter.pairListResDto(pairs));
    }

    @Operation(summary = "인기 있는 페어 목록 반환", description = "현재 가장 인기 있는 페어 목록을 반환하는 메서드입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "PAIR_2002", description = "현재 가장 인기 있는 페어 목록 반환을 완료했습니다.")
    })
    @Parameters({
            @Parameter(name = "size", description = "반환할 페어 수")
    })
    @GetMapping(value = "/popular/list")
    public ApiResponse<PopularPairListResDto> getPopularPairList (
            @RequestParam(name = "size") Integer size
    ){
        Pageable pageable = PageRequest.of(0, size);
        List<Pair> popularPairList =  pairService.findPopularPairList(pageable);

        return ApiResponse.onSuccess(SuccessCode.POPULAR_PAIRS_SUCCESS, pairConverter.popularPairListResDto(popularPairList));
    }
}
