package kopis.k_backend.search.converter;

import kopis.k_backend.performance.dto.ActorResponseDto.ActorListResDto;
import kopis.k_backend.performance.dto.PerformanceResponseDto.PerformanceListResDto;
import kopis.k_backend.search.dto.SearchResponseDto.SearchResDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SearchConverter {
    public static SearchResDto searchResDto(PerformanceListResDto performanceResDto, ActorListResDto actorResDto) {
        return SearchResDto.builder()
                .performances(performanceResDto)
                .actors(actorResDto)
                .build();
    }
}
