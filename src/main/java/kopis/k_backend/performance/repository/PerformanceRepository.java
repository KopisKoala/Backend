package kopis.k_backend.performance.repository;

import kopis.k_backend.performance.domain.Performance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    Optional<Performance> findByKopisPerfId(String kopisPerfId);

    @Query("SELECT p FROM Performance p JOIN FETCH p.hall WHERE p.id = :id")
    Optional<Performance> findByIdWithHall(@Param("id") Long id);

    List<Performance> findByState(String state);

    Page<Performance> findByTitleContaining(String title, Pageable pageable);
}
