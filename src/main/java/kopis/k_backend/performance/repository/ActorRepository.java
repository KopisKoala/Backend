package kopis.k_backend.performance.repository;

import kopis.k_backend.performance.domain.Actor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActorRepository extends JpaRepository<Actor, Long> {
    Optional<Actor> findByActorNameAndActorProfile(String actorName, String actorProfile);
    Page<Actor> findByActorNameContaining(String name, Pageable pageable);
}