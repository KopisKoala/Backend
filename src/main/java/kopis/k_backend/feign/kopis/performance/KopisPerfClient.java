package kopis.k_backend.feign.kopis.performance;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kopisPerfClient", url = "${kopis.api-url}")
public interface KopisPerfClient {
    @GetMapping("/pblprfr")
    ResponseEntity<String> getPerfs(@RequestParam("service") String service, // service key (필수)
                                    @RequestParam("stdate") Integer stdate, // 시작 일자 (필수)
                                    @RequestParam("eddate") Integer eddate, // 종료 날짜 (필수)
                                    @RequestParam("shcate") String shcate, // 장르
                                    @RequestParam("prfplccd") String prfplccd, // 공연장코드
                                    @RequestParam("cpage") Integer cpage, // (필수)
                                    @RequestParam("rows") Integer rows, // (필수)
                                    @RequestParam("newsql") String newsql);

    @GetMapping("/pblprfr/{perf-id}")
    ResponseEntity<String> getPerf(@RequestParam("service") String service,
                                   @PathVariable("perf-id") String hall,
                                   @RequestParam("newsql") String newsql);


}
