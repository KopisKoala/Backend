package kopis.k_backend.performance.repository;

import kopis.k_backend.performance.domain.Performance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {

}
