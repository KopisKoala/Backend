package kopis.k_backend.performance.repository;

import kopis.k_backend.performance.domain.FavoriteActor;
import kopis.k_backend.user.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteActorRepository extends JpaRepository<FavoriteActor, Long> {
    Slice<FavoriteActor> findFavoriteActorByUser(User user, Pageable pageable);
}
