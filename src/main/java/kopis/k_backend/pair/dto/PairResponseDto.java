package kopis.k_backend.pair.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kopis.k_backend.pair.domain.Pair;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@NoArgsConstructor
public class PairResponseDto {

    @Schema(description = "SimplePairResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SimplePairResDto {
        @Schema(description = "페어 id")
        private Long pairId;

        @Schema(description = "배우1 이름")
        private String actor1;

        @Schema(description = "배우2 이름")
        private String actor2;
    }

    @Schema(description = "PairListResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PairListResDto {
        @Schema(description = "페어들")
        private List<SimplePairResDto> pairList;
    }

    @Schema(description = "SearchPairResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PairDetailResDto {
        @Schema(description = "페어 id")
        private Long id;

        @Schema(description = "페어가 속한 공연 제목")
        private String title;

        @Schema(description = "배우1 이름")
        private String actor1Name;

        @Schema(description = "배우2 이름")
        private String actor2Name;

        @Schema(description = "배우1 사진")
        private String actor1Profile;

        @Schema(description = "배우2 사진")
        private String actor2Profile;

        @Schema(description = "해시 태그 1")
        private String hashtag1;

        @Schema(description = "해시 태그 2")
        private String hashtag2;

        @Schema(description = "해시 태그 3")
        private String hashtag3;

        @Schema(description = "페어 리뷰 수")
        private Long reviewCount;

        @Schema(description = "평점")
        private Double ratingAverage;
    }

    @Schema(description = "PairDetailListResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PairDetailListResDto {
        @Schema(description = "공연 별 페어 리스트")
        private List<PairDetailResDto> pairDetailResDtoList;
    }

    @Schema(description = "PopularPairListResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PopularPairListResDto {
        @Schema(description = "페어 상세 리스트")
        private List<PairDetailResDto> pairDetailResDtoList;
    }

    @Schema(description = "RecommendPairResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RecommendPairResDto {
        @Schema(description = "추천 기준")
        private String standard;

        @Schema(description = "페어 정보")
        private PairDetailResDto pairDetailResDtoByStandard;
    }

    @Schema(description = "RecommendPairListDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RecommendPairListDto {
        @Schema(description = "추천 기준 별 페어 리스트")
        private List<RecommendPairResDto> recommendPairResDtoList;
    }
}