package kopis.k_backend.review.service;

import jakarta.transaction.Transactional;
import kopis.k_backend.global.exception.GeneralException;
import kopis.k_backend.pair.domain.Pair;
import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.review.converter.ReviewConverter;
import kopis.k_backend.review.domain.Review;
import kopis.k_backend.review.domain.ReviewLike;
import kopis.k_backend.review.domain.ViewingPartner;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
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
    public Review create(ReviewReqDto reviewReqDto, User user, Performance performance, Pair pair) throws IOException {

        Review review = ReviewConverter.saveReview(reviewReqDto, user, performance, pair);
        reviewRepository.save(review);

        // 리뷰 개수 증가
        pair.increaseReviewCount(pair.getId());
        performance.increaseReviewCount(performance.getId());

        // 평균 레이팅 수정
        Long PairRatingSum = getSumOfPairRatings(pair);
        pair.updateRatingAverage(PairRatingSum);

        Long PerformanceRatingSum = getSumOfPerformanceRatings(performance);
        performance.updateRatingAverage(PerformanceRatingSum);

        return review;
    }

    @Transactional
    public void delete(Long id, User user) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> GeneralException.of(ErrorCode.REVIEW_NOT_FOUND));

        if(Objects.equals(review.getWriter(), user.getUsername())) {
            // 리뷰 삭제
            reviewRepository.deleteById(id);

            // 좋아요 삭제
            List<ReviewLike> reviewLikes = review.getReviewLikes();
            reviewLikeRepository.deleteAll(reviewLikes);

            // 리뷰 개수 감소
            Pair pair = pairRepository.findById(review.getPair().getId())
                    .orElseThrow(() -> GeneralException.of(ErrorCode.PAIR_NOT_FOUND));
            Performance performance = performanceRepository.findById(review.getPerformance().getId())
                    .orElseThrow(() -> GeneralException.of(ErrorCode.PERFORMANCE_NOT_FOUND));

            pair.decreaseReviewCount(pair.getId());
            performance.decreaseReviewCount(performance.getId());

            // 평균 레이팅 수정
            Long PairRatingSum = getSumOfPairRatings(pair);
            pair.updateRatingAverage(PairRatingSum);

            Long PerformanceRatingSum = getSumOfPerformanceRatings(performance);
            performance.updateRatingAverage(PerformanceRatingSum);

        }
        else throw new GeneralException(ErrorCode.REVIEW_NOT_YOURS);
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

    public List<Review> getMonthReviewList(User user, LocalDate month) {
        // 해당 월의 시작 일
        LocalDate startOfMonth = month.withDayOfMonth(1).atStartOfDay().toLocalDate();

        // 해당 월의 마지막 일
        LocalDate endOfMonth = month.withDayOfMonth(month.lengthOfMonth()).atTime(23, 59, 59).toLocalDate();

        // 해당 월에 작성된 리뷰 조회
        return reviewRepository.findAllByWriterAndPerformanceDateBetween(user, startOfMonth, endOfMonth);
    }

    public Long getSumOfPairRatings(Pair pair) {
        return reviewRepository.sumPairRatingsByPair(pair);
    }

    public Long getSumOfPerformanceRatings(Performance performance) {
        return reviewRepository.sumPerformanceRatingsByPerformance(performance);
    }

    @Transactional
    public ViewingPartner updateViewingPartner(Review review, Integer partnerNumber) {
        ViewingPartner viewingPartner = review.updateViewingPartner(partnerNumber);
        reviewRepository.save(review);
        return viewingPartner;
    }

    @Transactional
    public String updateMemo(Review review, String memo) {
        String memoResult = review.updateMemo(memo);
        reviewRepository.save(review);
        return memoResult;
    }
}

