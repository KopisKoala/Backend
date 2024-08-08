package kopis.k_backend.global.config;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        int timeout = 60 * 60 * 1000; // 1 hour in milliseconds

        CloseableHttpClient client = HttpClients.custom()
                .setDefaultRequestConfig(org.apache.hc.client5.http.config.RequestConfig.custom()
                        .setConnectTimeout(Timeout.ofMilliseconds(timeout))
                        .setConnectionRequestTimeout(Timeout.ofMilliseconds(timeout))
                        .setResponseTimeout(Timeout.ofMilliseconds(timeout))
                        .build())
                .build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(client);
        return new RestTemplate(factory);
    }
}
