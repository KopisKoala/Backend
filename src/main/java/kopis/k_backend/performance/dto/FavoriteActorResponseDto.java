package kopis.k_backend.performance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class FavoriteActorResponseDto {
    @Schema(description = "FavoriteActorResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FavoriteActorResDto {
        @Schema(description = "찜 배우 id")
        private Long Id;

        @Schema(description = "배우 id")
        private Long actorId;

        @Schema(description = "배우 이름")
        private String actorName;

        @Schema(description = "배우 사진")
        private String actorProfile;
    }

    @Schema(description = "FavoriteActorListResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FavoriteActorListResDto {
        @Schema(description = "찜 배우 수")
        private Long favoriteActorCount;

        @Schema(description = "찜 배우 리스트")
        private List<FavoriteActorResDto> favoriteActorResDtoList;
    }
}
