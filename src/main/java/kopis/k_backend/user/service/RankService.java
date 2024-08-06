package kopis.k_backend.user.service;

import jakarta.transaction.Transactional;
import kopis.k_backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RankService {
    @Transactional
    public void increaseReviewCount(User user) {
        user.increaseReviewCount();

        if (user.getReviews().size() == user.getReviewCount()) {
            user.updateUserRank();
        }
    }

    @Transactional
    public void decreaseReviewCount(User user) {
        user.decreaseReviewCount();

        if (user.getReviews().size() == user.getReviewCount()) {
            user.updateUserRank();
        }
    }
}
