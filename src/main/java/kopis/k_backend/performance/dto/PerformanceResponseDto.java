package kopis.k_backend.performance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;
import kopis.k_backend.performance.dto.ActorResponseDto.PerformanceDetailActorListResDto;

public class PerformanceResponseDto {
    @Schema(description = "SearchPerformanceResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HomeSearchPerformanceResDto {

        @Schema(description = "공연 id")
        private Long id;

        @Schema(description = "공연 이름")
        private String title;

        @Schema(description = "공연 포스터")
        private String poster;
    }

    @Schema(description = "HomeSearchPerformanceListResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HomeSearchPerformanceListResDto {

        @Schema(description = "공연 수")
        private Long performanceCount;

        @Schema(description = "공연 리스트")
        private List<HomeSearchPerformanceResDto> performanceList;
    }

    @Schema(description = "SimpleRankPerformanceDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SimpleRankPerformanceDto {

        @Schema(description = "공연 id")
        private Long perfId;

        @Schema(description = "공연 이름")
        private String title;

        @Schema(description = "공연 포스터")
        private String poster;

        @Schema(description = "공연 순위")
        private Integer rank;

        @Schema(description = "공연장 이름")
        private String hall;

        @Schema(description = "공연 상영 시간")
        private String duration;

    }

    @Schema(description = "RankPerformanceListDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RankPerformanceListDto {

        @Schema(description = "공연 리스트")
        private List<SimpleRankPerformanceDto> performanceList;

    }

    @Schema(description = "PerformanceDetailResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PerformanceDetailResDto {
        @Schema(description = "공연 id")
        private Long id;

        @Schema(description = "공연 제목")
        private String title;

        @Schema(description = "공연 포스터")
        private String poster;

        @Schema(description = "해시태그 1")
        private String hashtag1;

        @Schema(description = "해시태그 2")
        private String hashtag2;

        @Schema(description = "해시태그 3")
        private String hashtag3;

        @Schema(description = "평점")
        private Double ratingAverage;

        @Schema(description = "공연 시작 날짜")
        private String startDate;

        @Schema(description = "공연 끝 날짜")
        private String endDate;

        @Schema(description = "공연장 이름")
        private String hallName;

        @Schema(description = "도로명 주소")
        private String streetAddress;

        @Schema(description = "공연 시간")
        private String runtime;

        @Schema(description = "공연 상태")
        private String state;

        @Schema(description = "예매 링크")
        private String ticketingLink;

        @Schema(description = "식당 여부")
        private String isRestaurant;

        @Schema(description = "편의점 여부")
        private String isStore;

        @Schema(description = "카페 여부")
        private String isCafe;

        @Schema(description = "놀이방 여부")
        private String isNolibang;

        @Schema(description = "수유실 여부")
        private String isSuyu;

        @Schema(description = "주차시설 여부")
        private String isParking;

        @Schema(description = "배우 리스트")
        private PerformanceDetailActorListResDto performanceDetailActorListResDto;

    }

}
