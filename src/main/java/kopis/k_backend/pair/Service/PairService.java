package kopis.k_backend.pair.Service;

import kopis.k_backend.global.api_payload.ErrorCode;
import kopis.k_backend.global.exception.GeneralException;
import kopis.k_backend.pair.domain.Pair;
import kopis.k_backend.pair.repository.PairRepository;
import kopis.k_backend.performance.service.PerformanceService;
import kopis.k_backend.review.domain.Review;
import kopis.k_backend.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PairService {
    private static final Logger logger = LoggerFactory.getLogger(PerformanceService.class);

    private final PairRepository pairRepository;
    private final ReviewRepository reviewRepository;

    public Pair findById(Long id) {
        return pairRepository.findById(id)
                .orElseThrow(() -> GeneralException.of(ErrorCode.PAIR_NOT_FOUND));
    }

    public Long getReviewCountById(Long id){
        Pair pair =  pairRepository.findById(id)
                .orElseThrow(() ->GeneralException.of(ErrorCode.PAIR_NOT_FOUND));

        return pair.getReviewCount();
    }

    public Double getAverageRatingById(Long id){
        Pair pair = pairRepository.findById(id)
                .orElseThrow(() ->GeneralException.of(ErrorCode.PAIR_NOT_FOUND));

        return pair.getRatingAverage();
    }

    @Scheduled(fixedRate = 600000, zone = "Asia/Seoul") // 10분 간격으로 실행
    public void updateTopHashtags() {
        logger.info("updatePairTopHashtags started");

        List<Pair> pairs = pairRepository.findAll();
        for (Pair pair : pairs) { // 페어 하나씩 돌며 업데이트
            List<Review> reviews = reviewRepository.findByPair(pair);
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

            pair.updateTopHashtags(topHashtags); // 해당 해시태그를 공연 엔티티에 저장
            pairRepository.save(pair); // 위 메소드 에서 엔티티 변경 후 트랜잭션이 끝날때 변경사항이 db에 반영되어 save 생략해도 됨
        }
        logger.info("updatePairTopHashtags finished");
    }

}
