package kopis.k_backend.performance.repository;

import kopis.k_backend.performance.domain.PerformanceActor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PerformanceActorRepository extends JpaRepository<PerformanceActor, Long> {
    List<PerformanceActor> findByPerformanceId(Long performanceId);
}
