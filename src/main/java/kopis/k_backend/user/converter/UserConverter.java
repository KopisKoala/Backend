package kopis.k_backend.user.converter;

import kopis.k_backend.user.domain.User;
import kopis.k_backend.user.domain.UserRank;
import kopis.k_backend.user.dto.JwtDto;
import kopis.k_backend.user.dto.UserRequestDto;
import kopis.k_backend.user.dto.UserResponseDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserConverter {
    public static User saveUser(UserRequestDto.UserReqDto userReqDto) {
        return User.builder()
                .email(userReqDto.getEmail())
                .username(userReqDto.getUsername())
                .provider(userReqDto.getProvider())
                .nickname("WhaShow01")
                .userRank(UserRank.B)
                .build();
    }

    public static JwtDto jwtDto(String access, String refresh, String signIn) {
        return JwtDto.builder()
                .accessToken(access)
                .refreshToken(refresh)
                .signIn(signIn)
                .build();
    }

    public static UserResponseDto toUserDTO(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .provider(user.getProvider())
                .address(user.getAddress())
                .userRank(String.valueOf(user.getUserRank()))
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public static void updateUserFromDto(User user, UserResponseDto.UserUpdateDto userUpdateDto) {
        if (userUpdateDto.getNickname() != null) {
            user.setNickname(userUpdateDto.getNickname());
        }
        if (userUpdateDto.getAddress() != null) {
            user.setAddress(userUpdateDto.getAddress());
        }
    }

}
