package kopis.k_backend.global.api_payload;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ReasonDto {

    private HttpStatus httpStatus;
    private boolean isSuccess;
    private String code;
    private String message;
}
