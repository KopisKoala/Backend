package kopis.k_backend.user.jwt;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Header에 포함한 JWT를 해석하고, 그에 따라 사용자가 인증된 상태인지를 확인
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthCreationFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        log.info("url : {}", request.getServletPath());

        // JWT가 포함되어 있으면 포함되어 있는 헤더를 요청
        String authHeader
                = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authHeader 확인: " + authHeader);

        // authHeader가 null이 아니면서 "Bearer " 로 구성되어 있어야 정상적인 인증 정보
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // JWT를 회수하여 JWT가 정상적인 JWT인지를 판단
            String token = authHeader.split(" ")[1];

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            Authentication authentication;

            if (request.getServletPath().equals("/users/reissue")) {
                log.info("엑세스 토큰 재발급을 위한 익명 인증 객체 생성");
                authentication =
                        new AnonymousAuthenticationToken( //객체 새로 만들어서 필터로 보내줌
                                "key",
                                "anonymousUser",
                                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")
                        );

            } else {
                log.info("사용자 인증 객체 생성 시작");
                Claims claims = jwtTokenUtils.parseClaims(token);
                authentication = new UsernamePasswordAuthenticationToken(
                        CustomUserDetails.builder()
                                .username(claims.getSubject())
                                .build(),
                        token,
                        jwtTokenUtils.getAuthFromClaims(claims)
                );
            }
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            log.info("{} 인증 객체 생성 완료", context.getAuthentication().getName());
        }
        filterChain.doFilter(request, response);
    }
}
