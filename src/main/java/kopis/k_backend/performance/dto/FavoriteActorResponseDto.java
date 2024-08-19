package kopis.k_backend.performance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FavoriteActorResponseDto {
    @Schema(description = "FavoriteActorResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FavoriteActorResDto {
        @Schema(description = "배우 id")
        private Long id;

        @Schema(description = "배우 이름")
        private String actorName;

        @Schema(description = "배우 사진")
        private String actorProfile;
    }
}
