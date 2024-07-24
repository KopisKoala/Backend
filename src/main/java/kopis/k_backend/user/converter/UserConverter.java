package kopis.k_backend.user.converter;

import kopis.k_backend.user.domain.User;
import kopis.k_backend.user.domain.UserRank;
import kopis.k_backend.user.dto.JwtDto;
import kopis.k_backend.user.dto.UserRequestDto;
import kopis.k_backend.user.dto.UserResponseDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserConverter {
    public static User saveUser(UserRequestDto.UserReqDto userReqDto, String nick) {

        return User.builder()
                .email(userReqDto.getEmail())
                .username(userReqDto.getUsername())
                .provider(userReqDto.getProvider())
                .nickname(nick)
                .userRank(UserRank.B)
                .address("경기도 고양시 덕양구 항공대학로 76")
                .build();
    }

    public static JwtDto jwtDto(String access, String refresh, String signIn) {
        return JwtDto.builder()
                .accessToken(access)
                .refreshToken(refresh)
                .signIn(signIn)
                .build();
    }

    public static UserResponseDto.SimpleUserDto toUserDTO(User user) {
        return UserResponseDto.SimpleUserDto.builder()
                .nickname(user.getNickname())
                .email(user.getEmail())
                .address(user.getAddress())
                .userRank(String.valueOf(user.getUserRank()))
                .profileImage(user.getProfileImage())
                .build();
    }

}
