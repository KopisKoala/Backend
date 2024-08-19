package kopis.k_backend.performance.converter;

import kopis.k_backend.performance.domain.Actor;
import kopis.k_backend.performance.domain.FavoriteActor;
import kopis.k_backend.performance.dto.FavoriteActorResponseDto.FavoriteActorResDto;
import kopis.k_backend.performance.dto.FavoriteActorResponseDto.FavoriteActorListResDto;
import kopis.k_backend.user.domain.User;
import lombok.NoArgsConstructor;
import java.util.List;

@NoArgsConstructor
public class FavoriteActorConverter {
    public static FavoriteActor saveFavoriteActor(User user, Actor actor) {
        return FavoriteActor.builder()
                .actor(actor)
                .user(user)
                .build();
    }

    public static FavoriteActorResDto simpleFavoriteActorResDto(FavoriteActor favoriteActor) {
        return FavoriteActorResDto.builder()
                .Id(favoriteActor.getId())
                .actorId(favoriteActor.getActor().getId())
                .actorName(favoriteActor.getActor().getActorName())
                .actorProfile(favoriteActor.getActor().getActorProfile())
                .build();
    }

    public static FavoriteActorListResDto favoriteActorListResDto(List<FavoriteActor> favoriteActorList, Long favoriteActorCount) {
        List<FavoriteActorResDto> favoriteActorResDtoList = favoriteActorList.stream()
                .map(FavoriteActorConverter::simpleFavoriteActorResDto)
                .toList();

        return FavoriteActorListResDto.builder()
                .favoriteActorCount(favoriteActorCount)
                .favoriteActorResDtoList(favoriteActorResDtoList)
                .build();
    }
}
