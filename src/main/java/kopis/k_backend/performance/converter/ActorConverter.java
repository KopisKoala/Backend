package kopis.k_backend.performance.converter;

import kopis.k_backend.performance.domain.Actor;
import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.performance.dto.ActorResponseDto.PerformanceDetailActorListResDto;
import kopis.k_backend.performance.dto.ActorResponseDto.PerformanceDetailActorResDto;
import kopis.k_backend.performance.dto.ActorResponseDto.HomeSearchActorResDto;
import kopis.k_backend.performance.dto.ActorResponseDto.HomeSearchActorListResDto;
import kopis.k_backend.user.domain.User;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class ActorConverter {
    public static HomeSearchActorListResDto homeSearchActorListResDto (Page<Actor> actorPage) {
        return HomeSearchActorListResDto.builder()
                .actorCount(actorPage.getTotalElements())
                .actorList(actorPage.getContent().stream()
                        .map(actor -> new HomeSearchActorResDto(
                                actor.getId(),
                                actor.getActorName(),
                                actor.getActorProfile()
                        ))
                        .collect(Collectors.toList())
                )
                .build();
    }

    public static PerformanceDetailActorResDto performanceDetailActorResDto(User user, Actor actor) {

        boolean isFavorite = user.getFavoriteActors().stream()
                .anyMatch(favoriteActor -> favoriteActor.getActor().equals(actor));

        String isFavoriteActor = isFavorite ? "Y" : "N";

        return PerformanceDetailActorResDto.builder()
                .id(actor.getId())
                .actorName(actor.getActorName())
                .actorProfile(actor.getActorProfile())
                .isFavoriteActor(isFavoriteActor)
                .build();
    }

    public static PerformanceDetailActorListResDto performanceDetailActorListResDto (User user, Performance performance) {

        List<PerformanceDetailActorResDto> performanceDetailActorResDtoList = performance.getPerformanceActors().stream()
                .map(performanceActor -> performanceDetailActorResDto(user, performanceActor.getActor()))
                .collect(Collectors.toList());

        return PerformanceDetailActorListResDto.builder()
                .actorList(performanceDetailActorResDtoList)
                .build();

    }
}
