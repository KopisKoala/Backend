package kopis.k_backend.pair.Service;

import kopis.k_backend.global.api_payload.ErrorCode;
import kopis.k_backend.global.exception.GeneralException;
import kopis.k_backend.pair.domain.Pair;
import kopis.k_backend.pair.repository.PairRepository;
import kopis.k_backend.performance.domain.Actor;
import kopis.k_backend.performance.domain.FavoriteActor;
import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.performance.repository.ActorRepository;
import kopis.k_backend.performance.service.PerformanceService;
import kopis.k_backend.review.domain.Review;
import kopis.k_backend.review.repository.ReviewRepository;
import kopis.k_backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PairService {
    private static final Logger logger = LoggerFactory.getLogger(PerformanceService.class);

    private final PairRepository pairRepository;
    private final ReviewRepository reviewRepository;
    private final ActorRepository actorRepository;

    public Pair findById(Long id) {
        return pairRepository.findById(id)
                .orElseThrow(() -> GeneralException.of(ErrorCode.PAIR_NOT_FOUND));
    }

    public Long getReviewCountById(Long id){
        Pair pair =  pairRepository.findById(id)
                .orElseThrow(() ->GeneralException.of(ErrorCode.PAIR_NOT_FOUND));

        return pair.getReviewCount();
    }

    public Double getAverageRatingById(Long id){
        Pair pair = pairRepository.findById(id)
                .orElseThrow(() ->GeneralException.of(ErrorCode.PAIR_NOT_FOUND));

        return pair.getRatingAverage();
    }

    public Pair getTopRatedPair(List<Pair> pairList) {
        return pairList.stream()
                .max(Comparator.comparing(Pair::getRatingAverage)
                    .thenComparing(Pair::getReviewCount))
                .orElse(null);
    }

    public Pair getFavoritePair(List<Pair> pairList, User user) {
        // 유저의 찜한 배우 리스트
        List<FavoriteActor> favoriteActors = user.getFavoriteActors();

        // 유저의 찜한 배우가 1명 있는 페어 리스트
        List<Pair> oneFavoriteActors = new ArrayList<>();

        // 유저의 찜한 배우가 2명 있는 페어 리스트
        List<Pair> twoFavoriteActors = new ArrayList<>();

        for (Pair pair : pairList) {
            int favoriteActorCount = 0;

            for (FavoriteActor favoriteActor : favoriteActors) {
                Long favoriteActorId = favoriteActor.getActor().getId();

                if (pair.getActor1().equals(favoriteActorId) || pair.getActor2().equals(favoriteActorId)) {
                    favoriteActorCount++;
                }
            }

            if (favoriteActorCount == 1) {
                oneFavoriteActors.add(pair);
            } else if (favoriteActorCount == 2) {
                twoFavoriteActors.add(pair);
            }
        }

        // 두 배우가 모두 포함된 페어 중 평점이 가장 높고, 리뷰 수가 많은 페어
        if (!twoFavoriteActors.isEmpty()) {
            return twoFavoriteActors.stream()
                    .max(Comparator.comparing(Pair::getRatingAverage)
                            .thenComparing(Pair::getReviewCount))
                    .orElse(null);
        }

        // 찜한 배우가 1명 있는 페어 중 평점이 가장 높고, 리뷰 수가 많은 페어
        return oneFavoriteActors.stream()
                .max(Comparator.comparing(Pair::getRatingAverage)
                        .thenComparing(Pair::getReviewCount))
                .orElse(null);
    }

    public Pair getPreActorPair(List<Pair> pairList, User user) {
        // 유저가 이전에 봤던 배우 리스트 (Set을 사용하여 중복 제거)
        Set<Actor> preActorSet = new HashSet<>();

        for (Review review : user.getReviews()) {
            Actor actor1 = actorRepository.findById(review.getPair().getActor1())
                    .orElseThrow(() -> new RuntimeException("Actor not found: " + review.getPair().getActor1()));
            preActorSet.add(actor1);

            Actor actor2 = actorRepository.findById(review.getPair().getActor2())
                    .orElseThrow(() -> new RuntimeException("Actor not found: " + review.getPair().getActor2()));
            preActorSet.add(actor2);
        }

        // 유저가 이전에 봤던 배우가 1명 있는 페어 리스트
        List<Pair> onePreActors = new ArrayList<>();

        // 유저가 이전에 봤던 배우가 2명 있는 페어 리스트
        List<Pair> twoPreActors = new ArrayList<>();

        for (Pair pair : pairList) {
            int prePairCount = 0;

            if (preActorSet.contains(actorRepository.findById(pair.getActor1()).orElse(null))) {
                prePairCount++;
            }
            if (preActorSet.contains(actorRepository.findById(pair.getActor2()).orElse(null))) {
                prePairCount++;
            }

            if (prePairCount == 1) {
                onePreActors.add(pair);
            } else if (prePairCount == 2) {
                twoPreActors.add(pair);
            }
        }

        // 이전에 봤던 배우가 2명인 페어 중 평점이 가장 높고, 리뷰 수가 많은 페어
        if (!twoPreActors.isEmpty()) {
            return twoPreActors.stream()
                    .max(Comparator.comparing(Pair::getRatingAverage)
                            .thenComparing(Pair::getReviewCount))
                    .orElse(null);
        }

        // 이전에 봤던 배우가 1명인 페어 중 평점이 가장 높고, 리뷰 수가 많은 페어
        return onePreActors.stream()
                .max(Comparator.comparing(Pair::getRatingAverage)
                        .thenComparing(Pair::getReviewCount))
                .orElse(null);
    }

    public Pair getPreHashtagPair(List<Pair> pairList, User user) {
        // 유저가 작성한 리뷰에서 해시태그 추출
        Set<String> preHashtagSet = user.getReviews().stream()
                .map(Review::getHashtag)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        int maxHashtagCount = 0;
        Pair maxPreHashtagPair = null;

        for (Pair pair : pairList) {
            int hashtagCount = 0;

            if (preHashtagSet.contains(pair.getHashtag1())) {
                hashtagCount++;
            }
            if (preHashtagSet.contains(pair.getHashtag2())) {
                hashtagCount++;
            }
            if (preHashtagSet.contains(pair.getHashtag3())) {
                hashtagCount++;
            }

            if (hashtagCount > maxHashtagCount) {
                maxHashtagCount = hashtagCount;
                maxPreHashtagPair = pair;
            }
            // 해시태그 개수가 같으면, 리뷰 평점 순, 리뷰 개수 순으로 비교
            else if (hashtagCount == maxHashtagCount) {
                if (maxPreHashtagPair == null ||
                        pair.getRatingAverage().compareTo(maxPreHashtagPair.getRatingAverage()) > 0 ||
                        (pair.getRatingAverage().compareTo(maxPreHashtagPair.getRatingAverage()) == 0 &&
                                pair.getReviewCount().compareTo(maxPreHashtagPair.getReviewCount()) > 0)) {
                    maxPreHashtagPair = pair;
                }
            }
        }

        return maxPreHashtagPair;
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul") // 하루 간격으로 갱신
    public void updateTopHashtags() {
        logger.info("updatePairTopHashtags started");

        List<Pair> pairs = pairRepository.findAll();
        for (Pair pair : pairs) { // 페어 하나씩 돌며 업데이트
            List<Review> reviews = reviewRepository.findByPair(pair);
            Map<String, Long> hashtagFrequency = new HashMap<>();

            for (Review review : reviews) {
                String hashtag = review.getHashtag();
                if (hashtag != null && !hashtag.isEmpty()) {
                    hashtagFrequency.put(hashtag, hashtagFrequency.getOrDefault(hashtag, 0L) + 1);
                }
            }

            // 가장 빈도가 높은 해시태그 3개를 찾기
            List<String> topHashtags = hashtagFrequency.entrySet()
                    .stream()
                    .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                    .limit(3)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            pair.updateTopHashtags(topHashtags); // 해당 해시태그를 공연 엔티티에 저장
            pairRepository.save(pair); // 위 메소드 에서 엔티티 변경 후 트랜잭션이 끝날때 변경사항이 db에 반영되어 save 생략해도 됨
        }
        logger.info("updatePairTopHashtags finished");
    }

    public List<Pair> findPairsByPerformance(Performance performance){
        return pairRepository.findAllByPerformance(performance);
    }

    public List<Pair> findPopularPairList(Pageable pageable) {
        return pairRepository.findPopularPairs(pageable);
    }
}
