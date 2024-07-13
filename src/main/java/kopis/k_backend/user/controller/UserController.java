package kopis.k_backend.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kopis.k_backend.global.api_payload.ApiResponse;
import kopis.k_backend.global.api_payload.SuccessCode;
import kopis.k_backend.user.domain.User;
import kopis.k_backend.user.dto.JwtDto;
import kopis.k_backend.user.dto.UserRequestDto;
import kopis.k_backend.user.jwt.CustomUserDetails;
import kopis.k_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Tag(name = "회원", description = "회원 관련 api 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Operation(summary = "로그아웃", description = "로그아웃하는 메서드입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_2001", description = "로그아웃 되었습니다."),
    })
    @DeleteMapping("/logout")
    public ApiResponse<String> logout(HttpServletRequest request) {
        userService.logout(request);
        return ApiResponse.onSuccess(SuccessCode.USER_LOGOUT_SUCCESS, "refresh token 삭제 완료");
    }

    @Operation(summary = "토큰 재발급", description = "토큰을 재발급하는 메서드입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_2002", description = "토큰 재발급이 완료되었습니다."),
    })
    @PostMapping("/reissue")
    public ApiResponse<JwtDto> reissue(
            HttpServletRequest request
    ) {
        JwtDto jwt = userService.reissue(request);
        return ApiResponse.onSuccess(SuccessCode.USER_REISSUE_SUCCESS, jwt);
    }

    @Operation(summary = "회원탈퇴", description = "회원 탈퇴하는 메서드입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_2003", description = "회원탈퇴가 완료되었습니다."),
    })
    @DeleteMapping("/delete")
    public ApiResponse<String> deleteUser(Authentication auth) {
        userService.deleteUser(auth.getName());
        return ApiResponse.onSuccess(SuccessCode.USER_DELETE_SUCCESS, "user entity 삭제 완료");
    }

    @Operation(summary = "닉네임 입력", description = "중복 안되는 닉네임을 입력받는 메서드입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_2004", description = "닉네임 생성이 완료되었습니다.")
    })
    @PostMapping(value = "/nickname")
    public ApiResponse<Boolean> nickname(
            @RequestBody UserRequestDto.UserNicknameReqDto nicknameReqDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        User user = userService.findByUserName(customUserDetails.getUsername());
        userService.saveNickname(nicknameReqDto, user);

        return ApiResponse.onSuccess(SuccessCode.USER_NICKNAME_SUCCESS, true);
    }
}