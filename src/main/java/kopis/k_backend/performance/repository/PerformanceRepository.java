package kopis.k_backend.performance.repository;

import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.performance.domain.PerformanceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    Optional<Performance> findByKopisPerfId(String kopisPerfId);

    @Query("SELECT p FROM Performance p JOIN FETCH p.hall WHERE p.id = :id")
    Optional<Performance> findByIdWithHall(@Param("id") Long id);

    List<Performance> findByState(String state);

    Slice<Performance> findByTitleContaining(String title, Pageable pageable);

    @Query("SELECT p FROM Performance p WHERE p.reviewCount > :minReviews AND p.state != '공연완료' ORDER BY p.ratingAverage DESC, p.reviewCount ASC")
    List<Performance> findTop10ByMinReviewsOrderByRatingAndReviewCount(@Param("minReviews") Long minReviews);

    // 공연의 시작날짜가 주어진 endDate보다 작거나 같음 & 공연의 종료 날짜가 주어진 startDate보다 크거나 같음
    // 주어진 가격의 최솟값이 공연의 최댓값보다 작거가 같음 & 주어진 가격의 최댓값이 공연의 최솟값보다 크거나 같음
    @Query(value = "SELECT p.* FROM performance p " +
            "JOIN hall h ON p.hall_id = h.id " +
            "WHERE p.performance_type = :performanceType " +
            "AND p.start_date <= :endDate " +
            "AND p.end_date >= :startDate " +
            "AND h.sidonm = :location " +
            "AND CAST(p.lowest_price AS UNSIGNED) <= :maxPrice " +
            "AND CAST(p.highest_price AS UNSIGNED) >= :minPrice", nativeQuery = true)
    List<Performance> findPerformancesByCriteria(
            @Param("performanceType") PerformanceType performanceType,
            @Param("endDate") String endDate,
            @Param("startDate") String startDate,
            @Param("location") String location,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice
    );

    //  찜한 배우의 공연 반환
    @Query("SELECT p FROM Performance p " +
            "JOIN p.performanceActors pa " +
            "JOIN pa.actor a " +
            "JOIN a.favoriteActors fa " +
            "WHERE fa.user.id = :userId AND (p.state = '공연중' OR p.state = '공연예정')")
    List<Performance> findPerformancesByFavoriteActorsAndState(@Param("userId") Long userId);
}
