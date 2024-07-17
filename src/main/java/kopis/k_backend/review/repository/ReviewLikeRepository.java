package kopis.k_backend.review.repository;

import kopis.k_backend.review.domain.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

}
