package kopis.k_backend.review.domain;

import jakarta.persistence.*;
import kopis.k_backend.global.entity.BaseEntity;
import kopis.k_backend.pair.domain.Pair;
import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.user.domain.User;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "review")
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 20, nullable = false)
    private String writer; // username

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id")
    private Performance performance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pair_id")
    private Pair pair;

    @Column(length = 1000)
    private String content;

    @Column(length = 7)
    private String hashtag;

    @Column
    private Long likeCount = 0L;

    @Column
    private Integer performanceRatings = 5;

    @Column
    private Integer pairRatings = 5;

    @OneToMany(mappedBy = "review")
    private List<ReviewLike> reviewLikes = new ArrayList<>();

}