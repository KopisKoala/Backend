package kopis.k_backend.pair.repository;

import kopis.k_backend.pair.domain.Pair;
import kopis.k_backend.performance.domain.Performance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PairRepository extends JpaRepository<Pair, Long> {
    Optional<Pair> findById(Long id);

    List<Pair> findAllByPerformance(Performance performance);
}
