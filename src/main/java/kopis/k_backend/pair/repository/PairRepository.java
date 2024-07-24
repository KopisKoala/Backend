package kopis.k_backend.pair.repository;

import kopis.k_backend.pair.domain.Pair;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PairRepository extends JpaRepository<Pair, Long> {
    Optional<Pair> findById(Long id);
}
