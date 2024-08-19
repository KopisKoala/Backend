package kopis.k_backend.global.api_payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements BaseCode {
    // Common
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON_400", "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_500", "서버 에러, 서버 개발자에게 문의하세요."),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_4041", "존재하지 않는 회원입니다."),
    USER_NOT_FOUND_BY_EMAIL(HttpStatus.NOT_FOUND, "USER_4042", "존재하지 않는 회원입니다.-EMAIL"),
    USER_NOT_FOUND_BY_USERNAME(HttpStatus.NOT_FOUND, "USER_4043", "존재하지 않는 회원입니다.-USERNAME"),
    ALREADY_USED_NICKNAME(HttpStatus.FORBIDDEN, "USER_4031", "이미 사용중인 닉네임입니다."),
    USER_ADDRESS_NULL(HttpStatus.BAD_REQUEST, "USER_4001", "주소값이 비었거나 NULL입니다."),

    // Jwt
    WRONG_REFRESH_TOKEN(HttpStatus.NOT_FOUND, "JWT_4041", "일치하는 리프레시 토큰이 없습니다."),
    TOKEN_INVALID(HttpStatus.FORBIDDEN, "JWT_4032", "유효하지 않은 토큰입니다."),
    TOKEN_NO_AUTH(HttpStatus.FORBIDDEN, "JWT_4033", "권한 정보가 없는 토큰입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT_4011", "토큰 유효기간이 만료되었습니다."),

    // Common
    INVALID_FILE_CONTENT_TYPE(HttpStatus.BAD_REQUEST, "COMMON_4001", "잘못된 파일 형식입니다."),
    MISMATCH_IMAGE_FILE(HttpStatus.BAD_REQUEST, "COMMON_4002", "파일 형식은 이미지(JPEG 또는 PNG)여야 합니다."),

    // Review
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "REVIEW_4041", "존재하지 않는 리뷰입니다."),
    REVIEW_NOT_YOURS(HttpStatus.BAD_REQUEST, "REVIEW_4001", "작성자가 아니므로 삭제할 수 없습니다."),
    REVIEW_ALREADY_LIKED(HttpStatus.BAD_REQUEST, "REVIEW_4002", "이미 좋아요를 누른 게시물입니다."),
    REVIEW_LIKED_NOT_FOUND(HttpStatus.NOT_FOUND, "REVIEW_4042", "존재하지 않는 리뷰 좋아요 입니다."),

    // Performance
    PERFORMANCE_NOT_FOUND(HttpStatus.NOT_FOUND, "PERFORMANCE_4041", "존재하지 않는 공연입니다."),

    // Pair
    PAIR_NOT_FOUND(HttpStatus.NOT_FOUND, "PAIR_4041", "존재하지 않는 페어입니다."),

    // Actor
    ACTOR_NOT_FOUND(HttpStatus.NOT_FOUND, "ACTOR_4041", "존재하지 않는 배우입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDto getReason() {
        return ReasonDto.builder()
                .httpStatus(this.httpStatus)
                .isSuccess(false)
                .code(this.code)
                .message(this.message)
                .build();
    }
}
