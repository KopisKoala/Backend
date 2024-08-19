package kopis.k_backend.performance.service;

import jakarta.transaction.Transactional;
import kopis.k_backend.performance.converter.FavoriteActorConverter;
import kopis.k_backend.performance.domain.Actor;
import kopis.k_backend.performance.domain.FavoriteActor;
import kopis.k_backend.performance.repository.FavoriteActorRepository;
import kopis.k_backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteActorService {
    private final FavoriteActorRepository favoriteActorRepository;

    @Transactional
    public FavoriteActor create(User user, Actor actor) {

        // 이미 유저의 favoriteActors에 해당 배우가 있는지 확인
        List<FavoriteActor> favoriteActors = user.getFavoriteActors();

        for (FavoriteActor favoriteActor : favoriteActors) {
            if (favoriteActor.getActor().equals(actor)) {
                throw new IllegalArgumentException("이미 해당 배우를 찜하였습니다.");
            }
        }

        FavoriteActor favoriteActor = FavoriteActorConverter.saveFavoriteActor(user, actor);
        favoriteActorRepository.save(favoriteActor);

        return favoriteActor;
    }

    @Transactional
    public void delete(User user, Actor actor) {

        boolean favoriteActorNotFound = true;

        List<FavoriteActor> favoriteActors = user.getFavoriteActors();

        for (FavoriteActor favoriteActor : favoriteActors) {
            if (favoriteActor.getActor().equals(actor)) {
                favoriteActorRepository.deleteById(favoriteActor.getId());
                favoriteActorNotFound = false;
                // favoriteActorRepository.save(favoriteActor);
                break;
            }
        }

        // 해당 배우가 찜이 되어 있지 않은 경우
        if(favoriteActorNotFound) throw new IllegalArgumentException("찜이 되어 있지 않은 배우입니다.");

    }
}
