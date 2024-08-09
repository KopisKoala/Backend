package kopis.k_backend.feign.kopis.hall;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kopisHallClient", url = "${kopis.api-url}")
public interface KopisHallClient {
    @GetMapping("/prfplc")
    ResponseEntity<String> getHalls(@RequestParam("service") String service,
                                    //@RequestParam("shprfnmfct") String shprfnmfct,
                                    @RequestParam("cpage") Integer cpage,
                                    @RequestParam("rows") Integer rows);
    @GetMapping("/prfplc/{hall-id}")
    ResponseEntity<String> getHall(@RequestParam("service") String service,
                                    @RequestParam("hall-id") String hall,
                                    @RequestParam("newsql") String newsql);

}
