package kopis.k_backend.performance.repository;

import kopis.k_backend.performance.domain.PerformancePopularPlay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerformancePopularPlayRepository extends JpaRepository<PerformancePopularPlay, Long> {
    List<PerformancePopularPlay> findAllByDate(String date);
}
