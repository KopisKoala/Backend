package kopis.k_backend;

import kopis.k_backend.review.domain.Review;
import kopis.k_backend.review.service.ReviewLikeService;
import kopis.k_backend.review.service.ReviewService;
import kopis.k_backend.user.domain.User;
import kopis.k_backend.user.jwt.CustomUserDetails;
import kopis.k_backend.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import kopis.k_backend.review.repository.ReviewLikeRepository;
import kopis.k_backend.review.repository.ReviewRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class KBackendApplicationTests {

	ReviewLikeService reviewLikeService;
	ReviewRepository reviewRepository;
	User user;
	UserService userService;
	CustomUserDetails customUserDetails;

	@Test
	@DisplayName("동시에 100개의 요청으로 리뷰 좋아요 누르기")
	void like_100_request() throws InterruptedException {
		// given
		final int threadCount = 100;
		final ExecutorService executorService = Executors.newFixedThreadPool(32);
		final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

		// when
		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					//reviewLikeService.toggleLikeAndRetrieveCount(1L, user);
				} finally {
					countDownLatch.countDown();
				}
			});
		}
		countDownLatch.await();
		//final Review review = reviewRepository.findById(1L).orElseThrow();

		// then
		//assertThat(review.getLikeCount()).isEqualTo(1L);
	}

}
