package kopis.k_backend.performance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kopis.k_backend.global.api_payload.ApiResponse;
import kopis.k_backend.global.api_payload.SuccessCode;
import kopis.k_backend.performance.domain.Actor;
import kopis.k_backend.performance.domain.FavoriteActor;
import kopis.k_backend.performance.service.ActorService;
import kopis.k_backend.performance.service.FavoriteActorService;
import kopis.k_backend.user.domain.User;
import kopis.k_backend.user.jwt.CustomUserDetails;
import kopis.k_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "찜 배우", description = "찜 배우 관련 api입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/favorite/actor")
public class FavoriteActorController {
    private final UserService userService;
    private final ActorService actorService;
    private final FavoriteActorService favoriteActorService;

    @Operation(summary = "배우 찜 하기", description = "배우를 찜하는 메서드입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "FAVORITE_ACTOR_2011", description = "배우 찜이 완료되었습니다.")
    })
    @Parameters({
            @Parameter(name = "actorId", description = "찜할 배우 id")
    })
    @PostMapping(value = "/create/{actorId}")
    public ApiResponse<Long> create(
            @PathVariable(name = "actorId") Long actorId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        try {
            User user = userService.findByUserName(customUserDetails.getUsername());
            Actor actor = actorService.findById(actorId);

            FavoriteActor favoriteActor = favoriteActorService.create(user, actor);

            return ApiResponse.onSuccess(SuccessCode.FAVORITE_ACTOR_CREATED, favoriteActor.getId());
        } catch (Exception e) {
            log.error("Error during favorite actor creation", e);
            throw e;
        }
    }

    @Operation(summary = "배우 찜 취소하기", description = "배우 찜을 취소하는 메서드입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "FAVORITE_ACTOR_2001", description = "배우 찜 취소가 완료되었습니다.")
    })
    @Parameters({
            @Parameter(name = "actorId", description = "찜을 취소할 배우 id")
    })
    @GetMapping(value = "/delete/{actorId}")
    public ApiResponse<Boolean> delete(
            @PathVariable(name = "actorId") Long actorId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        try {
            User user = userService.findByUserName(customUserDetails.getUsername());
            Actor actor = actorService.findById(actorId);

            favoriteActorService.delete(user, actor);

            return ApiResponse.onSuccess(SuccessCode.FAVORITE_ACTOR_DELETED, true);
        } catch (Exception e) {
            log.error("Error during favorite actor deletion", e);
            throw e;
        }

    }

}
