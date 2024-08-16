package kopis.k_backend.search.service;

import kopis.k_backend.performance.converter.ActorConverter;
import kopis.k_backend.performance.converter.PerformanceConverter;
import kopis.k_backend.performance.domain.Actor;
import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.performance.dto.ActorResponseDto.ActorListResDto;
import kopis.k_backend.performance.dto.PerformanceResponseDto.PerformanceListResDto;
import kopis.k_backend.performance.repository.ActorRepository;
import kopis.k_backend.performance.repository.PerformanceRepository;
import kopis.k_backend.search.converter.SearchConverter;
import kopis.k_backend.search.dto.SearchResponseDto.SearchResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {
    private final PerformanceRepository performanceRepository;
    private final ActorRepository actorRepository;

    public SearchResDto getSearchResponseDto(final String query, final Pageable pageable) {
        // 공연 목록 검색
        Page<Performance> performancePage = performanceRepository.findByTitleContaining(query, pageable);
        PerformanceListResDto performanceListResDto = PerformanceConverter.performanceListResDto(performancePage);

        // 배우 목록 검색
        Page<Actor> actorPage = actorRepository.findByActorNameContaining(query, pageable);
        ActorListResDto actorListResDto = ActorConverter.actorListResDto(actorPage);

        return SearchConverter.searchResDto(performanceListResDto, actorListResDto);
    }
}
