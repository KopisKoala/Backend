package kopis.k_backend.feign.kopis.hall;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kopis.k_backend.global.api_payload.ApiResponse;
import kopis.k_backend.global.api_payload.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//공연장 정보는 전체를 미리 db에 넣어두어야 함. 넣기 쉽게 controller 작성
@Tag(name = "DB에 데이터 삽입", description = "공연장 관련 정보를 db에 넣는 api입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/db/hall")
public class KopisHallController {
    private final KopisHallService kopisHallService;

    @Operation(summary = "공연장 목록", description = "공연장 목록을 db에 넣는 api입니다. ")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DB_HALL_2011", description = "공연장 목록이 DB에 저장되었습니다."),
    })
    @GetMapping("/list")
    public ApiResponse<Boolean> putHallList() {

        kopisHallService.putHallList();

        return ApiResponse.onSuccess(SuccessCode.DB_HALL_LIST_SUCCESS, true);

    }

}
