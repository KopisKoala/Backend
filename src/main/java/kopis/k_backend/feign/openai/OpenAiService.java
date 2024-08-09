package kopis.k_backend.feign.openai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import kopis.k_backend.global.config.OpenAiConfig;
import kopis.k_backend.pair.domain.Pair;
import kopis.k_backend.pair.repository.PairRepository;
import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.performance.repository.PerformanceRepository;
import kopis.k_backend.review.domain.Review;
import kopis.k_backend.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class OpenAiService {

    private final OpenAiConfig openAiConfig;
    private final PerformanceRepository performanceRepository;
    private final ReviewRepository reviewRepository;
    private final PairRepository pairRepository;

    @Value("${openai.url.prompt}")
    private String promptUrl;

    public Map<String, Object> prompt(CompletionDto completionDto) {

        HttpHeaders headers = openAiConfig.httpHeaders();

        HttpEntity<CompletionDto> requestEntity = new HttpEntity<>(completionDto, headers);
        ResponseEntity<String> response = openAiConfig
                .restTemplate()
                .exchange(promptUrl, HttpMethod.POST, requestEntity, String.class);

        Map<String, Object> resultMap = new HashMap<>();
        try {
            ObjectMapper om = new ObjectMapper();
            resultMap = om.readValue(response.getBody(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            log.debug("JsonMappingException :: " + e.getMessage());
        } catch (RuntimeException e) {
            log.debug("RuntimeException :: " + e.getMessage());
        }
        return resultMap;
    }

    // 매월 1일마다 공연 리뷰 요약 갱신
    @Scheduled(cron = "0 0 0 1 * ?", zone = "Asia/Seoul")
    public void updatePerformanceReviewSummary() {
        List<Performance> performances = performanceRepository.findAll();

        // 요약할 리뷰 수
        int summaryCount = 3;

        for (Performance performance : performances) {
            if (performance.getReviewCount() < summaryCount) continue;

            List<Review> reviews = reviewRepository.findReviewByPerformanceAndWay(performance, "like", PageRequest.of(0, summaryCount)).toList();

            StringBuilder reviewsContent = new StringBuilder();

            for (Review review : reviews) {
                reviewsContent.append(review.getContent()).append("\n");
            }

            CompletionDto completionDto = new CompletionDto("gpt-3.5-turbo-instruct", reviewsContent.append("위 리뷰들을 합쳐 새로운 한 줄 리뷰를 추가 설명 없이 만들어 주세요.").toString(), 0f, 100);
            Map<String, Object> response = prompt(completionDto);

            String reviewSummary = "";

            ObjectMapper om = new ObjectMapper();
            List<Map<String, Object>> choices = om.convertValue(response.get("choices"), new TypeReference<List<Map<String, Object>>>() {});
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> firstChoice = choices.get(0);
                reviewSummary = (String) firstChoice.get("text");
            }

            performance.updateReviewSummary(reviewSummary);
            performanceRepository.save(performance);
        }
    }

    // 매월 1일마다 페어 리뷰 요약 갱신
    @Scheduled(cron = "0 0 0 1 * ?", zone = "Asia/Seoul")
    public void updatePairReviewSummary() {
        List<Pair> pairs = pairRepository.findAll();

        // 요약할 리뷰 수
        int summaryCount = 3;

        for (Pair pair : pairs) {
            if (pair.getReviewCount() < summaryCount) continue;

            List<Review> reviews = reviewRepository.findReviewByPairAndWay(pair, "like", PageRequest.of(0, summaryCount)).toList();

            StringBuilder reviewsContent = new StringBuilder();
            for (Review review : reviews) {
                reviewsContent.append(review.getContent()).append("\n");
            }

            CompletionDto completionDto = new CompletionDto("gpt-3.5-turbo-instruct", reviewsContent.append("위 리뷰들을 합쳐 새로운 한 줄 리뷰를 추가 설명 없이 만들어 주세요.").toString(), 0f, 100);
            Map<String, Object> response = prompt(completionDto);

            String reviewSummary = "";

            ObjectMapper om = new ObjectMapper();
            List<Map<String, Object>> choices = om.convertValue(response.get("choices"), new TypeReference<List<Map<String, Object>>>() {});
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> firstChoice = choices.get(0);
                reviewSummary = (String) firstChoice.get("text");
            }

            pair.updateReviewSummary(reviewSummary);
            pairRepository.save(pair);
        }
    }
}
