package kopis.k_backend.search.converter;

import kopis.k_backend.pair.converter.PairConverter;
import kopis.k_backend.pair.dto.PairResponseDto.PairDetailListResDto;
import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.performance.dto.ActorResponseDto.HomeSearchActorListResDto;
import kopis.k_backend.performance.dto.PerformanceResponseDto.HomeSearchPerformanceListResDto;
import kopis.k_backend.search.dto.SearchResponseDto.PairSearchResDto;
import kopis.k_backend.search.dto.SearchResponseDto.HomeSearchResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SearchConverter {
    private final PairConverter pairConverter;

    public static HomeSearchResDto homeSearchResDto(HomeSearchPerformanceListResDto homeSearchPerformanceListResDto, HomeSearchActorListResDto homeSearchActorListResDto) {
        return HomeSearchResDto.builder()
                .performances(homeSearchPerformanceListResDto)
                .actors(homeSearchActorListResDto)
                .build();
    }

    public PairSearchResDto pairSearchResDto(List<Performance> performanceList) {

        // 람다 함수 안에서 값을 수정할 수 있는 객체
        AtomicLong totalPairCount = new AtomicLong(0L);

        List<PairDetailListResDto> pairDetailListResDtos = performanceList.stream()
                .filter(performance -> !performance.getPairs().isEmpty())
                .map(performance -> {
                    PairDetailListResDto pairDetailListResDto = pairConverter.pairDetailListResDto(performance.getPairs());
                    totalPairCount.addAndGet(pairDetailListResDto.getPairDetailResDtoList().size());
                    return pairDetailListResDto;
                })
                .collect(Collectors.toList());

        return PairSearchResDto.builder()
                .totalPairCount(totalPairCount.get())
                .pairDetailListResDtos(pairDetailListResDtos)
                .build();
    }
}
