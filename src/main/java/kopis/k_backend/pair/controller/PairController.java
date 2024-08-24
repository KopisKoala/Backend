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
import kopis.k_backend.pair.dto.PairResponseDto.RecommendPairListDto;
import kopis.k_backend.pair.dto.PairResponseDto.RecommendPairResDto;
import kopis.k_backend.pair.dto.PairResponseDto.PopularPairListResDto;
import kopis.k_backend.pair.dto.PairResponseDto.PairListResDto;
import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.performance.service.PerformanceService;
import kopis.k_backend.user.domain.User;
import kopis.k_backend.user.jwt.CustomUserDetails;
import kopis.k_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    private final UserService userService;

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

    @Operation(summary = "페어 추천", description = "추천하는 페어 목록을 반환하는 메서드입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "PAIR_2003", description = "추천하는 페어 목록을 반환했습니다.")
    })
    @Parameters({
            @Parameter(name = "performanceId", description = "공연 id")
    })
    @GetMapping("/recommend")
    public ApiResponse<RecommendPairListDto> getRecommendPair(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(name = "performanceId") Long performanceId
    ) {
        User user = userService.findByUserName(customUserDetails.getUsername());
        Performance performance = performanceService.findById(performanceId);
        List<Pair> pairList = pairService.findPairsByPerformance(performance);

        List<Pair> recommendPairList = new ArrayList<>();

        // 1. 가장 별점이 높고 리뷰가 많은 페어
        Pair topRatedPair = pairService.getTopRatedPair(pairList);

        // 2. 사용자가 찜한 배우가 있는 페어
        Pair favoritePair = pairService.getFavoritePair(pairList, user);

        // 3. 이전에 봤던 배우가 속한 페어
        Pair preActorPair = pairService.getPreActorPair(pairList, user);

        // 4. 이전에 작성했던 해시 태그를 갖고 있는 페어
        Pair preHashtagPair = pairService.getPreHashtagPair(pairList, user);

        return ApiResponse.onSuccess(SuccessCode.PAIR_RECOMMEND_SUCCESS, pairConverter.recommendPairListDto(topRatedPair, favoritePair, preActorPair, preHashtagPair));
    }
}
