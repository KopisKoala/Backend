package kopis.k_backend.performance.service;

import jakarta.annotation.PostConstruct;
import kopis.k_backend.global.api_payload.ErrorCode;
import kopis.k_backend.global.exception.GeneralException;
import kopis.k_backend.pair.domain.Pair;
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
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PerformanceService {
    private final PerformanceRepository performanceRepository;
    private final PairRepository pairRepository;
    private final PerformanceActorRepository performanceActorRepository;
    private final ActorRepository actorRepository;
    private final ReviewRepository reviewRepository;
    private final HallRepository hallRepository;

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

    @Scheduled(cron = "0 0 * * * *", zone = "Asia/Seoul") // 1시간 간격으로 실행
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

    /*@PostConstruct // 실행시킬 때마다 DB에 예시 데이터 들어감. 본격적으로 DB에 데이터 넣기 전까지 사용할 예정.
    public void data() {
        System.out.println("Starting data initialization");
        try {
            // hall 객체 생성
            Hall hall = Hall.builder()
                    .sidonm("서울")
                    .gugunnm("서울")
                    .streetAddress("서울시 중구 퇴계로 387")
                    .hallName("충무아트센터 대극장")
                    .build();
            hallRepository.save(hall);

            // performance 객체 생성
            Performance p1 = Performance.builder()
                    .title("데스노트")
                    .performanceType(PerformanceType.PLAY)
                    .hall(hall)
                    .startDate(LocalDate.of(2023, 7, 1))
                    .endDate(LocalDate.of(2023, 7, 31))
                    .duration(160)
                    .lowestPrice(10000)
                    .highestPrice(150000)
                    .poster("포스터.img")
                    .ratingAverage(0.0)
                    .reviewCount(0L)
                    .ticketingLink("http://example.com/tickets")
                    .build();
            performanceRepository.save(p1);

            // pair 객체 생성
            Pair pair1 = Pair.builder()
                    .performance(p1)
                    .actor1Name("이동훈")
                    .actor2Name("박상신")
                    .ratingAverage(0.0)
                    .reviewCount(0L)
                    .build();
            pairRepository.save(pair1);

            Pair pair2 = Pair.builder()
                    .performance(p1)
                    .actor1Name("이동훈")
                    .actor2Name("이은석")
                    .ratingAverage(0.0)
                    .reviewCount(0L)
                    .build();
            pairRepository.save(pair2);

            // actor 객체 생성
            Actor a1 = Actor.builder()
                    .actorName("이동훈")
                    .actorProfile("이동훈.img")
                    .build();
            actorRepository.save(a1);

            Actor a2 = Actor.builder()
                    .actorName("박상신")
                    .actorProfile("박상신.img")
                    .build();
            actorRepository.save(a2);

            Actor a22 = Actor.builder()
                    .actorName("이은석")
                    .actorProfile("이은석.img")
                    .build();
            actorRepository.save(a22);

            // perf_actor 객체 생성
            PerformanceActor pa1 = PerformanceActor.builder()
                    .performance(p1)
                    .actor(a1)
                    .characterName("엘")
                    .build();
            performanceActorRepository.save(pa1);

            PerformanceActor pa2 = PerformanceActor.builder()
                    .performance(p1)
                    .actor(a2)
                    .characterName("라이토")
                    .build();
            performanceActorRepository.save(pa2);

            PerformanceActor pa22 = PerformanceActor.builder()
                    .performance(p1)
                    .actor(a22)
                    .characterName("라이토")
                    .build();
            performanceActorRepository.save(pa22);

            // ------------------------------------------------------------------
            // performance 객체 생성
            Performance p2 = Performance.builder()
                    .title("시카고")
                    .performanceType(PerformanceType.PLAY)
                    .hall(hall)
                    .startDate(LocalDate.of(2023, 7, 1))
                    .endDate(LocalDate.of(2023, 7, 31))
                    .duration(160)
                    .lowestPrice(10000)
                    .highestPrice(150000)
                    .poster("포스터.img")
                    .ratingAverage(0.0)
                    .reviewCount(0L)
                    .ticketingLink("http://example.com/tickets")
                    .build();
            performanceRepository.save(p2);

            // pair 객체 생성
            Pair pair13 = Pair.builder()
                    .performance(p2)
                    .actor1Name("이동훈1")
                    .actor2Name("박상신1")
                    .ratingAverage(0.0)
                    .reviewCount(0L)
                    .build();
            pairRepository.save(pair13);

            Pair pair23 = Pair.builder()
                    .performance(p2)
                    .actor1Name("이동훈1")
                    .actor2Name("이은석1")
                    .ratingAverage(0.0)
                    .reviewCount(0L)
                    .build();
            pairRepository.save(pair23);

            // actor 객체 생성
            Actor a13 = Actor.builder()
                    .actorName("이동훈1")
                    .actorProfile("이동훈1.img")
                    .build();
            actorRepository.save(a13);

            Actor a23 = Actor.builder()
                    .actorName("박상신1")
                    .actorProfile("박상신1.img")
                    .build();
            actorRepository.save(a23);

            Actor a223 = Actor.builder()
                    .actorName("이은석1")
                    .actorProfile("이은석1.img")
                    .build();
            actorRepository.save(a223);

            // perf_actor 객체 생성
            PerformanceActor pa13 = PerformanceActor.builder()
                    .performance(p2)
                    .actor(a13)
                    .characterName("배역1")
                    .build();
            performanceActorRepository.save(pa13);

            PerformanceActor pa23 = PerformanceActor.builder()
                    .performance(p2)
                    .actor(a23)
                    .characterName("배역2")
                    .build();
            performanceActorRepository.save(pa23);

            PerformanceActor pa223 = PerformanceActor.builder()
                    .performance(p2)
                    .actor(a223)
                    .characterName("배역2")
                    .build();
            performanceActorRepository.save(pa223);

        } catch (Exception e) {
            System.out.println("Error initializing data: " + e.getMessage());
            e.printStackTrace();
        }
    }*/
}
