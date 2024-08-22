package kopis.k_backend.pair.repository;

import kopis.k_backend.pair.domain.Pair;
import kopis.k_backend.performance.domain.Performance;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PairRepository extends JpaRepository<Pair, Long> {
    Optional<Pair> findById(Long id);

    List<Pair> findAllByPerformance(Performance performance);

    @Query("SELECT p FROM Pair p ORDER BY " +
            "p.reviewCount DESC, " +
            "p.ratingAverage DESC ")
    List<Pair> findPopularPairs(Pageable pageable);
}
