package kopis.k_backend.performance.converter;

import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.performance.domain.PerformancePopularMusical;
import kopis.k_backend.performance.domain.PerformancePopularPlay;
import kopis.k_backend.performance.dto.PerformanceResponseDto;
import kopis.k_backend.performance.dto.PerformanceResponseDto.PerformanceListResDto;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
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

    public static PerformanceResponseDto.SimplePopularPerformanceDto simplePopularMusicalDto(PerformancePopularMusical popularMusical){
        String dur = popularMusical.getPerformance().getStartDate() + " ~ " + popularMusical.getPerformance().getEndDate();

        return PerformanceResponseDto.SimplePopularPerformanceDto.builder()
                .perfId(popularMusical.getPerformance().getId())
                .title(popularMusical.getPerformance().getTitle())
                .poster(popularMusical.getPerformance().getPoster())
                .rank(popularMusical.getRanking())
                .hall(popularMusical.getPerformance().getHall().getHallName())
                .duration(dur)
                .build();
    }

    public static PerformanceResponseDto.PopularPerformanceListDto popularMusicalListDto(List<PerformancePopularMusical> musicalList){
        List<PerformanceResponseDto.SimplePopularPerformanceDto> musicalResDtos = musicalList.stream()
                .map(PerformanceConverter::simplePopularMusicalDto)
                .toList();

        return PerformanceResponseDto.PopularPerformanceListDto.builder()
                .performanceList(musicalResDtos)
                .build();
    }

    public static PerformanceResponseDto.SimplePopularPerformanceDto simplePopularPlayDto(PerformancePopularPlay popularPlay){
        String dur = popularPlay.getPerformance().getStartDate() + " ~ " + popularPlay.getPerformance().getEndDate();

        return PerformanceResponseDto.SimplePopularPerformanceDto.builder()
                .perfId(popularPlay.getPerformance().getId())
                .title(popularPlay.getPerformance().getTitle())
                .poster(popularPlay.getPerformance().getPoster())
                .rank(popularPlay.getRanking())
                .hall(popularPlay.getPerformance().getHall().getHallName())
                .duration(dur)
                .build();
    }

    public static PerformanceResponseDto.PopularPerformanceListDto popularPlayListDto(List<PerformancePopularPlay> playList){
        List<PerformanceResponseDto.SimplePopularPerformanceDto> musicalResDtos = playList.stream()
                .map(PerformanceConverter::simplePopularPlayDto)
                .toList();

        return PerformanceResponseDto.PopularPerformanceListDto.builder()
                .performanceList(musicalResDtos)
                .build();
    }

}
