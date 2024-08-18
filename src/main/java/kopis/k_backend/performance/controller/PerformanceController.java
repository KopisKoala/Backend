package kopis.k_backend.performance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kopis.k_backend.global.api_payload.ApiResponse;
import kopis.k_backend.global.api_payload.SuccessCode;
import kopis.k_backend.performance.converter.PerformanceConverter;
import kopis.k_backend.performance.domain.PerformancePopularMusical;
import kopis.k_backend.performance.domain.PerformancePopularPlay;
import kopis.k_backend.performance.dto.PerformanceResponseDto;
import kopis.k_backend.performance.service.PerformancePopularService;
import kopis.k_backend.performance.service.PerformanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "공연", description = "공연 관련 api 입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/performance")
public class PerformanceController {
    private final PerformanceService performanceService;
    private final PerformancePopularService performancePopularService;

    @Operation(summary = "뮤지컬 인기 순위", description = "뮤지컬 인기 순위를 반환하는 api입니다. ")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POPULAR_MUSICAL_LIST_2001", description = "뮤지컬 인기 순위를 반환헀습니다."),
    })
    @GetMapping("/popular/musicals")
    public ApiResponse<PerformanceResponseDto.PopularPerformanceListDto> popularMusicals() {
        // 일단은 날짜 픽스. 후에 셀레니움 사용해서 순위 데이터 끌어와서 데이터 업데이트하고 해당 날짜로 가져올 것임
        List<PerformancePopularMusical> musicals = performancePopularService.popularMusicalList("2024.08.18");

        PerformanceResponseDto.PopularPerformanceListDto popularPerformanceListDto = PerformanceConverter.popularMusicalListDto(musicals);
        return ApiResponse.onSuccess(SuccessCode.POPULAR_MUSICAL_LIST_SUCCESS, popularPerformanceListDto);
    }

    @Operation(summary = "연극 인기 순위", description = "연극 인기 순위를 반환하는 api입니다. ")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POPULAR_PLAY_LIST_2002", description = "연극 인기 순위를 반환헀습니다."),
    })
    @GetMapping("/popular/plays")
    public ApiResponse<PerformanceResponseDto.PopularPerformanceListDto> popularPlays() {
        // 일단은 날짜 픽스. 후에 셀레니움 사용해서 순위 데이터 끌어와서 데이터 업데이트하고 해당 날짜로 가져올 것임
        List<PerformancePopularPlay> plays = performancePopularService.popularPlayList("2024.08.18");

        PerformanceResponseDto.PopularPerformanceListDto popularPerformanceListDto = PerformanceConverter.popularPlayListDto(plays);
        return ApiResponse.onSuccess(SuccessCode.POPULAR_PLAY_LIST_SUCCESS, popularPerformanceListDto);
    }

}
