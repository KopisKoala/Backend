package kopis.k_backend.pair.domain;

import jakarta.persistence.*;
import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.review.domain.Review;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "pair")
public class Pair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id")
    private Performance performance;

    @Column(nullable = false)
    private String actor1Name;

    @Column(nullable = false)
    private String actor2Name;

    @Column(length = 7)
    private String hashtag1;

    @Column(length = 7)
    private String hashtag2;

    @Column(length = 7)
    private String hashtag3;

    private Double ratingAverage = 0D;

    private Long reviewCount = 0L;

    @OneToMany(mappedBy = "pair")
    private List<Review> reviews = new ArrayList<>();

    public void increaseReviewCount(Long pairId){
        this.id = pairId;
        this.reviewCount += 1;
    }

    public void decreaseReviewCount(Long pairId){
        this.id = pairId;
        this.reviewCount -= 1;
    }

}
