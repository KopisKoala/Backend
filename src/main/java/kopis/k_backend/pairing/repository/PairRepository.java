package kopis.k_backend.pairing.repository;

import kopis.k_backend.pairing.domain.Pair;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PairRepository extends JpaRepository<Pair, Long> {
}
