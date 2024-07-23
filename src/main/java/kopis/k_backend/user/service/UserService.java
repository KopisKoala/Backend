package kopis.k_backend.user.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import kopis.k_backend.global.api_payload.ErrorCode;
import kopis.k_backend.global.exception.GeneralException;
import kopis.k_backend.global.s3.AmazonS3Manager;
import kopis.k_backend.user.converter.UserConverter;
import kopis.k_backend.user.domain.RefreshToken;
import kopis.k_backend.user.domain.User;
import kopis.k_backend.user.dto.JwtDto;
import kopis.k_backend.user.dto.UserRequestDto;
import kopis.k_backend.user.dto.UserResponseDto;
import kopis.k_backend.user.jwt.JwtTokenUtils;
import kopis.k_backend.user.repository.RefreshTokenRepository;
import kopis.k_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static kopis.k_backend.global.api_payload.ErrorCode.BAD_REQUEST;
import static org.apache.logging.log4j.util.Strings.isEmpty;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JpaUserDetailsManager manager;
    private final JwtTokenUtils jwtTokenUtils;
    private final AmazonS3Manager amazonS3Manager;

    public User findByUserName(String userName){
        return userRepository.findByUsername(userName)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND_BY_USERNAME));
    }

    public Boolean checkMemberByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public User createUser(UserRequestDto.UserReqDto userReqDto) {
        // 새로운 사용자 생성
        User newUser = userRepository.save(UserConverter.saveUser(userReqDto));

        // 새로운 사용자 정보를 반환하기 전에 저장된 UserDetails를 다시 로드하여 동기화 시도
        manager.loadUserByUsername(userReqDto.getUsername());

        return newUser;
    }

    @Transactional
    public void saveNickname(UserRequestDto.UserNicknameReqDto nicknameReqDto, User user) {
        // 입력된 닉네임
        String nickname = nicknameReqDto.getNickname();

        // 중복 검사
        if (userRepository.existsByNickname(nickname)) {
            throw GeneralException.of(ErrorCode.ALREADY_USED_NICKNAME);
        }

        // 중복이 없는 경우 닉네임 저장
        user.setNickname(nickname);
        userRepository.save(user);
    }

    @Transactional
    public JwtDto jwtMakeSave(String username){

        // JWT 생성 - access & refresh
        UserDetails details
                = manager.loadUserByUsername(username);

        JwtDto jwt = jwtTokenUtils.generateToken(details); //2. access, refresh token 생성 및 발급
        log.info("accessToken: {}", jwt.getAccessToken());
        log.info("refreshToken: {} ", jwt.getRefreshToken());

        // 유효기간 초단위 설정 후 db에 refresh token save
        Claims refreshTokenClaims = jwtTokenUtils.parseClaims(jwt.getRefreshToken());
        Long validPeriod
                = refreshTokenClaims.getExpiration().toInstant().getEpochSecond()
                - refreshTokenClaims.getIssuedAt().toInstant().getEpochSecond();

        // DB에 저장된 해당 사용자의 리프레시 토큰을 업데이트
        Optional<RefreshToken> existingToken = refreshTokenRepository.findById(username);
        if (existingToken.isPresent()) {
            refreshTokenRepository.deleteById(username);
        }

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .id(username)
                        .ttl(validPeriod)
                        .refreshToken(jwt.getRefreshToken())
                        .build()
        );

        // JSON 형태로 응답
        return jwt;
    }

    @Transactional
    public void logout(HttpServletRequest request) {
        // 1. access token 찾아오기
        String accessToken = request.getHeader("Authorization").split(" ")[1];

        // 2. 리프레시 토큰을 username으로 찾아 삭제
        String username = jwtTokenUtils.parseClaims(accessToken).getSubject();
        log.info("access token에서 추출한 username : {}", username);
        if (refreshTokenRepository.existsById(username)) {
            refreshTokenRepository.deleteById(username);
            log.info("DB에서 리프레시 토큰 삭제 완료");
        } else {
            throw GeneralException.of(ErrorCode.WRONG_REFRESH_TOKEN);
        }
    }

    // access, refresh 토큰 재발급
    @Transactional
    public JwtDto reissue(HttpServletRequest request) {
        // 1. Request에서 Refresh Token 추출
        String refreshTokenValue = request.getHeader("Authorization").split(" ")[1];

        // 2. DB에서 해당 Refresh Token을 찾음
        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(refreshTokenValue)
                .orElseThrow(() -> new GeneralException(ErrorCode.WRONG_REFRESH_TOKEN));
        log.info("찾은 refresh token : {}", refreshToken);

        // 3. Refresh Token의 유효기간 확인 (생략)

        // 4. Refresh Token을 발급한 사용자 정보 로드
        UserDetails userDetails = manager.loadUserByUsername(refreshToken.getId());
        log.info("refresh token에서 추출한 username : {}", refreshToken.getId());

        // 5. 새로운 Access Token 및 Refresh Token 생성
        JwtDto jwt = jwtTokenUtils.generateToken(userDetails);
        log.info("reissue: refresh token 재발급 완료");

        // 6. Refresh Token 정보 업데이트 및 DB에 저장
        refreshToken.updateRefreshToken(jwt.getRefreshToken());
        Claims refreshTokenClaims = jwtTokenUtils.parseClaims(jwt.getRefreshToken());
        Long validPeriod = refreshTokenClaims.getExpiration().toInstant().getEpochSecond()
                - refreshTokenClaims.getIssuedAt().toInstant().getEpochSecond();
        refreshToken.updateTtl(validPeriod);
        refreshTokenRepository.save(refreshToken);
        log.info("accessToken: {}", jwt.getAccessToken());
        log.info("refreshToken: {} ", jwt.getRefreshToken());

        // 7. DB에 새로운 리프레시 토큰이 정상적으로 저장되었는지 확인
        if (!refreshTokenRepository.existsById(refreshToken.getId())) {
            throw GeneralException.of(ErrorCode.WRONG_REFRESH_TOKEN);
        }

        return jwt;
    }

    @Transactional
    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        if (refreshTokenRepository.existsById(username)) {
            refreshTokenRepository.deleteById(username);
            log.info("DB에서 리프레시 토큰 삭제 완료");
        }
        userRepository.delete(user);
        log.info("{} 회원 탈퇴 완료", username);
    }


    @Transactional
    public void updateProfileImage(MultipartFile file, User user) throws IOException {
        String uploadFileUrl = null;
        String dirName = "profile/";

        if (file != null) {
            String contentType = file.getContentType();
            if (ObjectUtils.isEmpty(contentType)) {
                throw GeneralException.of(ErrorCode.INVALID_FILE_CONTENT_TYPE);
            }

            MediaType mediaType = amazonS3Manager.contentType(Objects.requireNonNull(file.getOriginalFilename()));
            if (mediaType == null || !(mediaType.equals(MediaType.IMAGE_PNG) || mediaType.equals(MediaType.IMAGE_JPEG))) {
                throw GeneralException.of(ErrorCode.MISMATCH_IMAGE_FILE);
            }

            // 이전 프로필 이미지가 존재하는지 확인
            if (!isEmpty(user.getProfileImage())) {
                // 기존 프로필 이미지를 S3에서 삭제
                String previousFilePath = user.getProfileImage();
                amazonS3Manager.delete(previousFilePath); // S3에서 삭제
            }

            java.io.File uploadFile = amazonS3Manager.convert(file)
                    .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));

            String fileName = dirName + AmazonS3Manager.generateFileName(file);
            uploadFileUrl = amazonS3Manager.putS3(uploadFile, fileName);

            user.setProfileImage(uploadFileUrl); // 새로운 사진 url 저장
            userRepository.save(user);
        }
    }

    // 사용자의 주소 정보를 저장합니다.
    @Transactional
    public void updateAddress(User user, UserResponseDto.UserAddressDto userUpdateAddressDto) {
        if (userUpdateAddressDto.getAddress() == null || userUpdateAddressDto.getAddress().isEmpty()) {
            throw GeneralException.of(BAD_REQUEST);
        }
        user.setAddress(userUpdateAddressDto.getAddress());
        userRepository.save(user);
    }
}
