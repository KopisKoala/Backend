package kopis.k_backend.feign.kopis.performance;

import kopis.k_backend.feign.kopis.hall.KopisHallService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class KopisPerfServiceLocal {
    private final KopisPerfClient kopisPerfClient;
    private final KopisHallService kopisHallService;
    private final KopisPerfService kopisPerfService;
    private final String service;

    public KopisPerfServiceLocal(KopisPerfClient kopisPerfClient, KopisHallService kopisHallService,
                                 KopisPerfService kopisPerfService, @Value("${kopis.key}") String apiKey
    ) {
        this.kopisPerfClient = kopisPerfClient;
        this.kopisHallService = kopisHallService;
        this.kopisPerfService = kopisPerfService;
        this.service = apiKey;
    }

    public void updatePerfStateLocal(){
        kopisPerfService.updatePerfState();
    }

    public void putPerfListLocal(){
        kopisPerfService.putPerfList();
    }

    public void putPerfListForAllGenresAndHalls(int hallNum) { // test
        List<String> genres = Arrays.asList("GGGA", "AAAA");
        List<String> hallIds = kopisHallService.getAllHallId();

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (String genre : genres) {
            for (String hallId : hallIds) {
                String formattedNumber = String.format("%02d", hallNum);

                // Java의 CompletableFuture를 사용하여 병렬 처리로 전환하여 로직 최적화
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {

                    ResponseEntity<String> response = kopisPerfClient.getPerfs(service, 20240801, 99999999, genre, hallId + "-" + formattedNumber, 1, 10, "Y");
                    String body = response.getBody();
                    System.out.println("GET BODY - 잘 실행되고 있나 확인하는 용");
                    try {
                        assert body != null;
                        kopisPerfService.putPerfListLogic(body, hallId);
                    } catch (Exception e) {
                        System.out.println("Error processing performance detail: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
                futures.add(future);

                // 딜레이 추가
                try {
                    Thread.sleep(100); // 100ms 딜레이
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

}
