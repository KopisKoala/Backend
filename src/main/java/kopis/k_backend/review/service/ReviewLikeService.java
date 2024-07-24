package kopis.k_backend.review.service;

import kopis.k_backend.global.exception.GeneralException;
import kopis.k_backend.review.domain.Review;
import kopis.k_backend.review.domain.ReviewLike;
import kopis.k_backend.review.repository.ReviewLikeRepository;
import kopis.k_backend.review.repository.ReviewRepository;
import kopis.k_backend.user.domain.User;
import kopis.k_backend.global.api_payload.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewLikeService {
    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;

    @Transactional
    public Long toggleLikeAndRetrieveCount(Long reviewId, User user) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> GeneralException.of(ErrorCode.REVIEW_NOT_FOUND));

        ReviewLike existingLike = reviewLikeRepository.findByUserAndReview(user, review).orElse(null);;

        if (existingLike != null) {
            throw new GeneralException(ErrorCode.REVIEW_ALREADY_LIKED);
        } else {
            // 좋아요를 누르지 않았다면, 좋아요 누르기
            reviewLikeRepository.save(ReviewLike.builder().user(user).review(review).build());
            // 좋아요 수 업데이트
            return review.increaseLikeCount();
        }
    }

    @Transactional
    public Long cancelLikeAndRetrieveCount(Long reviewId, User user) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> GeneralException.of(ErrorCode.REVIEW_NOT_FOUND));

        ReviewLike existingLike = reviewLikeRepository.findByUserAndReview(user, review)
                .orElse(null);

        if (existingLike != null) {
            // 좋아요를 눌렀었다면 좋아요 취소
            reviewLikeRepository.delete(existingLike);
            // 좋아요 수 업데이트
            return review.decreaseLikeCount();

        } else {
            throw new GeneralException(ErrorCode.REVIEW_LIKED_NOT_FOUND);
        }
    }

}
