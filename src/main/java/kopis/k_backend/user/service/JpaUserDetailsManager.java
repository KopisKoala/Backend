package kopis.k_backend.user.service;

import kopis.k_backend.global.api_payload.ErrorCode;
import kopis.k_backend.global.exception.GeneralException;
import kopis.k_backend.user.domain.User;
import kopis.k_backend.user.jwt.CustomUserDetails;
import kopis.k_backend.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class JpaUserDetailsManager implements UserDetailsManager {
    // UserDetailsManager의 구현체로 만들면, Spring Security Filter에서 사용자 정보 회수에 활용할 수 있음
    private final UserRepository userRepository;

    public JpaUserDetailsManager(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @Override
    // 실제로 Spring Security 내부에서 사용하는 반드시 구현해야 정상동작을 기대할 수 있는 메소드
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userRepository.findByUsername(username).orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));;
        return CustomUserDetails.fromEntity(user);
    }

    @Override
    // 새로운 사용자를 저장하는 메소드
    public void createUser(UserDetails user) {
        log.info("try create user: {}", user.getUsername());
        // 사용자가 (이미) 있으면 생성할수 없다.
        if (this.userExists(user.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        try {
            userRepository.save(
                    ((CustomUserDetails) user).newEntity());
        } catch (ClassCastException e) {
            log.error("failed to cast to {}", CustomUserDetails.class);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    // 계정이름을 가진 사용자가 존재하는지 확인하는 메소드
    public boolean userExists(String username) {
        log.info("check if user: {} exists", username);
        return this.userRepository.existsByUsername(username);
    }

    @Override
    public void updateUser(UserDetails user) {
    }
    @Override
    public void deleteUser(String username) {
    }
    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }
}
