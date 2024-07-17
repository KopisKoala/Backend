package kopis.k_backend.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kopis.k_backend.global.api_payload.ApiResponse;
import kopis.k_backend.pair.domain.Pair;
import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.performance.service.PerformanceService;
import kopis.k_backend.review.domain.Review;
import kopis.k_backend.review.dto.ReviewRequestDto.ReviewReqDto;
import kopis.k_backend.global.api_payload.*;
import kopis.k_backend.review.service.ReviewService;
import kopis.k_backend.user.jwt.CustomUserDetails;
import kopis.k_backend.user.service.UserService;
import kopis.k_backend.pair.Service.PairService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import kopis.k_backend.user.domain.User;
import java.io.IOException;

@Tag(name = "리뷰", description = "리뷰 관련 api 입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final UserService userService;
    private final ReviewService reviewService;
    private final PerformanceService performanceService;
    private final PairService pairService;

    @Operation(summary = "리뷰 만들기 메서드", description = "리뷰를 만드는 메서드입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REVIEW_2011", description = "리뷰 생성이 완료되었습니다.")
    })
    @PostMapping(value = "/create")
    public ApiResponse<Long> create(
            // 받는 형식과 받은 결과
            @RequestBody ReviewReqDto reviewReqDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) throws IOException {
        try {
            // user Entity를 가져옴
            User user = userService.findByUserName(customUserDetails.getUsername());

            // performance Entity를 가져옴
            Performance performance = performanceService.findById(reviewReqDto.getPerformanceId());

            // pair Entity를 가져옴
            Pair pair = pairService.findById(reviewReqDto.getPairId());

            Review review = reviewService.create(reviewReqDto, user, performance, pair);

            return ApiResponse.onSuccess(SuccessCode.REVIEW_CREATED, review.getId());

        } catch (Exception e) {
            // 예외 발생 시 응답 내용 로그
            log.error("Error during review creation", e);
            throw e;
        }
    }
}
