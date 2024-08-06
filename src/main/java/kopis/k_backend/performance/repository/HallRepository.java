package kopis.k_backend.performance.repository;

import kopis.k_backend.performance.domain.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface HallRepository extends JpaRepository<Hall, Long> {
    Optional<Hall> findByKopisHallId(String kopisHallId);

    @Query("SELECT h.kopisHallId FROM Hall h WHERE h.kopisHallId IS NOT NULL")
    List<String> findAllKopisHallIds();

}
