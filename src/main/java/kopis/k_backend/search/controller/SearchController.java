package kopis.k_backend.search.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kopis.k_backend.global.api_payload.ApiResponse;
import kopis.k_backend.global.api_payload.SuccessCode;
import kopis.k_backend.search.dto.SearchResponseDto.SearchResDto;
import kopis.k_backend.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "검색", description = "검색 관련 api 입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "홈 화면 검색 결과 조회", description = "검색어가 포함된 배우와 공연 목록을 조회하는 메서드입니다. (page: 0부터 시작, size: 한 페이지 크기, sort: asc/desc")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SEARCH_2001", description = "홈 화면 검색 결과 반환 완료했습니다.")
    })
    @GetMapping(value = "/home")
    public ApiResponse<SearchResDto> getSearchResults(
            @RequestParam final String query,
            @PageableDefault final Pageable pageable
    ){
        final SearchResDto searchResDto = searchService.getSearchResponseDto(query, pageable);

        return ApiResponse.onSuccess(SuccessCode.SEARCH_HOME_SUCCESS, searchResDto);
    }

}
