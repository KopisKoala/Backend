package kopis.k_backend.global.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = {"kopis.k_backend.feign"})
class OpenFeignConfig {

}
