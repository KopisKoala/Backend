package kopis.k_backend.performance.converter;

import kopis.k_backend.performance.domain.Actor;
import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.performance.domain.PerformanceActor;
import kopis.k_backend.performance.dto.ActorResponseDto.PerformanceDetailActorListResDto;
import kopis.k_backend.performance.dto.ActorResponseDto.PerformanceDetailActorResDto;
import kopis.k_backend.performance.dto.ActorResponseDto.HomeSearchActorResDto;
import kopis.k_backend.performance.dto.ActorResponseDto.HomeSearchActorListResDto;
import kopis.k_backend.user.domain.User;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class ActorConverter {
    public static HomeSearchActorListResDto homeSearchActorListResDto (Slice<Actor> actorSlice, User user) {

        return HomeSearchActorListResDto.builder()
                .actorCount(actorSlice.stream().count())
                .actorList(actorSlice.getContent().stream()
                        .map(actor -> {
                            boolean isFavorite = user.getFavoriteActors().stream()
                                    .anyMatch(favoriteActor -> favoriteActor.getActor().equals(actor));

                            String isFavoriteActor = isFavorite ? "Y" : "N";
                            return new HomeSearchActorResDto(
                                    actor.getId(),
                                    actor.getActorName(),
                                    actor.getActorProfile(),
                                    isFavoriteActor
                            );
                        })
                        .collect(Collectors.toList())
                )
                .build();
    }

    public static PerformanceDetailActorResDto performanceDetailActorResDto(User user, PerformanceActor performanceActor) {

        boolean isFavorite = user.getFavoriteActors().stream()
                .anyMatch(favoriteActor -> favoriteActor.getActor().equals(performanceActor.getActor()));

        String isFavoriteActor = isFavorite ? "Y" : "N";

        return PerformanceDetailActorResDto.builder()
                .id(performanceActor.getActor().getId())
                .actorName(performanceActor.getActor().getActorName())
                .actorProfile(performanceActor.getActor().getActorProfile())
                .isFavoriteActor(isFavoriteActor)
                .characterName(performanceActor.getCharacterName())
                .build();
    }

    public static PerformanceDetailActorListResDto performanceDetailActorListResDto (User user, Performance performance) {

        List<PerformanceDetailActorResDto> performanceDetailActorResDtoList = performance.getPerformanceActors().stream()
                .map(performanceActor -> performanceDetailActorResDto(user, performanceActor))
                .collect(Collectors.toList());

        return PerformanceDetailActorListResDto.builder()
                .actorList(performanceDetailActorResDtoList)
                .build();

    }
}
