package kopis.k_backend.feign.kopis.popularPerformance;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "KopisPopularClient", url = "${kopis.api-url}")

public interface KopisPopularClient {
    @GetMapping("/prfstsPrfBy")
    ResponseEntity<String> getPopularPerfs(@RequestParam("service") String service, // service key (필수)
                                    @RequestParam("stdate") Integer stdate, // 시작 일자 (필수)
                                    @RequestParam("eddate") Integer eddate, // 종료 날짜 (필수)
                                    @RequestParam("shcate") String shcate, // 장르
                                    @RequestParam("cpage") Integer cpage, // (필수)
                                    @RequestParam("rows") Integer rows); // (필수)

}
