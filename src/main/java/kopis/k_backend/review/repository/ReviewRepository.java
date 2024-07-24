package kopis.k_backend.review.repository;

import kopis.k_backend.pair.domain.Pair;
import kopis.k_backend.performance.domain.Performance;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import kopis.k_backend.review.domain.Review;


@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r WHERE (r.performance = :performance) ORDER BY " +
            "CASE WHEN :way = 'recent' THEN r.createdAt END DESC, " +
            "CASE WHEN :way = 'like' THEN r.likeCount END DESC, " +
            "CASE WHEN :way = 'like' THEN r.createdAt END DESC, " +
            "CASE WHEN :way = 'desc' THEN r.performanceRatings END DESC, " +
            "CASE WHEN :way = 'desc' THEN r.likeCount END DESC, " +
            "CASE WHEN :way = 'asc' THEN r.performanceRatings END ASC, " +
            "CASE WHEN :way = 'asc' THEN r.likeCount END DESC")
    Slice<Review> findReviewByPerformanceAndWay(@Param("performance")Performance performance, @Param("way") String way, Pageable pageable);

    @Query("SELECT r FROM Review r WHERE (r.pair = :pair) ORDER BY " +
            "CASE WHEN :way = 'recent' THEN r.createdAt END DESC, " +
            "CASE WHEN :way = 'like' THEN r.likeCount END DESC, " +
            "CASE WHEN :way = 'like' THEN r.createdAt END DESC, " +
            "CASE WHEN :way = 'desc' THEN r.performanceRatings END DESC, " +
            "CASE WHEN :way = 'desc' THEN r.likeCount END DESC, " +
            "CASE WHEN :way = 'asc' THEN r.performanceRatings END ASC, " +
            "CASE WHEN :way = 'asc' THEN r.likeCount END DESC")
    Slice<Review> findReviewByPairAndWay(@Param("pair") Pair pair, @Param("way") String way, Pageable pageable);

}
