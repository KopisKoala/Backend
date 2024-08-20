package kopis.k_backend.search.converter;

import kopis.k_backend.performance.dto.ActorResponseDto.HomeSearchActorListResDto;
import kopis.k_backend.performance.dto.PerformanceResponseDto.HomeSearchPerformanceListResDto;
import kopis.k_backend.search.dto.SearchResponseDto.SearchResDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SearchConverter {
    public static SearchResDto searchResDto(HomeSearchPerformanceListResDto homeSearchPerformanceListResDto, HomeSearchActorListResDto homeSearchActorListResDto) {
        return SearchResDto.builder()
                .performances(homeSearchPerformanceListResDto)
                .actors(homeSearchActorListResDto)
                .build();
    }
}
