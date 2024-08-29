package kopis.k_backend.search.service;

import kopis.k_backend.pair.converter.PairConverter;
import kopis.k_backend.pair.domain.Pair;
import kopis.k_backend.performance.converter.ActorConverter;
import kopis.k_backend.performance.converter.PerformanceConverter;
import kopis.k_backend.performance.domain.Actor;
import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.performance.dto.ActorResponseDto.HomeSearchActorListResDto;
import kopis.k_backend.performance.dto.PerformanceResponseDto.HomeSearchPerformanceListResDto;
import kopis.k_backend.performance.repository.ActorRepository;
import kopis.k_backend.performance.repository.PerformanceRepository;
import kopis.k_backend.search.converter.SearchConverter;
import kopis.k_backend.search.dto.SearchResponseDto.PairSearchResDto;
import kopis.k_backend.search.dto.SearchResponseDto.HomeSearchResDto;
import kopis.k_backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {
    private final PerformanceRepository performanceRepository;
    private final ActorRepository actorRepository;
    private final SearchConverter searchConverter;

    public HomeSearchResDto getHomeSearchResDto(final String query, final Pageable pageable, final User user) {
        // 공연 목록 검색
        Slice<Performance> performanceSlice = performanceRepository.findByTitleContaining(query, pageable);
        HomeSearchPerformanceListResDto homeSearchPerformanceListResDto = PerformanceConverter.homeSearchPerformanceListResDto(performanceSlice);

        // 배우 목록 검색
        Slice<Actor> actorSlice = actorRepository.findByActorNameContaining(query, pageable);
        HomeSearchActorListResDto actorListResDto = ActorConverter.homeSearchActorListResDto(actorSlice, user);

        return SearchConverter.homeSearchResDto(homeSearchPerformanceListResDto, actorListResDto);
    }

    public List<Performance> getPerformanceList(final String query, final Pageable pageable) {
        return performanceRepository.findByTitleContaining(query, pageable).toList();
    }

    public PairSearchResDto getPairSearchResDto(List<Performance> performanceList) {

        return searchConverter.pairSearchResDto(performanceList);

    }
}
