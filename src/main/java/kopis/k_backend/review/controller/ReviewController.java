package kopis.k_backend.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kopis.k_backend.global.api_payload.ApiResponse;
import kopis.k_backend.pair.converter.PairConverter;
import kopis.k_backend.pair.domain.Pair;
import kopis.k_backend.pair.dto.PairResponseDto.PairListResDto;
import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.performance.service.PerformanceService;
import kopis.k_backend.review.converter.ReviewConverter;
import kopis.k_backend.review.domain.Review;
import kopis.k_backend.review.dto.ReviewRequestDto.ReviewReqDto;
import kopis.k_backend.global.api_payload.*;
import kopis.k_backend.review.dto.ReviewResponseDto.MyReviewResDto;
import kopis.k_backend.review.service.ReviewService;
import kopis.k_backend.user.jwt.CustomUserDetails;
import kopis.k_backend.user.service.RankService;
import kopis.k_backend.user.service.UserService;
import kopis.k_backend.pair.Service.PairService;
import kopis.k_backend.review.dto.ReviewResponseDto.ReviewListResDto;
import kopis.k_backend.review.dto.ReviewResponseDto.MonthReviewListResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import kopis.k_backend.user.domain.User;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "리뷰", description = "리뷰 관련 api 입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final UserService userService;
    private final ReviewService reviewService;
    private final PerformanceService performanceService;
    private final PairService pairService;
    private final RankService rankService;

    @Operation(summary = "공연에 따른 페어 반환", description = "공연에 따른 페어들을 반환하는 메서드입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "PAIR_2001", description = "공연에 맞는 페어들을 반환 완료했습니다.")
    })
    @GetMapping(value = "/{performance-id}/pairs")
    public ApiResponse<PairListResDto> create(
            @PathVariable(name = "performance-id") Long id,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        Performance performance = performanceService.findById(id);
        List<Pair> pairs = pairService.findPairsByPerformance(performance);

        return ApiResponse.onSuccess(SuccessCode.PERFORMANCE_MATCH_PAIRS, PairConverter.pairListResDto(pairs));
    }

    @Operation(summary = "리뷰 만들기 메서드", description = "리뷰를 만드는 메서드입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REVIEW_2011", description = "리뷰 생성이 완료되었습니다.")
    })
    @PostMapping(value = "/create")
    public ApiResponse<Long> create(
            // 받는 형식과 받은 결과
            @RequestBody ReviewReqDto reviewReqDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) throws IOException {
        try {
            // user Entity를 가져옴
            User user = userService.findByUserName(customUserDetails.getUsername());

            // performance Entity를 가져옴
            Performance performance = performanceService.findById(reviewReqDto.getPerformanceId());

            // pair Entity를 가져옴
            Pair pair = pairService.findById(reviewReqDto.getPairId());

            Review review = reviewService.create(reviewReqDto, user, performance, pair);

            rankService.increaseReviewCount(user);

            return ApiResponse.onSuccess(SuccessCode.REVIEW_CREATED, review.getId());

        } catch (Exception e) {
            // 예외 발생 시 응답 내용 로그
            log.error("Error during review creation", e);
            throw e;
        }
    }

    @Operation(summary = "리뷰 삭제 메서드", description = "리뷰를 삭제하는 메서드입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REVIEW_2001", description = "리뷰 삭제가 완료되었습니다.")
    })
    @DeleteMapping(value = "/delete/{review-id}")
    public ApiResponse<Boolean> delete(
            @PathVariable(name = "review-id") Long id,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        User user = userService.findByUserName(customUserDetails.getUsername());
        reviewService.delete(id, user);

        rankService.decreaseReviewCount(user);

        return ApiResponse.onSuccess(SuccessCode.REVIEW_DELETED, true);
    }

    @Operation(summary = "공연 리뷰 목록 조회 메서드", description = "공연 리뷰 목록을 조회하는 메서드입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REVIEW_2002", description = "공연 리뷰 목록 조회가 완료되었습니다.")
    })
    @Parameters({
            @Parameter(name = "performanceId", description = "조회하고 싶은 공연 id"),
            @Parameter(name = "way", description = "정렬 방식, recent: 최신순, like: 좋아요 순, desc: 공연 별점 높은 순, asc: 공연 별점 낮은 순"),
            @Parameter(name = "scrollPosition", description = "데이터 가져올 시작 위치. 0부터 시작. scrollPosition * fetchSize가 첫 데이터 주소"),
            @Parameter(name = "fetchSize", description = "가져올 데이터 크기 (리뷰 개수)")
    })
    @GetMapping("/list/performance")
    public ApiResponse<ReviewListResDto> getPerformanceReviews(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(name = "performanceId") Long performanceId,
            @RequestParam(name = "way") String way,
            @RequestParam(name = "scrollPosition", defaultValue = "0") Integer scrollPosition,
            @RequestParam(name = "fetchSize", defaultValue = "1000") Integer fetchSize
    ){
        User user = userService.findByUserName(customUserDetails.getUsername());
        Long reviewCount = performanceService.getReviewCountById(performanceId);
        Double rating = performanceService.getAverageRatingById(performanceId);
        Performance performance = performanceService.findById(performanceId);
        List<String> hashtags = new ArrayList<>();
        hashtags.add(performance.getHashtag1()); hashtags.add(performance.getHashtag2()); hashtags.add(performance.getHashtag3());
        String ratingType = "performance";

        List<Review> reviews = reviewService.getPerformanceReviewList(performanceId, way, scrollPosition, fetchSize);
        return ApiResponse.onSuccess(SuccessCode.REVIEW_LIST_VIEW_SUCCESS, ReviewConverter.reviewListResDto(reviews, reviewCount, rating, ratingType, hashtags, user));
    }

    @Operation(summary = "페어 리뷰 목록 조회 메서드", description = "페어 리뷰 목록을 조회하는 메서드입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REVIEW_2003", description = "페어 리뷰 목록 조회가 완료되었습니다.")
    })
    @Parameters({
            @Parameter(name = "pairId", description = "조회하고 싶은 페어 id"),
            @Parameter(name = "way", description = "정렬 방식, recent: 최신순, like: 좋아요 순, desc: 공연 별점 높은 순, asc: 공연 별점 낮은 순"),
            @Parameter(name = "scrollPosition", description = "데이터 가져올 시작 위치. 0부터 시작. scrollPosition * fetchSize가 첫 데이터 주소"),
            @Parameter(name = "fetchSize", description = "가져올 데이터 크기 (리뷰 개수)")
    })
    @GetMapping("/list/pair")
    public ApiResponse<ReviewListResDto> getPairReviews(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(name = "pairId") Long pairId,
            @RequestParam(name = "way") String way,
            @RequestParam(name = "scrollPosition", defaultValue = "0") Integer scrollPosition,
            @RequestParam(name = "fetchSize", defaultValue = "1000") Integer fetchSize
    ){
        User user = userService.findByUserName(customUserDetails.getUsername());
        Long reviewCount = pairService.getReviewCountById(pairId);
        Double rating = pairService.getAverageRatingById(pairId);
        Pair p = pairService.findById(pairId);
        List<String> hashtags = new ArrayList<>();
        hashtags.add(p.getHashtag1()); hashtags.add(p.getHashtag2()); hashtags.add(p.getHashtag3());
        String ratingType = "pair";

        List<Review> reviews = reviewService.getPairReviewList(pairId, way, scrollPosition, fetchSize);
        return ApiResponse.onSuccess(SuccessCode.REVIEW_LIST_VIEW_SUCCESS, ReviewConverter.reviewListResDto(reviews, reviewCount, rating, ratingType, hashtags, user));
    }

    @Operation(summary = "마이페이지 월 리뷰 목록 조회", description = "사용자가 해당 월에 작성한 리뷰 목록을 반환하는 메서드입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REVIEW_2005", description = "월 리뷰 목록 반환이 완료되었습니다.")
    })
    @Parameters({
            @Parameter(name = "month", description = "조회하고 싶은 월의 첫째날 ex) 2024-08-01")
    })
    @GetMapping(value = "/myPage/reviews")
    public ApiResponse<MonthReviewListResDto> getMonthReviews(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(name = "month") LocalDate month
    ){
        User user = userService.findByUserName(customUserDetails.getUsername());
        List<Review> monthReviewList = reviewService.getMonthReviewList(user, month);
        Long reviewCount = (long) monthReviewList.size();
        return ApiResponse.onSuccess(SuccessCode.REVIEW_MONTH_SUCCESS, ReviewConverter.monthReviewListResDto(monthReviewList, reviewCount));
    }

    @Operation(summary = "마이페이지 리뷰 조회", description = "리뷰 id에 따른 리뷰 정보를 반환하는 메서드입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REVIEW_2006", description = "리뷰 반환이 완료되었습니다.")
    })
    @GetMapping(value = "/myPage/review/{review-id}")
    public ApiResponse<MyReviewResDto> getMyReview(
            @PathVariable(name = "review-id") Long reviewId
    ){
        Review review = reviewService.findById(reviewId);

        return ApiResponse.onSuccess(SuccessCode.REVIEW_MY_SUCCESS, ReviewConverter.myReviewResDto(review));

    }

    @Operation(summary = "함께 본 사람 수정", description = "리뷰에 함께 본 사람을 적는 메서드입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REVIEW_2007", description = "함꼐 본 사람 수정이 완료되었습니다.")
    })
    @PatchMapping(value = "/myPage/viewingPartner/update/{review-id}")
    @Parameters({
            @Parameter(name = "partnerNumber", description = "0: 미입력, 1: 가족, 2: 친구, 3: 연인, 4: 혼자")
    })
    public void updateViewingPartner(
            @PathVariable(name = "review-id") Long reviewId,
            @RequestParam(name = "partnerNumber") Integer partnerNumber
    ){
        Review review = reviewService.findById(reviewId);
        reviewService.updateViewingPartner(review, partnerNumber);
    }

    @Operation(summary = "리뷰 메모 추가", description = "리뷰에 메모를 적는 메서드입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REVIEW_2008", description = "메모 추가가 완료되었습니다.")
    })
    @PatchMapping(value = "/myPage/memo/update/{review-id}")
    public void updateMemo(
            @PathVariable(name = "review-id") Long reviewId,
            @RequestBody String memo
    ){
        Review review = reviewService.findById(reviewId);
        reviewService.updateMemo(review, memo);
    }
}