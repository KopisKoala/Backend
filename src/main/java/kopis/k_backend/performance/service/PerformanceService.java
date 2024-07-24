package kopis.k_backend.performance.service;

import kopis.k_backend.global.api_payload.ErrorCode;
import kopis.k_backend.global.exception.GeneralException;
import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.performance.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

}
