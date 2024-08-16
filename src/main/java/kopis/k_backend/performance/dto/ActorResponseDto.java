package kopis.k_backend.performance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class ActorResponseDto {

    @Schema(description = "ActorResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ActorResDto {

        @Schema(description = "배우 id")
        private Long id;

        @Schema(description = "배우 이름")
        private String actorName;

        @Schema(description = "배우 프로필")
        private String actorProfile;
    }

    @Schema(description = "ActorListResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ActorListResDto {

        @Schema(description = "배우 수")
        private Long actorCount;

        @Schema(description = "배우 리스트")
        private List<ActorResDto> actorList;
    }
}
