package kopis.k_backend.performance.converter;

import kopis.k_backend.performance.domain.*;
import kopis.k_backend.performance.dto.ActorResponseDto.PerformanceDetailActorListResDto;
import kopis.k_backend.performance.dto.PerformanceResponseDto;
import kopis.k_backend.performance.dto.PerformanceResponseDto.PerformanceDetailResDto;
import kopis.k_backend.performance.dto.PerformanceResponseDto.SimpleRankPerformanceDto;
import kopis.k_backend.performance.dto.PerformanceResponseDto.RankPerformanceListDto;
import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.performance.dto.PerformanceResponseDto.HomeSearchPerformanceResDto;
import kopis.k_backend.performance.dto.PerformanceResponseDto.HomeSearchPerformanceListResDto;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
public class PerformanceConverter {
    public static HomeSearchPerformanceResDto homeSearchPerformanceResDto(Performance performance) {
        return HomeSearchPerformanceResDto.builder()
                .id(performance.getId())
                .title(performance.getTitle())
                .poster(performance.getPoster())
                .build();
    }

    public static HomeSearchPerformanceListResDto homeSearchPerformanceListResDto (Slice<Performance> performanceSlice) {
        return HomeSearchPerformanceListResDto.builder()
                .performanceCount(performanceSlice.stream().count())
                .performanceList(performanceSlice.getContent().stream()
                        .map(PerformanceConverter::homeSearchPerformanceResDto)
                        .collect(Collectors.toList())
                )
                .build();
    }

    public static SimpleRankPerformanceDto simplePopularMusicalDto(PerformancePopularMusical popularMusical){
        String dur = popularMusical.getPerformance().getStartDate() + " ~ " + popularMusical.getPerformance().getEndDate();

        return SimpleRankPerformanceDto.builder()
                .perfId(popularMusical.getPerformance().getId())
                .title(popularMusical.getPerformance().getTitle())
                .poster(popularMusical.getPerformance().getPoster())
                .rank(popularMusical.getRanking())
                .hall(popularMusical.getPerformance().getHall().getHallName())
                .duration(dur)
                .build();
    }

    public static RankPerformanceListDto popularMusicalListDto(List<PerformancePopularMusical> musicalList){
        List<SimpleRankPerformanceDto> musicalResDtos = musicalList.stream()
                .map(PerformanceConverter::simplePopularMusicalDto)
                .toList();

        return RankPerformanceListDto.builder()
                .performanceList(musicalResDtos)
                .build();
    }

    public static SimpleRankPerformanceDto simplePopularPlayDto(PerformancePopularPlay popularPlay){
        String dur = popularPlay.getPerformance().getStartDate() + " ~ " + popularPlay.getPerformance().getEndDate();

        return SimpleRankPerformanceDto.builder()
                .perfId(popularPlay.getPerformance().getId())
                .title(popularPlay.getPerformance().getTitle())
                .poster(popularPlay.getPerformance().getPoster())
                .rank(popularPlay.getRanking())
                .hall(popularPlay.getPerformance().getHall().getHallName())
                .duration(dur)
                .build();
    }

    public static RankPerformanceListDto popularPlayListDto(List<PerformancePopularPlay> playList){
        List<SimpleRankPerformanceDto> musicalResDtos = playList.stream()
                .map(PerformanceConverter::simplePopularPlayDto)
                .toList();

        return RankPerformanceListDto.builder()
                .performanceList(musicalResDtos)
                .build();
    }

    public static SimpleRankPerformanceDto simpleAdvertisePerformanceDto(PerformanceAdvertise performanceAdvertise){
        String dur = performanceAdvertise.getPerformance().getStartDate() + " ~ " + performanceAdvertise.getPerformance().getEndDate();

        return SimpleRankPerformanceDto.builder()
                .perfId(performanceAdvertise.getPerformance().getId())
                .title(performanceAdvertise.getPerformance().getTitle())
                .poster(performanceAdvertise.getPerformance().getPoster())
                .rank(performanceAdvertise.getNumber())
                .hall(performanceAdvertise.getPerformance().getHall().getHallName())
                .duration(dur)
                .build();
    }

    public static RankPerformanceListDto performanceAdvertiseListDto(List<PerformanceAdvertise> advertiseList){
        List<SimpleRankPerformanceDto> advertiseResDtos = advertiseList.stream()
                .map(PerformanceConverter::simpleAdvertisePerformanceDto)
                .toList();

        return RankPerformanceListDto.builder()
                .performanceList(advertiseResDtos)
                .build();
    }

    public static SimpleRankPerformanceDto simpleAttractPerformanceDto(PerformanceAttract performanceAttract){
        String dur = performanceAttract.getPerformance().getStartDate() + " ~ " + performanceAttract.getPerformance().getEndDate();

        return SimpleRankPerformanceDto.builder()
                .perfId(performanceAttract.getPerformance().getId())
                .title(performanceAttract.getPerformance().getTitle())
                .poster(performanceAttract.getPerformance().getPoster())
                .rank(performanceAttract.getRanking())
                .hall(performanceAttract.getPerformance().getHall().getHallName())
                .duration(dur)
                .build();
    }

    public static RankPerformanceListDto performanceAttractListDto(List<PerformanceAttract> attractList){
        List<SimpleRankPerformanceDto> attractResDtos = attractList.stream()
                .map(PerformanceConverter::simpleAttractPerformanceDto)
                .toList();

        return RankPerformanceListDto.builder()
                .performanceList(attractResDtos)
                .build();
    }

    public static PerformanceDetailResDto performanceDetailResDto(Performance performance, PerformanceDetailActorListResDto performanceDetailActorListResDto) {
        return PerformanceDetailResDto.builder()
                .id(performance.getId())
                .title(performance.getTitle())
                .poster(performance.getPoster())
                .hashtag1(performance.getHashtag1())
                .hashtag2(performance.getHashtag2())
                .hashtag3(performance.getHashtag3())
                .ratingAverage(performance.getRatingAverage())
                .startDate(performance.getStartDate())
                .endDate(performance.getEndDate())
                .hallName(performance.getHall().getHallName())
                .streetAddress(performance.getHall().getStreetAddress())
                .runtime(performance.getRuntime())
                .state(performance.getState())
                .ticketingLink(performance.getTicketingLink())
                .isRestaurant(performance.getHall().getRestaurant())
                .isStore(performance.getHall().getStore())
                .isCafe(performance.getHall().getCafe())
                .isNolibang(performance.getHall().getNolibang())
                .isSuyu(performance.getHall().getSuyu())
                .isParking(performance.getHall().getParkinglot())
                .performanceDetailActorListResDto(performanceDetailActorListResDto)
                .build();
    }

    public static PerformanceResponseDto.SimpleRecommendPerfDto simpleRecommendResDto(Performance performance){
        return PerformanceResponseDto.SimpleRecommendPerfDto.builder()
                .id(performance.getId())
                .title(performance.getTitle())
                .poster(performance.getPoster())
                .ratingAverage(performance.getRatingAverage())

                .price(performance.getPrice())
                .startDate(performance.getStartDate())
                .endDate(performance.getEndDate())
                .build();
    }

    public static PerformanceResponseDto.StandardRecommendPerfDto standardRecommendResDto(String standard, List<Performance> performanceList){
        List<PerformanceResponseDto.SimpleRecommendPerfDto> recommendResDtos = performanceList.stream()
                .map(PerformanceConverter::simpleRecommendResDto)
                .toList();

        return PerformanceResponseDto.StandardRecommendPerfDto.builder()
                .standard(standard)
                .performancesByStandard(recommendResDtos)
                .build();

    }

    public static PerformanceResponseDto.StandardRecommendPerfListDto standardRecommendListResDto(Map<String, List<Performance>> performancesByStandardMap) {
        List<PerformanceResponseDto.StandardRecommendPerfDto> recommendResDtos = performancesByStandardMap.entrySet().stream()
                .map(entry -> standardRecommendResDto(entry.getKey(), entry.getValue()))
                .toList();

        return PerformanceResponseDto.StandardRecommendPerfListDto.builder()
                .performancesByStandardList(recommendResDtos)
                .build();
    }

}
