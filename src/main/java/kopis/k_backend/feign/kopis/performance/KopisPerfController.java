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

@Tag(name = "DB에 데이터 삽입", description = "공연 관련 정보를 db에 넣는 api입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/db/performance")
public class KopisPerfController {
    private final KopisPerfServiceLocal kopisPerfServiceLocal;

    @Operation(summary = "공연", description = "공연 상태를 업데이트하는 api입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DB_PERF_2013", description = "공연 상태를 업데이트하는 api입니다."),
    })
    @GetMapping("/state")
    public ApiResponse<Boolean> updatePerfState(){
        kopisPerfServiceLocal.updatePerfStateLocal();
        return ApiResponse.onSuccess(SuccessCode.DB_PERF_STATE_UPDATE_SUCCESS, true);
    }

    @Operation(summary = "공연 목록", description = "공연들을 db에 넣는 api입니다. ")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DB_PERF_2014", description = "공연이 DB에 저장되었습니다."),
    })
    @GetMapping("/list")
    public ApiResponse<Boolean> putPerfs() {
        kopisPerfServiceLocal.putPerfListLocal();
        return ApiResponse.onSuccess(SuccessCode.DB_PERF_LIST_SUCCESS, true);
    }

    @Operation(summary = "공연 목록", description = "공연장을 가지고 공연들을 db에 넣는 api입니다. ")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DB_PERF_2014", description = "공연이 DB에 저장되었습니다."),
    })
    @GetMapping("/listByHall")
    public ApiResponse<Boolean> putPerfs(
            @RequestParam("hall-num") Integer hallNum
    ) {
        kopisPerfServiceLocal.putPerfListForAllGenresAndHalls(hallNum);
        return ApiResponse.onSuccess(SuccessCode.DB_PERF_LIST_SUCCESS, true);
    }
}
