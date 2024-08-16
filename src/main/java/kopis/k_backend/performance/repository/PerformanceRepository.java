package kopis.k_backend.performance.repository;

import kopis.k_backend.performance.domain.Performance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    Optional<Performance> findByKopisPerfId(String kopisPerfId);

    @Query("SELECT h.kopisPerfId FROM Performance h WHERE h.kopisPerfId IS NOT NULL")
    List<String> findAllKopisPerfIds();

    List<Performance> findByState(String state);

    Page<Performance> findByTitleContaining(String title, Pageable pageable);
}
