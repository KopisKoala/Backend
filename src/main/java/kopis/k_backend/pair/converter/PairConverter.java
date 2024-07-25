package kopis.k_backend.pair.converter;

import kopis.k_backend.pair.domain.Pair;
import kopis.k_backend.pair.dto.PairResponseDto;
import kopis.k_backend.review.dto.ReviewResponseDto;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class PairConverter {

    public static PairResponseDto.SimplePairResDto simplePairResDto(Pair pair){
        return PairResponseDto.SimplePairResDto.builder()
                .pairId(pair.getId())
                .actor1(pair.getActor1Name())
                .actor2(pair.getActor2Name())
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
