package kopis.k_backend.performance.converter;

import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.performance.dto.PerformanceResponseDto.PerformanceResDto;
import kopis.k_backend.performance.dto.PerformanceResponseDto.PerformanceListResDto;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.stream.Collectors;

@NoArgsConstructor
public class PerformanceConverter {
    public static PerformanceResDto performanceResDto(Performance performance) {
        return PerformanceResDto.builder()
                .id(performance.getId())
                .hallName(performance.getHall().getHallName())
                .title(performance.getTitle())
                .startDate(performance.getStartDate())
                .endDate(performance.getEndDate())
                .poster(performance.getPoster())
                .ratingAverage(performance.getRatingAverage())
                .reviewSummary(performance.getReviewSummary())
                .build();
    }

    public static PerformanceListResDto performanceListResDto (Page<Performance> performancePage) {
        return PerformanceListResDto.builder()
                .performanceCount(performancePage.getTotalElements())
                .performanceList(performancePage.getContent().stream()
                        .map(PerformanceConverter::performanceResDto)
                        .collect(Collectors.toList())
                )
                .build();
    }
}
