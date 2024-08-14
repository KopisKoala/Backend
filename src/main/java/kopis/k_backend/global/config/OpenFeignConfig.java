package kopis.k_backend.global.config;

import feign.Request;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = {"kopis.k_backend.feign"})
class OpenFeignConfig {
    @Bean
    public Request.Options feignOptions() {
        return new Request.Options(5000, 60000); // (연결 타임아웃, 읽기 타임아웃)
    }
}
