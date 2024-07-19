package kopis.k_backend.review.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import kopis.k_backend.review.service.ReviewLikeService;
import kopis.k_backend.user.domain.User;
import kopis.k_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "리뷰 좋아요", description = "게시물 좋아요 관련 api 입니다.")
@RestController
@RequestMapping("/review/{review-id}/like")
@RequiredArgsConstructor
public class ReviewLikeController {
    private final UserService userService;
    private final ReviewLikeService reviewLikeService;
}
