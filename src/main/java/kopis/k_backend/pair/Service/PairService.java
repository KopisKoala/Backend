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

    public void data(){
        Performance p = performanceRepository.findById(1L)
                .orElseThrow(() ->GeneralException.of(ErrorCode.PERFORMANCE_NOT_FOUND));

        Pair pair = Pair.builder()
                .performance(p)
                .actor1Name("이동훈")
                .actor2Name("박상신")
                .hashtag1("잘생겼다")
                .hashtag2("경이롭다")
                .hashtag3("짜릿하다")
                .ratingAverage(4.5)
                .reviewCount(10L)
                .build();

        pairRepository.save(pair);
    }

}
