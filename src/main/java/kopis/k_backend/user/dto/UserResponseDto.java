package kopis.k_backend.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;


@NoArgsConstructor
public class UserResponseDto {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SimpleUserDto {
        @Schema(description = "닉네임")
        private String nickname;

        @Schema(description = "이메일")
        private String email;

        @Schema(description = "주소")
        private String address;

        @Schema(description = "회원 등급")
        private String userRank;

        @Schema(description = "프로필 이미지")
        private String profileImage;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserUpdateProfileImageDto {

        @Schema(description = "프로필 이미지 변경")
        private MultipartFile profileImage;
    }
}

