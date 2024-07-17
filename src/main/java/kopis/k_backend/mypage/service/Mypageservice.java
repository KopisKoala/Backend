package kopis.k_backend.mypage.service;

import kopis.k_backend.user.jwt.JwtTokenUtils;
import kopis.k_backend.user.repository.RefreshTokenRepository;
import kopis.k_backend.user.repository.UserRepository;
import kopis.k_backend.user.service.JpaUserDetailsManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class Mypageservice {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JpaUserDetailsManager manager;
    private final JwtTokenUtils jwtTokenUtils;

    // 기존 메서드들 ...


}
