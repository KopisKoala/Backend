package kopis.k_backend.performance.service;

import jakarta.annotation.PostConstruct;
import kopis.k_backend.global.api_payload.ErrorCode;
import kopis.k_backend.global.exception.GeneralException;
import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.performance.domain.PerformanceType;
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

    public Performance findById(Long id) {
        return performanceRepository.findById(id)
                .orElseThrow(() ->GeneralException.of(ErrorCode.PERFORMANCE_NOT_FOUND));
    }

    public Long getReviewCountById(Long id){
        Performance perf =  performanceRepository.findById(id)
                .orElseThrow(() ->GeneralException.of(ErrorCode.PERFORMANCE_NOT_FOUND));

        return perf.getReviewCount();
    }

    @PostConstruct
    public void data() {
        // Performance 객체 생성
        Performance performance = Performance.builder()
                .title("Amazing Performance")
                .performanceType(PerformanceType.PLAY)
                .hashtag1("신난다")
                .hashtag2("소름끼친다")
                .hashtag3("심장멎는줄")
                .district("Seoul")
                .streetAddress("123 Performance St.")
                .hallName("Main Hall")
                .startDate(LocalDate.of(2023, 7, 1))
                .endDate(LocalDate.of(2023, 7, 31))
                .Duration(120)
                .lowestPrice(5000)
                .highestPrice(200000)
                .poster("poster.jpg")
                .ratingAverage(4.5)
                .reviewCount(10L)
                .ticketingLink("http://example.com/tickets")
                .build();

        performanceRepository.save(performance);
    }

}
