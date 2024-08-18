package kopis.k_backend.performance.service;

import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.performance.domain.PerformancePopularMusical;
import kopis.k_backend.performance.domain.PerformancePopularPlay;
import kopis.k_backend.performance.repository.PerformancePopularMusicalRepository;
import kopis.k_backend.performance.repository.PerformancePopularPlayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PerformancePopularService {
    private final PerformancePopularMusicalRepository performancePopularMusicalRepository;
    private final PerformancePopularPlayRepository performancePopularPlayRepository;

    public List<PerformancePopularMusical> popularMusicalList(String date){
        return performancePopularMusicalRepository.findAllByDate(date);
    }

    public List<PerformancePopularPlay> popularPlayList(String date){
        return performancePopularPlayRepository.findAllByDate(date);
    }

}
