package kopis.k_backend.mypage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kopis.k_backend.global.api_payload.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kopis.k_backend.global.api_payload.SuccessCode;
import kopis.k_backend.user.domain.User;
import kopis.k_backend.user.jwt.CustomUserDetails;
import kopis.k_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "마이페이지", description = "마이페이지 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {

    private final UserService userService;

    // 회원 정보 조회
    @Operation(summary = "회원 정보 조회", description = "현재 로그인한 회원의 정보를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 정보 조회 완료")
    })
    @GetMapping("/info")
    public ApiResponse<User> getUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = userService.findByUserName(customUserDetails.getUsername());
        return ApiResponse.onSuccess(SuccessCode.USER_INFO_VIEW_SUCCESS, user);
    }

    // 회원 정보 수정
    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 정보 수정 완료"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "파일 업로드 중 오류 발생")
    })
    @PutMapping("/info")
    public ApiResponse<User> updateUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                            @RequestParam("nickname") String nickname,
                                            @RequestParam("address") String address,
                                            @RequestParam("email") String email,
                                            @RequestParam("membershipLevel") String membershipLevel,
                                            @RequestParam(value = "file", required = false) MultipartFile file) {
        User user = userService.findByUserName(customUserDetails.getUsername());
        user.setNickname(nickname);
        user.setAddress(address);
        user.setEmail(email);
        user.setMembershipLevel(membershipLevel);
        User updatedUser = userService.updateProfile(user, file);
        return ApiResponse.onSuccess(SuccessCode.USER_INFO_UPDATE_SUCCESS, updatedUser);
    }

    // 로그아웃
    @Operation(summary = "로그아웃", description = "현재 로그인한 사용자를 로그아웃합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그아웃 완료")
    })
    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        userService.logout(request);
        return ApiResponse.onSuccess(SuccessCode.USER_LOGOUT_SUCCESS, null);
    }

    // 회원 탈퇴
    @Operation(summary = "회원 탈퇴", description = "현재 로그인한 사용자를 탈퇴시킵니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 탈퇴 완료")
    })
    @DeleteMapping("/delete")
    public ApiResponse<Void> deleteUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        userService.deleteUser(customUserDetails.getUsername());
        return ApiResponse.onSuccess(SuccessCode.USER_DELETE_SUCCESS, null);
    }
}
