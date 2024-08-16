package kopis.k_backend.performance.converter;

import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.performance.dto.PerformanceResponseDto;
import kopis.k_backend.performance.dto.PerformanceResponseDto.PerformanceListResDto;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.stream.Collectors;

@NoArgsConstructor
public class PerformanceConverter {
    public static PerformanceListResDto performanceListResDto (Page<Performance> performancePage) {
        return PerformanceListResDto.builder()
                .performanceCount(performancePage.getTotalElements())
                .performanceList(performancePage.getContent().stream()
                        .map(performance -> new PerformanceResponseDto.PerformanceResDto(
                                performance.getId(),
                                performance.getTitle(),
                                performance.getPoster(),
                                performance.getRatingAverage(),
                                performance.getReviewSummary()
                        ))
                        .collect(Collectors.toList())
                )
                .build();
    }
}
