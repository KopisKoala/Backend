package kopis.k_backend.performance.repository;

import kopis.k_backend.performance.domain.Hall;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HallRepository extends JpaRepository<Hall, Long> {
}
