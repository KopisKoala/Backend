package kopis.k_backend.performance.domain;

import jakarta.persistence.*;
import kopis.k_backend.pair.domain.Pair;
import kopis.k_backend.review.domain.Review;
import lombok.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "performance")
public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private PerformanceType performanceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id")
    private Hall hall;

    @Column(length = 7)
    private String hashtag1;

    @Column(length = 7)
    private String hashtag2;

    @Column(length = 7)
    private String hashtag3;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Integer Duration;

    @Column(nullable = false)
    private Integer lowestPrice;

    @Column(nullable = false)
    private Integer highestPrice;

    private String poster;

    private Double ratingAverage = 0.0;

    private Long reviewCount = 0L;

    private String ticketingLink;

    @OneToMany(mappedBy = "performance")
    private List<Pair> pairs = new ArrayList<>();

    @OneToMany(mappedBy = "performance")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "performance")
    private List<PerformanceActor> performanceActors = new ArrayList<>();

    public void increaseReviewCount(Long performanceId){
        this.id = performanceId;
        this.reviewCount += 1;
    }

    public void decreaseReviewCount(Long performanceId){
        this.id = performanceId;
        this.reviewCount -= 1;
    }

    public void updateRatingAverage(Long sum) {
        if (this.reviewCount != 0) {
            BigDecimal average = new BigDecimal((double) sum / this.reviewCount);
            average = average.setScale(1, RoundingMode.HALF_UP); // 소수점 첫째 자리까지 반올림
            this.ratingAverage = average.doubleValue();
        } else {
            this.ratingAverage = 0.0;
        }
    }

    public void updateTopHashtags(List<String> topHashtags) {
        this.hashtag1 = topHashtags.size() > 0 ? topHashtags.get(0) : null;
        this.hashtag2 = topHashtags.size() > 1 ? topHashtags.get(1) : null;
        this.hashtag3 = topHashtags.size() > 2 ? topHashtags.get(2) : null;
    }
}
