package kopis.k_backend.user.domain;

import jakarta.persistence.*;
import kopis.k_backend.global.entity.BaseEntity;
import kopis.k_backend.review.domain.Review;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "user")
public class User extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String provider;

    private Long reviewCount = 0L;

    private String address;

    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private UserRank userRank = UserRank.A; // rank는 예약어. 기본 값을 Rank.A로 설정

    @OneToMany(mappedBy = "user")
    private List<Review> reviews = new ArrayList<>();

    public User(String username, String nickname, String email, String provider) {
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.provider = provider;
    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    public void updateProfileImage(String profileImage){
        this.profileImage = profileImage;
    }

    public void updateAddress(String address) { this.address = address; }

    public void increaseReviewCount() {
        this.reviewCount += 1;
    }

    public void decreaseReviewCount() {
        this.reviewCount -= 1;
    }

    public void updateUserRank() {
        if (10 <= this.reviewCount && this.reviewCount < 30) {
            this.userRank = UserRank.Superior;
        }
        else if (30 <= this.reviewCount && this.reviewCount < 50) {
            this.userRank = UserRank.Royal;
        }
        else if (50 <= this.reviewCount) {
            this.userRank = UserRank.VIP;
        }
    }
}
