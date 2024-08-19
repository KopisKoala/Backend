package kopis.k_backend.performance.repository;

import kopis.k_backend.performance.domain.PerformanceAttract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerformanceAttractRepository extends JpaRepository<PerformanceAttract, Long> {
    List<PerformanceAttract> findAllByDate(String date);
}
