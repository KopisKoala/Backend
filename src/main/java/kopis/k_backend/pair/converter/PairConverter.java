package kopis.k_backend.pair.converter;

import kopis.k_backend.global.api_payload.ErrorCode;
import kopis.k_backend.global.exception.GeneralException;
import kopis.k_backend.pair.domain.Pair;
import kopis.k_backend.pair.dto.PairResponseDto.RecommendPairResDto;
import kopis.k_backend.pair.dto.PairResponseDto.RecommendPairListDto;
import kopis.k_backend.pair.dto.PairResponseDto.PopularPairListResDto;
import kopis.k_backend.pair.dto.PairResponseDto.PairDetailListResDto;
import kopis.k_backend.pair.dto.PairResponseDto.PairDetailResDto;
import kopis.k_backend.pair.dto.PairResponseDto.SimplePairResDto;
import kopis.k_backend.pair.dto.PairResponseDto.PairListResDto;
import kopis.k_backend.performance.domain.Actor;
import kopis.k_backend.performance.dto.ActorResponseDto;
import kopis.k_backend.performance.repository.ActorRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PairConverter {
    private final ActorRepository actorRepository;

    public SimplePairResDto simplePairResDto(Pair pair) {
        Actor actor_1 = actorRepository.findById(pair.getActor1())
                .orElseThrow(() -> GeneralException.of(ErrorCode.ACTOR_NOT_FOUND));

        Actor actor_2 = actorRepository.findById(pair.getActor2())
                .orElseThrow(() -> GeneralException.of(ErrorCode.ACTOR_NOT_FOUND));

        return SimplePairResDto.builder()
                .pairId(pair.getId())
                .actor1(actor_1.getActorName())
                .actor2(actor_2.getActorName())
                .build();
    }

    public PairListResDto pairListResDto(List<Pair> pairs) {
        List<SimplePairResDto> pairResDtos = pairs.stream()
                .map(this::simplePairResDto)
                .collect(Collectors.toList());

        return PairListResDto.builder()
                .pairList(pairResDtos)
                .build();
    }

    public PairDetailResDto pairDetailResDto(Pair pair) {
        Actor actor1 = actorRepository.findById(pair.getActor1())
                .orElseThrow(() -> new GeneralException(ErrorCode.ACTOR_NOT_FOUND));
        Actor actor2 = actorRepository.findById(pair.getActor2())
                .orElseThrow(() -> new GeneralException(ErrorCode.ACTOR_NOT_FOUND));

        return PairDetailResDto.builder()
                .id(pair.getId())
                .actor1Name(actor1.getActorName())
                .actor2Name(actor2.getActorName())
                .actor1Profile(actor1.getActorProfile())
                .actor2Profile(actor1.getActorProfile())
                .hashtag1(pair.getHashtag1())
                .hashtag2(pair.getHashtag2())
                .hashtag3(pair.getHashtag3())
                .reviewCount(pair.getReviewCount())
                .ratingAverage(pair.getRatingAverage())
                .build();
    }

    public PairDetailListResDto pairDetailListResDto(List<Pair> pairList) {
        List<PairDetailResDto> pairDetailListResDtoList = pairList.stream()
                .map(this::pairDetailResDto)
                .toList();

        return PairDetailListResDto.builder()
                .pairDetailResDtoList(pairDetailListResDtoList)
                .pairCount((long) pairDetailListResDtoList.size())
                .performanceTitle(pairList.get(0).getPerformance().getTitle())
                .build();
    }

    public PopularPairListResDto popularPairListResDto (List<Pair> pairList) {
        List<PairDetailResDto> pairDetailResDtoList = pairList.stream()
                .map(this::pairDetailResDto)
                .toList();

        return PopularPairListResDto.builder()
                .pairDetailResDtoList(pairDetailResDtoList)
                .build();
    }

    public RecommendPairResDto recommendPairResDto(Pair pair, String standard) {
        return RecommendPairResDto.builder()
                .standard(standard)
                .pairDetailResDtoByStandard(pairDetailResDto(pair))
                .build();
    }

    public RecommendPairListDto recommendPairListDto(Pair topRatedPair, Pair favoritePair, Pair preActorPair, Pair preHashtagPair) {
        List<RecommendPairResDto> recommendPairResDtoList = new ArrayList<>();

        if (topRatedPair != null) recommendPairResDtoList.add(recommendPairResDto(topRatedPair, "topRatedPair"));
        if (favoritePair != null) recommendPairResDtoList.add(recommendPairResDto(favoritePair, "favoritePair"));
        if (preActorPair != null) recommendPairResDtoList.add(recommendPairResDto(preActorPair, "preActorPair"));
        if (preHashtagPair != null) recommendPairResDtoList.add(recommendPairResDto(preHashtagPair, "preHashtagPair"));

        return RecommendPairListDto.builder()
                .recommendPairResDtoList(recommendPairResDtoList)
                .build();
    }
}
