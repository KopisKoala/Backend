package kopis.k_backend.performance.domain;

import jakarta.persistence.*;
import kopis.k_backend.pairing.domain.Pair;
import kopis.k_backend.review.domain.Review;
import lombok.*;
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

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String streetAddress;

    @Column(nullable = false)
    private String hallName;

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

    private Long likeCount = 0L;

    private Long reviewCount = 0L;

    private String ticketingLink;

    @OneToMany(mappedBy = "performance")
    private List<Pair> pairs = new ArrayList<>();

    @OneToMany(mappedBy = "performance")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "performance")
    private List<PerformanceActor> performanceActors = new ArrayList<>();
}
