package kopis.k_backend.review.repository;

import jakarta.persistence.LockModeType;
import kopis.k_backend.review.domain.Review;
import kopis.k_backend.review.domain.ReviewLike;
import kopis.k_backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ReviewLike>findByUserAndReview(User user, Review review);

}
