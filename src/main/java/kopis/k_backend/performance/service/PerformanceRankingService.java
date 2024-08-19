package kopis.k_backend.performance.service;

import kopis.k_backend.performance.domain.*;
import kopis.k_backend.performance.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PerformanceRankingService {
    private final PerformanceRepository performanceRepository;
    private final PerformancePopularMusicalRepository performancePopularMusicalRepository;
    private final PerformancePopularPlayRepository performancePopularPlayRepository;
    private final PerformanceAdvertiseRepository performanceAdvertiseRepository;
    private final PerformanceAttractRepository performanceAttractRepository;

    @Scheduled(cron = "30 23 0 * * *", zone = "Asia/Seoul") // 매일 밤 11시 30분
    public void extractAttractPerformance() {
        LocalDate today = LocalDate.now().plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        String formattedDate = today.format(formatter);  // 내일 날짜를 "yyyy.MM.dd" 형식으로 변환

        List<Performance> performances = performanceRepository.findTop10ByMinReviewsOrderByRatingAndReviewCount(5L);

        for (int i = 0; i < performances.size(); i++) {
            Performance performance = performances.get(i);

            // PerformanceAttract 엔티티 생성 및 저장
            PerformanceAttract performanceAttract = PerformanceAttract.builder()
                    .ranking(i + 1)
                    .date(formattedDate)
                    .performance(performance)
                    .build();
            performanceAttractRepository.save(performanceAttract);
        }
    }

    public List<PerformancePopularMusical> popularMusicalList(String date){
        return performancePopularMusicalRepository.findAllByDate(date);
    }

    public List<PerformancePopularPlay> popularPlayList(String date){
        return performancePopularPlayRepository.findAllByDate(date);
    }

    public List<PerformanceAdvertise> advertisePerformanceList(String date){
        return performanceAdvertiseRepository.findAllByDate(date);
    }

    public List<PerformanceAttract> attractPerformanceList(String date){
        return performanceAttractRepository.findAllByDate(date);
    }


}
