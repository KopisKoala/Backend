package kopis.k_backend.user.repository;

import kopis.k_backend.user.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken); //reissue
    boolean existsById(String username);
    void deleteById(String username);
}
