package kopis.k_backend.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
@Getter
@Setter
public class JwtDto {
    private String accessToken;
    private String refreshToken;
    private String signIn;

    public JwtDto(String accessToken, String refreshToken, String signIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.signIn = signIn;
    }
}
