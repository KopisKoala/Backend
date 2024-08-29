package kopis.k_backend.performance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class ActorResponseDto {

    @Schema(description = "HomeSearchActorResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HomeSearchActorResDto {
        @Schema(description = "배우 id")
        private Long id;

        @Schema(description = "배우 이름")
        private String actorName;

        @Schema(description = "배우 프로필")
        private String actorProfile;

        @Schema(description = "배우 찜 여부")
        private String isFavoriteActor;
    }

    @Schema(description = "HomeSearchActorListResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HomeSearchActorListResDto {
        @Schema(description = "배우 수")
        private Long actorCount;

        @Schema(description = "배우 리스트")
        private List<HomeSearchActorResDto> actorList;
    }

    @Schema(description = "PerformanceDetailActorResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PerformanceDetailActorResDto {
        @Schema(description = "배우 id")
        private Long id;

        @Schema(description = "배우 이름")
        private String actorName;

        @Schema(description = "배우 프로필")
        private String actorProfile;

        @Schema(description = "배우 찜 여부")
        private String isFavoriteActor;

        @Schema(description = "배우 역할")
        private String characterName;
    }

    @Schema(description = "PerformanceDetailActorListResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PerformanceDetailActorListResDto {
        @Schema(description = "배우 리스트")
        private List<PerformanceDetailActorResDto> actorList;
    }

}
