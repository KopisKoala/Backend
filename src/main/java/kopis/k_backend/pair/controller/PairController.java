package kopis.k_backend.pair.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kopis.k_backend.global.api_payload.ApiResponse;
import kopis.k_backend.global.api_payload.SuccessCode;
import kopis.k_backend.pair.Service.PairService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "페어", description = "페어 관련 api 입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/pair")
public class PairController {

    private final PairService pairService;

    @Operation(summary = "DB에 예시 데이터 넣기", description = "DB에 페어 예시 데이터 넣는 메소드입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REVIEW_2001", description = "리뷰 삭제가 완료되었습니다.")
    })
    @DeleteMapping(value = "/data")
    public ApiResponse<Boolean> delete(){
        pairService.data();
        return ApiResponse.onSuccess(SuccessCode.REVIEW_DELETED, true);
    }

}
