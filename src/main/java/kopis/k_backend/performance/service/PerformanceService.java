package kopis.k_backend.performance.service;

import jakarta.annotation.PostConstruct;
import kopis.k_backend.global.api_payload.ErrorCode;
import kopis.k_backend.global.exception.GeneralException;
import kopis.k_backend.pair.domain.Pair;
import kopis.k_backend.pair.repository.PairRepository;
import kopis.k_backend.performance.domain.Actor;
import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.performance.domain.PerformanceActor;
import kopis.k_backend.performance.domain.PerformanceType;
import kopis.k_backend.performance.repository.ActorRepository;
import kopis.k_backend.performance.repository.PerformanceActorRepository;
import kopis.k_backend.performance.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final PairRepository pairRepository;
    private final PerformanceActorRepository performanceActorRepository;
    private final ActorRepository actorRepository;

    public Performance findById(Long id) {
        return performanceRepository.findById(id)
                .orElseThrow(() ->GeneralException.of(ErrorCode.PERFORMANCE_NOT_FOUND));
    }

    public Long getReviewCountById(Long id){
        Performance perf =  performanceRepository.findById(id)
                .orElseThrow(() ->GeneralException.of(ErrorCode.PERFORMANCE_NOT_FOUND));

        return perf.getReviewCount();
    }


    // 실행시킬 때마다 db에 예시 데이터 들어감. 본격적으로 db에 데이터 넣기 전까지 사용할 예정.
    @PostConstruct
    public void data() {
        // performance 객체 생성
        Performance performance = Performance.builder()
                .title("데스노트")
                .performanceType(PerformanceType.PLAY)
                .hashtag1("짜릿하다")
                .hashtag2("소름끼친다")
                .hashtag3("심장멎는줄")
                .district("서울")
                .streetAddress("서울시 중구 퇴계로 387")
                .hallName("충무아트센터 대극장")
                .startDate(LocalDate.of(2023, 7, 1))
                .endDate(LocalDate.of(2023, 7, 31))
                .Duration(160)
                .lowestPrice(10000)
                .highestPrice(150000)
                .poster("포스터.img")
                .ratingAverage(0.0)
                .reviewCount(0L)
                .ticketingLink("http://example.com/tickets")
                .build();
        performanceRepository.save(performance);

        // pair 객체 생성
        Performance p = performanceRepository.findById(1L)
                .orElseThrow(() ->GeneralException.of(ErrorCode.PERFORMANCE_NOT_FOUND));

        Pair pair = Pair.builder()
                .performance(p)
                .actor1Name("이동훈")
                .actor2Name("박상신")
                .hashtag1("잘생겼다")
                .hashtag2("경이롭다")
                .hashtag3("짜릿하다")
                .ratingAverage(0.0)
                .reviewCount(0L)
                .build();
        pairRepository.save(pair);

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

        // perf_actor 객체 생성
        PerformanceActor pa1 = PerformanceActor.builder()
                .performance(p)
                .actor(a1)
                .characterName("엘")
                .build();
        performanceActorRepository.save(pa1);
        PerformanceActor pa2 = PerformanceActor.builder()
                .performance(p)
                .actor(a2)
                .characterName("라이토")
                .build();
        performanceActorRepository.save(pa2);


    }

}
