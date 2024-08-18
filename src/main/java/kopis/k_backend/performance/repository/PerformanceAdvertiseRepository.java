package kopis.k_backend.performance.repository;

import kopis.k_backend.performance.domain.PerformanceAdvertise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerformanceAdvertiseRepository extends JpaRepository<PerformanceAdvertise, Long> {
    List<PerformanceAdvertise> findAllByDate(String date);
}
