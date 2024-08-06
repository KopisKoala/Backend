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
import java.util.Arrays;
import java.util.List;

//공연장 정보는 전체를 미리 db에 넣어두어야 함. 넣기 쉽게 controller 작성
@Tag(name = "DB에 데이터 삽입", description = "공연장 관련 정보를 db에 넣는 api입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/db/hall")
public class KopisHallController {
    private final KopisHallService kopisHallService;

    @Operation(summary = "공연장", description = "공연장을 db에 넣는 api입니다. ")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "HALL_2012", description = "공연장이 DB에 저장되었습니다."),
    })
    @GetMapping("/detail")
    public ApiResponse<Boolean> putHall() {

        List<String> hallIds = kopisHallService.getAllHallId();
        for (String hallId : hallIds) {
            kopisHallService.putHallDetail(hallId);
        }
        return ApiResponse.onSuccess(SuccessCode.DB_HALL_DETAIL_SUCCESS, true);
    }

    @Operation(summary = "공연장 목록", description = "공연장 목록을 db에 넣는 api입니다. ")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "HALL_2011", description = "공연장 목록이 DB에 저장되었습니다."),
    })
    @GetMapping("/list")
    public ApiResponse<Boolean> putHallList() {

        List<String> concertHalls = Arrays.asList(
                "예술의전당",
                "콘서트홀",
                "아트센터",
                "블루스퀘어",
                "충무아트센터",
                "백주년기념관",
                "코엑스아티움",
                "평화의전당",
                "체육관",
                "라이브홀",
                "에스플렉스센터",
                "올림픽홀",
                "코엑스",
                "실내체육관",
                "아트센터",
                "아트리움",
                "문화예술의전당",
                "아람누리",
                "문화예술회관",
                "오페라하우스",
                "문화회관",
                "시민회관",
                "문화예술회관",
                "세계문화엑스포공원",
                "아트홀",
                "문예회관"
        );

        int idx = 0;
        while(idx < concertHalls.size()){
            String hallName = concertHalls.get(idx++);
            kopisHallService.putHallList(hallName);
        }

        return ApiResponse.onSuccess(SuccessCode.DB_HALL_LIST_SUCCESS, true);

    }

}
