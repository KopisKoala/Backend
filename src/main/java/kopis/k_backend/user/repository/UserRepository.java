package kopis.k_backend.user.repository;

import kopis.k_backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 1. 사용자 계정이름으로 사용자 정보를 회수하는 기능
    Optional<User> findByUsername(String username);
    // 2. 사용자 이메일으로 사용자 정보를 회수하는 기능
    Optional<User> findByEmail(String email);
    // 3. 사용자 계정이름을 가진 사용자 정보가 존재하는지 판단하는 기능
    boolean existsByUsername(String username);
    // 4. 사용자 이메일을 가진 사용자 정보가 존재하는지 판단하는 기능
    boolean existsByEmail(String email);
    // 5. 닉네임이 사용중인지 판단하는 기능
    boolean existsByNickname(String nickname);
}