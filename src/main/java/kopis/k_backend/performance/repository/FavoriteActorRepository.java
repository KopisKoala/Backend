package kopis.k_backend.performance.repository;

import kopis.k_backend.performance.domain.FavoriteActor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteActorRepository extends JpaRepository<FavoriteActor, Long> {

}
