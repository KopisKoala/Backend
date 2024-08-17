package kopis.k_backend.performance.service;

import kopis.k_backend.global.api_payload.ErrorCode;
import kopis.k_backend.global.exception.GeneralException;
import kopis.k_backend.pair.repository.PairRepository;
import kopis.k_backend.performance.domain.*;
import kopis.k_backend.performance.repository.ActorRepository;
import kopis.k_backend.performance.repository.HallRepository;
import kopis.k_backend.performance.repository.PerformanceActorRepository;
import kopis.k_backend.performance.repository.PerformanceRepository;
import kopis.k_backend.review.domain.Review;
import kopis.k_backend.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PerformanceService {
    private final PerformanceRepository performanceRepository;;
    private final ReviewRepository reviewRepository;

    public Performance findById(Long id) {
        return performanceRepository.findById(id)
                .orElseThrow(() -> GeneralException.of(ErrorCode.PERFORMANCE_NOT_FOUND));
    }

    public Long getReviewCountById(Long id) {
        Performance perf = performanceRepository.findById(id)
                .orElseThrow(() -> GeneralException.of(ErrorCode.PERFORMANCE_NOT_FOUND));

        return perf.getReviewCount();
    }

    public Double getAverageRatingById(Long id) {
        Performance perf = performanceRepository.findById(id)
                .orElseThrow(() -> GeneralException.of(ErrorCode.PERFORMANCE_NOT_FOUND));

        return perf.getRatingAverage();
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul") // 하루 간격으로 실행
    public void updateTopHashtags() {
        System.out.println("updatePerfTopHashtags started");

        List<Performance> performances = performanceRepository.findAll();
        for (Performance performance : performances) { // 공연 하나씩 돌며 업데이트
            List<Review> reviews = reviewRepository.findByPerformance(performance);
            Map<String, Long> hashtagFrequency = new HashMap<>();

            for (Review review : reviews) {
                String hashtag = review.getHashtag();
                if (hashtag != null && !hashtag.isEmpty()) {
                    hashtagFrequency.put(hashtag, hashtagFrequency.getOrDefault(hashtag, 0L) + 1);
                }
            }

            // 가장 빈도가 높은 해시태그 3개를 찾기
            List<String> topHashtags = hashtagFrequency.entrySet()
                    .stream()
                    .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                    .limit(3)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            performance.updateTopHashtags(topHashtags); // 해당 해시태그를 공연 엔티티에 저장
            performanceRepository.save(performance); // 위 메소드에서 엔티티 변경 후 트랜잭션이 끝날 때 변경사항이 DB에 반영되어 save 생략해도 됨
        }
        System.out.println("updatePerfTopHashtags finished");
    }
}
