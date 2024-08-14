package kopis.k_backend.performance.repository;

import kopis.k_backend.performance.domain.Actor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepository extends JpaRepository<Actor, Long> {
    Page<Actor> findByActorNameContaining(String name, Pageable pageable);
}