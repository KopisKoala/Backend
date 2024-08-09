package kopis.k_backend.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

@Configuration
public class OpenAiConfig {
    // open api key
    @Value("${openai.secret-key}")
    private String secretKey;

    // RESTful 웹 서비스와의 통신을 쉽게 할 수 있도록 도와주는 HTTP 클라이언트
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // HTTP 요청 및 응답의 헤더를 표현하는 클래스
    @Bean
    public HttpHeaders httpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
