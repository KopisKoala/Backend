package kopis.k_backend.performance.converter;

import kopis.k_backend.performance.domain.Actor;
import kopis.k_backend.performance.domain.FavoriteActor;
import kopis.k_backend.user.domain.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FavoriteActorConverter {
    public static FavoriteActor saveFavoriteActor(User user, Actor actor) {
        return FavoriteActor.builder()
                .actor(actor)
                .user(user)
                .build();
    }
}
