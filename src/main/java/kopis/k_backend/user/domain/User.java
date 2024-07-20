package kopis.k_backend.user.domain;

import jakarta.persistence.*;
import kopis.k_backend.global.entity.BaseEntity;
import kopis.k_backend.review.domain.Review;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    private String nickname;

    private String email;

    private String provider;

    private String address;

    @Enumerated(EnumType.STRING)
    @Column(length = 5, nullable = false)
    private UserRank userRank = UserRank.B; // rank는 예약어. 기본 값을 Rank.B로 설정

    @OneToMany(mappedBy = "user")
    private List<Review> reviews = new ArrayList<>();
    private String profileImage;


    // Getter와 Setter 추가
    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
