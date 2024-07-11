package kopis.k_backend.global.api_payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class ApiResponse<T> {

    private final Boolean isSuccess;
    private final String code;
    private final String message;
    @JsonInclude(Include.NON_NULL)
    private final T result;

    public static <T> ApiResponse<T> onSuccess(T result) {
        return new ApiResponse<>(true, SuccessCode.OK.getCode(), SuccessCode.OK.getMessage(),
                result);
    }

    public static <T> ApiResponse<T> onSuccess(BaseCode code, T result) {
        return new ApiResponse<>(true, code.getReason().getCode(), code.getReason().getMessage(),
                result);
    }

    public static <T> ApiResponse<T> onFailure(String code, String message, T data) {
        return new ApiResponse<>(false, code, message, data);
    }

}
