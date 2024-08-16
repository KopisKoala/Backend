package kopis.k_backend.feign.openai;

import io.jsonwebtoken.io.IOException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kopis.k_backend.global.api_payload.ApiResponse;
import kopis.k_backend.global.api_payload.SuccessCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@Tag(name = "Open Ai", description = "Open Ai 관련 api 입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/chat/chatGpt")
public class OpenAiController {

    private final OpenAiService openAiService;

    @Operation(summary = "GPT", description = "GPT API 요청 (model = gpt-3.5-turbo-instruct, prompt = gpt 요청 메세지. temperature = 0, max_token = 100)")
    @PostMapping("/prompt")
    public ApiResponse<Map<String, Object>> gptPrompt(
            @RequestBody CompletionDto completionDto
    ) throws IOException {
        try {
            Map<String, Object> result = openAiService.prompt(completionDto);
            return ApiResponse.onSuccess(SuccessCode.OPEN_AI_API_SUCCESS, result);

        } catch (Exception e) {
            log.error("Error during GPT API", e);
            throw e;
        }
    }

    @Operation(summary = "Performance Review Summary Test", description = "각 공연별 Review Summary를 진행합니다.")
    @GetMapping("/summary")
    public ApiResponse<Boolean> updatePerformanceReviewSummaryTest(
    ) throws IOException {
        try {
            openAiService.updatePerformanceReviewSummary();

            return ApiResponse.onSuccess(SuccessCode.OPEN_AI_PERFORMANCE_REVIEW_SUMMARY, true);
        } catch (Exception e) {
            log.error("Error during review summary", e);
            throw e;
        }
    }
}
