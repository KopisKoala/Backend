package kopis.k_backend.user.domain;

import jakarta.persistence.*;
import kopis.k_backend.global.entity.BaseEntity;
import lombok.*;

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

    private String membershipLevel;

}
