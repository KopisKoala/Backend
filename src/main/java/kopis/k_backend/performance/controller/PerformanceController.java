package kopis.k_backend.performance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kopis.k_backend.global.api_payload.ApiResponse;
import kopis.k_backend.global.api_payload.SuccessCode;
import kopis.k_backend.performance.converter.PerformanceConverter;
import kopis.k_backend.performance.domain.PerformanceAdvertise;
import kopis.k_backend.performance.domain.PerformanceAttract;
import kopis.k_backend.performance.domain.PerformancePopularMusical;
import kopis.k_backend.performance.domain.PerformancePopularPlay;
import kopis.k_backend.performance.dto.PerformanceResponseDto;
import kopis.k_backend.performance.service.PerformanceRankingService;
import kopis.k_backend.performance.service.PerformanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Tag(name = "공연", description = "공연 관련 api 입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/performance")
public class PerformanceController {
    private final PerformanceService performanceService;
    private final PerformanceRankingService performanceRankingService;

    @Operation(summary = "뮤지컬 인기 순위", description = "뮤지컬 인기 순위를 반환하는 api입니다. ")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POPULAR_MUSICAL_LIST_2001", description = "뮤지컬 인기 순위를 반환헀습니다."),
    })
    @GetMapping("/popular/musicals")
    public ApiResponse<PerformanceResponseDto.RankPerformanceListDto> popularMusicals() {
        // 일단은 날짜 픽스. 후에 셀레니움 사용해서 순위 데이터 끌어와서 데이터 업데이트하고 해당 날짜로 가져올 것임
        List<PerformancePopularMusical> musicals = performanceRankingService.popularMusicalList("2024.08.18");

        PerformanceResponseDto.RankPerformanceListDto popularPerformanceListDto = PerformanceConverter.popularMusicalListDto(musicals);
        return ApiResponse.onSuccess(SuccessCode.POPULAR_MUSICAL_LIST_SUCCESS, popularPerformanceListDto);
    }

    @Operation(summary = "연극 인기 순위", description = "연극 인기 순위를 반환하는 api입니다. ")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POPULAR_PLAY_LIST_2002", description = "연극 인기 순위를 반환헀습니다."),
    })
    @GetMapping("/popular/plays")
    public ApiResponse<PerformanceResponseDto.RankPerformanceListDto> popularPlays() {
        // 일단은 날짜 픽스. 후에 셀레니움 사용해서 순위 데이터 끌어와서 데이터 업데이트하고 해당 날짜로 가져올 것임
        List<PerformancePopularPlay> plays = performanceRankingService.popularPlayList("2024.08.18");

        PerformanceResponseDto.RankPerformanceListDto popularPerformanceListDto = PerformanceConverter.popularPlayListDto(plays);
        return ApiResponse.onSuccess(SuccessCode.POPULAR_PLAY_LIST_SUCCESS, popularPerformanceListDto);
    }

    @Operation(summary = "광고 공연", description = "광고 공연 리스트를 반환하는 api입니다. ")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "ADVERTISE_PERFORMANCE_LIST_2003", description = "공연 추천 리스트를 반환헀습니다."),
    })
    @GetMapping("/advertise")
    public ApiResponse<PerformanceResponseDto.RankPerformanceListDto> advertisePerformances() {
        // 일단은 날짜 픽스. 후에 셀레니움 사용해서 순위 데이터 끌어와서 데이터 업데이트하고 해당 날짜로 가져올 것임
        List<PerformanceAdvertise> advertisements = performanceRankingService.advertisePerformanceList("2024.08.18");

        PerformanceResponseDto.RankPerformanceListDto advertisePerformanceListDto = PerformanceConverter.performanceAdvertiseListDto(advertisements);
        return ApiResponse.onSuccess(SuccessCode.ADVERTISE_PERFORMANCE_LIST_SUCCESS, advertisePerformanceListDto);
    }

    @Operation(summary = "이런 공연은 어때요 순위", description = "공연 추천 리스트를 반환하는 api입니다. ")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "ATTRACT_PERFORMANCE_LIST_2004", description = "공연 추천 리스트를 반환헀습니다."),
    })
    @GetMapping("/attract")
    public ApiResponse<PerformanceResponseDto.RankPerformanceListDto> attractPerformances() {
        // 리뷰 적고(5개 이상) 별점 높은 공연 db에 넣기 -> 현재 스케줄러로 돌아가고 있음
        //performanceRankingService.extractAttractPerformance();

        // 오늘 날짜에 맞는 공연 가져오기
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        String formattedDate = today.format(formatter);
        List<PerformanceAttract> attracts = performanceRankingService.attractPerformanceList(formattedDate);

        PerformanceResponseDto.RankPerformanceListDto attractPerformanceListDto = PerformanceConverter.performanceAttractListDto(attracts);
        return ApiResponse.onSuccess(SuccessCode.POPULAR_PLAY_LIST_SUCCESS, attractPerformanceListDto);
    }

}
