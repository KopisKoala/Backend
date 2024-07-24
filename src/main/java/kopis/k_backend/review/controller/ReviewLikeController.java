package kopis.k_backend.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kopis.k_backend.global.api_payload.ApiResponse;
import kopis.k_backend.review.service.ReviewLikeService;
import kopis.k_backend.user.domain.User;
import kopis.k_backend.user.jwt.CustomUserDetails;
import kopis.k_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import kopis.k_backend.global.api_payload.SuccessCode;


@Tag(name = "리뷰 좋아요", description = "게시물 좋아요 관련 api 입니다.")
@RestController
@RequestMapping("/review/{review-id}/like")
@RequiredArgsConstructor
public class ReviewLikeController {
    private final UserService userService;
    private final ReviewLikeService reviewLikeService;

    @Operation(summary = "게시물 좋아요 메서드", description = "게시물 좋아요하는 메서드입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "LIKE_2001", description = "게시물 좋아요 성공")
    })
    @PostMapping("/create")
    public ApiResponse<Long> toggleLike(
            @PathVariable(name = "review-id") Long reviewId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findByUserName(customUserDetails.getUsername());
        Long likeCount = reviewLikeService.toggleLikeAndRetrieveCount(reviewId, user);

        return ApiResponse.onSuccess(SuccessCode.REVIEW_LIKE_SUCCESS, likeCount);
    }

    @Operation(summary = "게시물 좋아요 취소 메서드", description = "게시물 좋아요를 취소하는 메서드입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "LIKE_2002", description = "게시물 좋아요 취소 성공")
    })
    @DeleteMapping("/delete")
    public ApiResponse<Long> cancelLike(@PathVariable(name = "review-id") Long reviewId,
                                        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findByUserName(customUserDetails.getUsername());
        Long likeCount = reviewLikeService.cancelLikeAndRetrieveCount(reviewId, user);

        return ApiResponse.onSuccess(SuccessCode.REVIEW_UNLIKE_SUCCESS, likeCount);
    }
}
