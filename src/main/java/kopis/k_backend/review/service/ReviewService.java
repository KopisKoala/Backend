package kopis.k_backend.review.service;

import jakarta.transaction.Transactional;
import kopis.k_backend.global.exception.GeneralException;
import kopis.k_backend.pair.domain.Pair;
import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.review.converter.ReviewConverter;
import kopis.k_backend.review.domain.Review;
import kopis.k_backend.review.domain.ReviewLike;
import kopis.k_backend.review.repository.ReviewLikeRepository;
import kopis.k_backend.review.repository.ReviewRepository;
import kopis.k_backend.global.api_payload.ErrorCode;
import kopis.k_backend.user.domain.User;
import kopis.k_backend.review.dto.ReviewRequestDto.ReviewReqDto;
import kopis.k_backend.performance.repository.PerformanceRepository;
import kopis.k_backend.pair.repository.PairRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final PerformanceRepository performanceRepository;
    private final PairRepository pairRepository;

    @Transactional
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Transactional
    public Review findById(Long id){
        return reviewRepository.findById(id)
                .orElseThrow(() -> GeneralException.of(ErrorCode.REVIEW_NOT_FOUND));
    }

    @Transactional
    public List<Review> findByUser(User user){
        return user.getReviews();
    }

    @Transactional
    public Review create(ReviewReqDto reviewReqDto, User user, Performance performance, Pair pair) throws IOException {

        Review review = ReviewConverter.saveReview(reviewReqDto, user, performance, pair);

        reviewRepository.save(review);

        return review;
    }

    @Transactional
    public void delete(Long id, User user) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> GeneralException.of(ErrorCode.REVIEW_NOT_FOUND));
        log.info("ReviewWriter: " + review.getWriter());
        log.info("Nickname: " + user.getNickname());

        if(Objects.equals(review.getWriter(), user.getNickname())) {
            // 리뷰 삭제
            reviewRepository.deleteById(id);

            // 좋아요 삭제
            List<ReviewLike> reviewLikes = review.getReviewLikes();
            reviewLikeRepository.deleteAll(reviewLikes);
        }
        else throw new GeneralException(ErrorCode.BAD_REQUEST);
    }

    public List<Review> getPerformanceReviewList(Long performanceId, String way, Integer scrollPosition, Integer fetchSize) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> GeneralException.of(ErrorCode.PERFORMANCE_NOT_FOUND));
        Slice<Review> reviewSlice = reviewRepository.findReviewByPerformanceAndWay(performance, way, PageRequest.of(scrollPosition, fetchSize));
        return reviewSlice.getContent();
    }

    public List<Review> getPairReviewList(Long pairId, String way, Integer scrollPosition, Integer fetchSize) {
        Pair pair = pairRepository.findById(pairId)
                .orElseThrow(() -> GeneralException.of(ErrorCode.PAIR_NOT_FOUND));
        Slice<Review> reviewSlice = reviewRepository.findReviewByPairAndWay(pair, way, PageRequest.of(scrollPosition, fetchSize));
        return reviewSlice.getContent();
    }

}

