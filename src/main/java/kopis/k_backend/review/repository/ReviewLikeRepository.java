package kopis.k_backend.review.repository;

import kopis.k_backend.review.domain.Review;
import kopis.k_backend.review.domain.ReviewLike;
import kopis.k_backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    Optional<ReviewLike>findByUserAndReview(User user, Review review);

}
