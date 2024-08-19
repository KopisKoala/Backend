package kopis.k_backend.performance.service;

import jakarta.transaction.Transactional;
import kopis.k_backend.global.api_payload.ErrorCode;
import kopis.k_backend.global.exception.GeneralException;
import kopis.k_backend.performance.domain.Actor;
import kopis.k_backend.performance.repository.ActorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActorService {
    private final ActorRepository actorRepository;
    @Transactional
    public Actor findById(Long id){
        return actorRepository.findById(id)
                .orElseThrow(() -> GeneralException.of(ErrorCode.ACTOR_NOT_FOUND));
    }
}
