package kopis.k_backend.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kopis.k_backend.global.api_payload.ApiResponse;
import kopis.k_backend.global.api_payload.SuccessCode;
import kopis.k_backend.user.converter.UserConverter;
import kopis.k_backend.user.domain.User;
import kopis.k_backend.user.dto.JwtDto;
import kopis.k_backend.user.dto.UserRequestDto;
import kopis.k_backend.user.service.UserService;
import org.springframework.web.bind.annotation.*;

@Tag(name = "토큰", description = "access token 관련 api 입니다.")
@RestController
@RequestMapping("/token")
public class TokenController {

    private final UserService userService;
    @Operation(summary = "토큰 반환", description = "프론트에게 유저 정보 받아 토큰 반환하는 메서드입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_2011", description = "회원가입 & 로그인 성공"),
    })
    @PostMapping("/generate")
    public ApiResponse<JwtDto> tokenToFront(
            @RequestBody UserRequestDto.UserReqDto userReqDto // email, username, provider
    ) {
        // 1. 받은 email 가지고 회원가입 되어있는 사용자인지 판단
        Boolean isMember = userService.checkMemberByEmail(userReqDto.getEmail());

        // 2. jwt 생성
        String accessToken = "";
        String refreshToken = "";

        // 3. 기존 회원인지 판별 -> 튜토리얼 때문
        String signIn = "wasUser";

        if(isMember){ // jwt 재생성 -> 성공
            // jwt 생성
            JwtDto jwt = userService.jwtMakeSave(userReqDto.getUsername());
            // 변수에 저장
            accessToken = jwt.getAccessToken();
            refreshToken = jwt.getRefreshToken();

        } else{ // 회원 가입 이후 jwt 생성 (db 접근 해야함)
            // db에 정보 넣기(= 회원가입)
            User user = userService.createUser(userReqDto);
            // jwt 생성
            JwtDto jwt = userService.jwtMakeSave(userReqDto.getUsername());
            // 변수에 저장
            accessToken = jwt.getAccessToken();
            refreshToken = jwt.getRefreshToken();
            // 회원이 됨
            signIn = "newUser";
        }

        return ApiResponse.onSuccess(SuccessCode.USER_LOGIN_SUCCESS, UserConverter.jwtDto(accessToken, refreshToken, signIn));
    }

    public TokenController(UserService userService) {
        this.userService = userService;
    }
}
