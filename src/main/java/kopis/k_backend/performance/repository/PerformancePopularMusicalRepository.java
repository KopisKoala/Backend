package kopis.k_backend.performance.repository;

import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.performance.domain.PerformancePopularMusical;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PerformancePopularMusicalRepository extends JpaRepository<PerformancePopularMusical, Long> {

    List<PerformancePopularMusical> findAllByDate(String date);
}
