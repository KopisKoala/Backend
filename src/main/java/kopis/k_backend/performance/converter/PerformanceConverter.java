package kopis.k_backend.performance.converter;

import kopis.k_backend.performance.domain.*;
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

    public static PerformanceResponseDto.SimpleRankPerformanceDto simplePopularMusicalDto(PerformancePopularMusical popularMusical){
        String dur = popularMusical.getPerformance().getStartDate() + " ~ " + popularMusical.getPerformance().getEndDate();

        return PerformanceResponseDto.SimpleRankPerformanceDto.builder()
                .perfId(popularMusical.getPerformance().getId())
                .title(popularMusical.getPerformance().getTitle())
                .poster(popularMusical.getPerformance().getPoster())
                .rank(popularMusical.getRanking())
                .hall(popularMusical.getPerformance().getHall().getHallName())
                .duration(dur)
                .build();
    }

    public static PerformanceResponseDto.RankPerformanceListDto popularMusicalListDto(List<PerformancePopularMusical> musicalList){
        List<PerformanceResponseDto.SimpleRankPerformanceDto> musicalResDtos = musicalList.stream()
                .map(PerformanceConverter::simplePopularMusicalDto)
                .toList();

        return PerformanceResponseDto.RankPerformanceListDto.builder()
                .performanceList(musicalResDtos)
                .build();
    }

    public static PerformanceResponseDto.SimpleRankPerformanceDto simplePopularPlayDto(PerformancePopularPlay popularPlay){
        String dur = popularPlay.getPerformance().getStartDate() + " ~ " + popularPlay.getPerformance().getEndDate();

        return PerformanceResponseDto.SimpleRankPerformanceDto.builder()
                .perfId(popularPlay.getPerformance().getId())
                .title(popularPlay.getPerformance().getTitle())
                .poster(popularPlay.getPerformance().getPoster())
                .rank(popularPlay.getRanking())
                .hall(popularPlay.getPerformance().getHall().getHallName())
                .duration(dur)
                .build();
    }

    public static PerformanceResponseDto.RankPerformanceListDto popularPlayListDto(List<PerformancePopularPlay> playList){
        List<PerformanceResponseDto.SimpleRankPerformanceDto> musicalResDtos = playList.stream()
                .map(PerformanceConverter::simplePopularPlayDto)
                .toList();

        return PerformanceResponseDto.RankPerformanceListDto.builder()
                .performanceList(musicalResDtos)
                .build();
    }

    public static PerformanceResponseDto.SimpleRankPerformanceDto simpleAdvertisePerformanceDto(PerformanceAdvertise performanceAdvertise){
        String dur = performanceAdvertise.getPerformance().getStartDate() + " ~ " + performanceAdvertise.getPerformance().getEndDate();

        return PerformanceResponseDto.SimpleRankPerformanceDto.builder()
                .perfId(performanceAdvertise.getPerformance().getId())
                .title(performanceAdvertise.getPerformance().getTitle())
                .poster(performanceAdvertise.getPerformance().getPoster())
                .rank(performanceAdvertise.getNumber())
                .hall(performanceAdvertise.getPerformance().getHall().getHallName())
                .duration(dur)
                .build();
    }

    public static PerformanceResponseDto.RankPerformanceListDto performanceAdvertiseListDto(List<PerformanceAdvertise> advertiseList){
        List<PerformanceResponseDto.SimpleRankPerformanceDto> advertiseResDtos = advertiseList.stream()
                .map(PerformanceConverter::simpleAdvertisePerformanceDto)
                .toList();

        return PerformanceResponseDto.RankPerformanceListDto.builder()
                .performanceList(advertiseResDtos)
                .build();
    }

    public static PerformanceResponseDto.SimpleRankPerformanceDto simpleAttractPerformanceDto(PerformanceAttract performanceAttract){
        String dur = performanceAttract.getPerformance().getStartDate() + " ~ " + performanceAttract.getPerformance().getEndDate();

        return PerformanceResponseDto.SimpleRankPerformanceDto.builder()
                .perfId(performanceAttract.getPerformance().getId())
                .title(performanceAttract.getPerformance().getTitle())
                .poster(performanceAttract.getPerformance().getPoster())
                .rank(performanceAttract.getRanking())
                .hall(performanceAttract.getPerformance().getHall().getHallName())
                .duration(dur)
                .build();
    }

    public static PerformanceResponseDto.RankPerformanceListDto performanceAttractListDto(List<PerformanceAttract> attractList){
        List<PerformanceResponseDto.SimpleRankPerformanceDto> attractResDtos = attractList.stream()
                .map(PerformanceConverter::simpleAttractPerformanceDto)
                .toList();

        return PerformanceResponseDto.RankPerformanceListDto.builder()
                .performanceList(attractResDtos)
                .build();
    }

}
