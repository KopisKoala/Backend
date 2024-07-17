package kopis.k_backend.performance.repository;

import kopis.k_backend.performance.domain.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepository extends JpaRepository<Actor, Long> {

}