package kopis.k_backend.search.service;

import kopis.k_backend.performance.domain.Actor;
import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.performance.dto.ActorResponseDto.ActorResDto;
import kopis.k_backend.performance.dto.ActorResponseDto.ActorListResDto;
import kopis.k_backend.performance.dto.PerformanceResponseDto.PerformanceResDto;
import kopis.k_backend.performance.dto.PerformanceResponseDto.PerformanceListResDto;
import kopis.k_backend.performance.repository.ActorRepository;
import kopis.k_backend.performance.repository.PerformanceRepository;
import kopis.k_backend.search.dto.SearchResponseDto.SearchResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {
    private final PerformanceRepository performanceRepository;
    private final ActorRepository actorRepository;

    public SearchResDto getSearchResponseDto(final String query, final Pageable pageable) {
        // 공연 목록 검색
        Page<Performance> performancePage = performanceRepository.findByTitleContaining(query, pageable);

        System.out.println(performancePage.getTotalElements());

        PerformanceListResDto performances = PerformanceListResDto.builder()
                .performanceCount(performancePage.getTotalElements())  // 총 공연 수 설정
                .performanceList(performancePage.getContent().stream() // 공연 리스트 생성
                        .map(performance -> new PerformanceResDto(
                                performance.getId(),
                                performance.getTitle(),
                                performance.getPoster(),
                                performance.getRatingAverage(),
                                performance.getReviewSummary()
                        ))
                        .collect(Collectors.toList())
                )
                .build();

        // 배우 목록 검색
        Page<Actor> actorPage = actorRepository.findByActorNameContaining(query, pageable);

        System.out.println(actorPage.getTotalElements());

        ActorListResDto actors = ActorListResDto.builder()
                .actorCount(actorPage.getTotalElements())
                .actorList(actorPage.getContent().stream()
                        .map(actor -> new ActorResDto(
                                actor.getId(),
                                actor.getActorName(),
                                actor.getActorProfile()
                        ))
                        .collect(Collectors.toList())
                )
                .build();

        return SearchResDto.builder()
                .performances(performances)
                .actors(actors)
                .build();
    }
}
