package kopis.k_backend.pair.converter;

import kopis.k_backend.global.api_payload.ErrorCode;
import kopis.k_backend.global.exception.GeneralException;
import kopis.k_backend.pair.domain.Pair;
import kopis.k_backend.pair.dto.PairResponseDto;
import kopis.k_backend.performance.domain.Actor;
import kopis.k_backend.performance.repository.ActorRepository;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class PairConverter {

    private static ActorRepository actorRepository;

    public static PairResponseDto.SimplePairResDto simplePairResDto(Pair pair){
        Actor actor_1 = actorRepository.findById(pair.getActor1())
                .orElseThrow(() -> GeneralException.of(ErrorCode.ACTOR_NOT_FOUND));

        Actor actor_2 = actorRepository.findById(pair.getActor2())
                .orElseThrow(() -> GeneralException.of(ErrorCode.ACTOR_NOT_FOUND));

        return PairResponseDto.SimplePairResDto.builder()
                .pairId(pair.getId())
                .actor1(actor_1.getActorName())
                .actor2(actor_2.getActorName())
                .build();
    }

    public static PairResponseDto.PairListResDto pairListResDto(List<Pair> pairs) {
        List<PairResponseDto.SimplePairResDto> pairResDtos = pairs.stream()
                .map(PairConverter::simplePairResDto)
                .collect(Collectors.toList());

        return PairResponseDto.PairListResDto.builder()
                .pairList(pairResDtos)
                .build();
    }
}
