package kopis.k_backend.feign.kopis.performance;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kopis.k_backend.global.api_payload.ApiResponse;
import kopis.k_backend.global.api_payload.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "DB에 데이터 삽입", description = "공연 관련 정보를 db에 넣는 api입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/db/performance")
public class KopisPerfController {
    private final KopisPerfService kopisPerfService;

    @Operation(summary = "공연 목록", description = "공연들을 db에 넣는 api입니다. ")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DB_PERF_2013", description = "공연이 DB에 저장되었습니다."),
    })
    @GetMapping("/list/{hall-num}")
    public ApiResponse<Boolean> putPerfs(@RequestParam("hall-num") Integer hallNum) {
        kopisPerfService.putPerfListForAllGenresAndHalls(hallNum);
        return ApiResponse.onSuccess(SuccessCode.DB_PERF_LIST_SUCCESS, true);
    }

    @Operation(summary = "공연", description = "공연 상세 정보를 db에 넣는 api입니다. ")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DB_PERF_2014", description = "공연 상세 정보가 DB에 저장되었습니다."),
    })
    @GetMapping("/detail")
    public ApiResponse<Boolean> putPerf() {
        List<String> perfIds = kopisPerfService.getAllPerfId();
        for (String perfId : perfIds) {
            kopisPerfService.putPerfDetail(perfId);
        }
        return ApiResponse.onSuccess(SuccessCode.DB_PERF_DETAIL_SUCCESS, true);
    }
}
