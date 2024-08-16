package kopis.k_backend.performance.converter;

import kopis.k_backend.performance.domain.Actor;
import kopis.k_backend.performance.dto.ActorResponseDto;
import kopis.k_backend.performance.dto.ActorResponseDto.ActorListResDto;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.stream.Collectors;

@NoArgsConstructor
public class ActorConverter {
    public static ActorListResDto actorListResDto (Page<Actor> actorPage) {
        return ActorListResDto.builder()
                .actorCount(actorPage.getTotalElements())
                .actorList(actorPage.getContent().stream()
                        .map(actor -> new ActorResponseDto.ActorResDto(
                                actor.getId(),
                                actor.getActorName(),
                                actor.getActorProfile()
                        ))
                        .collect(Collectors.toList())
                )
                .build();
    }
}
