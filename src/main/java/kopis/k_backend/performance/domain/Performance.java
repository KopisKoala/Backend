package kopis.k_backend.performance.domain;

import jakarta.persistence.*;
import kopis.k_backend.pair.domain.Pair;
import kopis.k_backend.review.domain.Review;
import lombok.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
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

    @Column
    private String genere;

    @Column
    private String cast;

    @Column(unique = true)
    private String kopisPerfId;

    @Column(nullable = false)
    private PerformanceType performanceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id")
    private Hall hall;

    @Column(nullable = false)
    private String startDate;

    @Column(nullable = false)
    private String endDate;

    @Column
    private String poster;

    @Column
    private String ticketingLink;

    @Column
    private String state;

    @Column(nullable = false)
    private String duration;

    @Column(nullable = false)
    private String lowestPrice;

    @Column(nullable = false)
    private String highestPrice;

    @Column(nullable = false)
    private String price;

    @Column(nullable = false)
    private Double ratingAverage = 0.0;

    @Column(nullable = false)
    private Long reviewCount = 0L;

    @Column(length = 7)
    private String hashtag1;

    @Column(length = 7)
    private String hashtag2;

    @Column(length = 7)
    private String hashtag3;

    @Column
    private String reviewSummary;

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
        this.hashtag1 = !topHashtags.isEmpty() ? topHashtags.get(0) : null;
        this.hashtag2 = topHashtags.size() > 1 ? topHashtags.get(1) : null;
        this.hashtag3 = topHashtags.size() > 2 ? topHashtags.get(2) : null;
    }

    public void updateReviewSummary(String reviewSummary) {
        this.reviewSummary = reviewSummary;
    }

}
