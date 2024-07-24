package kopis.k_backend.pair.Service;

import kopis.k_backend.global.api_payload.ErrorCode;
import kopis.k_backend.global.exception.GeneralException;
import kopis.k_backend.pair.domain.Pair;
import kopis.k_backend.pair.repository.PairRepository;
import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.performance.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PairService {

    private final PairRepository pairRepository;
    private final PerformanceRepository performanceRepository;

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

}
