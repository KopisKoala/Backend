package kopis.k_backend.performance.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "actor")
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String actorName;

    private String actorProfile;

    @OneToMany(mappedBy = "actor")
    private List<FavoriteActor> favoriteActors = new ArrayList<>();
}
