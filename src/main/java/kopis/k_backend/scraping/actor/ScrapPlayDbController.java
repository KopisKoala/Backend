package kopis.k_backend.scraping.actor;

import kopis.k_backend.global.api_payload.ApiResponse;
import kopis.k_backend.global.api_payload.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/scrape/playdb")
public class ScrapPlayDbController {
    private final ScrapPlayDbService scrapPlayDbService;

    @PostMapping("/all")
    public ApiResponse<String> scrapeAndSaveActorsByOne() {
        List<Long> performanceIds = scrapPlayDbService.findPerformancesWithNonPlayDBCast();

        for(Long id : performanceIds){
            scrapPlayDbService.scrapeAndSaveActors(id);
        }
        return ApiResponse.onSuccess(SuccessCode.SCRAP_PERF_PUT_ACTOR_SUCCESS, "PLAYDB의 공연 배우 정보를 DB에 넣었습니다.");
    }

    @PostMapping("/one-by-one")
    public ApiResponse<String> scrapeAndSaveActorsByOnce(@RequestParam("perfId") Long performanceId) {
        scrapPlayDbService.scrapeAndSaveActors(performanceId);
        return ApiResponse.onSuccess(SuccessCode.SCRAP_PERF_PUT_ACTOR_SUCCESS, "PLAYDB의 공연 배우 정보를 DB에 넣었습니다.");
    }

}
