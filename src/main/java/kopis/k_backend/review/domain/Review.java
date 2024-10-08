package kopis.k_backend.review.domain;

import jakarta.persistence.*;
import kopis.k_backend.global.entity.BaseEntity;
import kopis.k_backend.pair.domain.Pair;
import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.user.domain.User;
import lombok.*;

import java.time.LocalDate;
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
    private User writer;

    @Column(nullable = false)
    private String writerName; // username

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

    @Column
    private LocalDate performanceDate;

    @Column
    private ViewingPartner viewingPartner = ViewingPartner.NULL;

    @Column(length = 1000)
    private String memo;

    @OneToMany(mappedBy = "review")
    private List<ReviewLike> reviewLikes = new ArrayList<>();

    public Long increaseLikeCount(){
        this.likeCount += 1;
        return this.likeCount;
    }

    public Long decreaseLikeCount(){
        this.likeCount -= 1;
        return this.likeCount;
    }

    public ViewingPartner updateViewingPartner(Integer partnerNumber) {
        if (partnerNumber == 0) {
            viewingPartner = ViewingPartner.NULL;
        }
        else if (partnerNumber == 1) {
            viewingPartner = ViewingPartner.FAMILY;
        }
        else if (partnerNumber == 2) {
            viewingPartner = ViewingPartner.FRIEND;
        }
        else if (partnerNumber == 3) {
            viewingPartner = ViewingPartner.COUPLE;
        }
        else if(partnerNumber == 4) {
            viewingPartner = ViewingPartner.ALONE;
        }
        else {
            throw new IllegalArgumentException("Invalid partner number: " + partnerNumber);
        }

        return viewingPartner;
    }

    public String updateMemo(String memo) {
        this.memo = memo;
        return this.memo;
    }
}