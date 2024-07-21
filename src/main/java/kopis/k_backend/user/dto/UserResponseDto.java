package kopis.k_backend.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    @Schema(description = "사용자 ID")
    private Long id;

    @Schema(description = "사용자 이름")
    private String username;

    @Schema(description = "닉네임")
    private String nickname;

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "소셜 제공자")
    private String provider;

    @Schema(description = "주소")
    private String address;

    @Schema(description = "회원 등급")
    private String userRank;

    @Schema(description = "생성 일자")
    private LocalDateTime createdAt;

    @Schema(description = "수정 일자")
    private LocalDateTime updatedAt;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserUpdateDto {

        @Schema(description = "닉네임")
        private String nickname;

        @Schema(description = "주소")
        private String address;
    }
}
