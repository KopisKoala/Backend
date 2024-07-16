package kopis.k_backend.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        Info info = new Info()
                .version("v1.0.0")
                .title("Koala's KOPIS API")
                .description("Koala's KOPIS API 명세서");

        // SecurityScheme명
        String jwtSchemeName = "AccessToken";
        // API 요청헤더에 인증정보 포함
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
        // SecuritySchemes 등록
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP) // HTTP 방식
                        .scheme("bearer")
                        .bearerFormat("JWT")); // 토큰 형식을 지정하는 임의의 문자(Optional)

        // 서버 URL 설정
        Server httpsServer = new Server();
        httpsServer.setUrl("https://kopis.sangsin.site");
        httpsServer.setDescription("kopis https 서버입니다.");

        Server httpServer = new Server();
        httpServer.setUrl("http://kopis.sangsin.site");
        httpServer.setDescription("kopis http 서버입니다.");

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components)
                .servers(List.of(httpsServer, httpServer));
    }

    // 사용자 auth -> swagger 사용 가능
    @Bean
    public GroupedOpenApi allGroup() {
        return GroupedOpenApi.builder()
                .group("All")
                .pathsToMatch("/**")
                .build();
    }
}
