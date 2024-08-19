package kopis.k_backend.global.api_payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode implements BaseCode {
    // Common
    OK(HttpStatus.OK, "COMMON_200", "Success"),
    CREATED(HttpStatus.CREATED, "COMMON_201", "Created"),

    // User
    USER_LOGIN_SUCCESS(HttpStatus.CREATED, "USER_2011", "회원가입& 로그인이 완료되었습니다."),
    USER_LOGOUT_SUCCESS(HttpStatus.OK, "USER_2001", "로그아웃 되었습니다."),
    USER_REISSUE_SUCCESS(HttpStatus.OK, "USER_2002", "토큰 재발급이 완료되었습니다."),
    USER_DELETE_SUCCESS(HttpStatus.OK, "USER_2003", "회원탈퇴가 완료되었습니다."),

    USER_NICKNAME_SUCCESS(HttpStatus.OK, "USER_2004", "닉네임 생성/수정이 완료되었습니다."),
    USER_INFO_UPDATE_SUCCESS(HttpStatus.OK, "USER_2005", "회원 정보 수정이 완료 되었습니다."),
    USER_INFO_VIEW_SUCCESS(HttpStatus.OK, "USER_2006", "회원 정보 조회가 완료 되었습니다."),
    USER_PROFILE_IMAGE_UPDATE_SUCCESS(HttpStatus.OK, "USER_2007", "프로필 사진 이미지 업로드가 완료 되었습니다."),

    // Review
    REVIEW_CREATED(HttpStatus.CREATED, "REVIEW_2011", "리뷰가 생성되었습니다."),
    REVIEW_DELETED(HttpStatus.OK, "REVIEW_2001", "리뷰가 삭제되었습니다."),
    REVIEW_LIST_VIEW_SUCCESS(HttpStatus.OK, " REVIEW_2002", "리뷰 리스트 조회가 완료되었습니다."),
    REVIEW_LIKE_SUCCESS(HttpStatus.OK, " REVIEW_2003", "리뷰 좋아요 생성이 완료되었습니다."),
    REVIEW_UNLIKE_SUCCESS(HttpStatus.OK, " REVIEW_2004", "리뷰 좋아요 삭제가 완료되었습니다."),
    REVIEW_MONTH_SUCCESS(HttpStatus.OK, "REVIEW_2005", "회원이 해당 월에 작성한 리뷰 목록 반환이 완료되었습니다."),
    REVIEW_MY_SUCCESS(HttpStatus.OK, "REVIEW_2006", "리뷰 조회가 완료되었습니다."),

    // Pair
    PERFORMANCE_MATCH_PAIRS(HttpStatus.OK, "PAIR_2001", "공연에 맞는 페어들을 반환 완료했습니다."),

    // Goods
    GOODS_LIST_VIEW_SUCCESS(HttpStatus.OK, "GOODS_2001", "굿즈 리스트 조회가 완료되었습니다."),

    // DB
    DB_HALL_LIST_SUCCESS(HttpStatus.OK, "DB_HALL_2011", "공연장 리스트를 DB에 넣었습니다."),

    DB_PERF_STATE_UPDATE_SUCCESS(HttpStatus.OK, "DB_PERF_2013", "공연 상태를 업데이트하는 api입니다."),
    DB_PERF_LIST_SUCCESS(HttpStatus.OK, "DB_PERF_2014", "공연 리스트를 DB에 넣었습니다."),

    // Scrap
    SCRAP_PERF_PUT_ACTOR_SUCCESS(HttpStatus.OK, "SCRAP_PERF_2011", "PLAYDB의 공연 배우 정보를 DB에 넣었습니다."),

    // Open AI
    OPEN_AI_API_SUCCESS(HttpStatus.OK, "OPEN_2001", "GPT API 요청이 완료되었습니다."),
    OPEN_AI_PERFORMANCE_REVIEW_SUMMARY(HttpStatus.OK, "OPEN_2002", "공연 별 REVIEW 요약이 완료되었습니다."),
    OPEN_AI_PAIR_REVIEW_SUMMARY(HttpStatus.OK, "OPEN_2003", "페어 별 REVIEW 요약이 완료되었습니다."),

    // Search
    SEARCH_HOME_SUCCESS(HttpStatus.OK, "SEARCH_2001", "홈 화면 검색이 완료되었습니다."),

    // Favorite Actor
    FAVORITE_ACTOR_CREATED(HttpStatus.CREATED, "FAVORITE_ACTOR_2011", "찜 배우가 생성되었습니다."),
    FAVORITE_ACTOR_DELETED(HttpStatus.OK, "FAVORITE_ACTOR_2001", "찜 배우가 삭제되었습니다.")

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDto getReason() {
        return ReasonDto.builder()
                .httpStatus(this.httpStatus)
                .isSuccess(true)
                .code(this.code)
                .message(this.message)
                .build();
    }
}
