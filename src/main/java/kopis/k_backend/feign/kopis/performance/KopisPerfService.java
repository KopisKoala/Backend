package kopis.k_backend.feign.kopis.performance;

import kopis.k_backend.feign.kopis.hall.KopisHallService;
import kopis.k_backend.performance.domain.Hall;
import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.performance.domain.PerformanceType;
import kopis.k_backend.performance.repository.HallRepository;
import kopis.k_backend.performance.repository.PerformanceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class KopisPerfService {

    private final KopisPerfClient kopisPerfClient;
    private final PerformanceRepository performanceRepository;
    private final HallRepository hallRepository;
    private final KopisHallService kopisHallService;

    private final String service;
    private final Integer cpage;
    private final Integer rows;

    public KopisPerfService(KopisPerfClient kopisPerfClient, PerformanceRepository performanceRepository,
                            HallRepository hallRepository, KopisHallService kopisHallService,
                            @Value("${kopis.key}") String apiKey) {
        this.kopisPerfClient = kopisPerfClient;
        this.performanceRepository = performanceRepository;
        this.hallRepository = hallRepository;
        this.kopisHallService = kopisHallService;
        this.service = apiKey;
        this.cpage = 1;
        this.rows = 5000;
    }

    public List<String> getAllPerfId() {
        return performanceRepository.findAllKopisPerfIds();
    }

    public void putPerfDetail(String perfId) {
        ResponseEntity<String> response = kopisPerfClient.getPerf(service, perfId, "Y");
        String body = response.getBody();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(body.getBytes()));

            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("db");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String mt20id = getElementValue(element, "mt20id");
                    String prfnm = getElementValue(element, "prfnm");
                    String prfpdfrom = getElementValue(element, "prfpdfrom");
                    String prfpdto = getElementValue(element, "prfpdto");
                    String fcltynm = getElementValue(element, "fcltynm");
                    String prfruntime = getElementValue(element, "prfruntime");
                    String pcseguidance = getElementValue(element, "pcseguidance");
                    String genrenm = getElementValue(element, "genrenm");
                    String poster = getElementValue(element, "poster");
                    String prfstate = getElementValue(element, "prfstate");
                    String relateurl = getElementValue(element, "relateurl");

                    PerformanceType performanceType;
                    if ("뮤지컬".equals(genrenm)) {
                        performanceType = PerformanceType.MUSICAL;
                    } else if ("연극".equals(genrenm)) {
                        performanceType = PerformanceType.PLAY;
                    } else {
                        System.out.println("Skipping non-musical/play genre: " + genrenm);
                        continue;
                    }

                    System.out.println("price: " + pcseguidance);
                    // 가격 추출 로직 수정
                    Set<Integer> priceSet = Pattern.compile("(\\d{1,3}(,\\d{3})*)원")
                            .matcher(pcseguidance)
                            .results()
                            .map(matchResult -> matchResult.group(1).replaceAll(",", ""))
                            .map(Integer::parseInt)
                            .collect(Collectors.toSet()); // 중복 제거를 위한 Set 사용

                    List<Integer> prices = priceSet.stream()
                            .sorted() // 오름차순 정렬
                            .toList();

                    // collection 출력
                    for(int price : prices){
                        System.out.println(price + " ");
                    }
                    int lowestPrice = 0;
                    int highestPrice = 0;

                    if (!prices.isEmpty()) {
                        lowestPrice = Collections.min(prices);
                        highestPrice = Collections.max(prices);
                    }
                    System.out.println("lowest: " + lowestPrice + " / highest " + highestPrice);

                    Optional<Performance> existingPerformance = performanceRepository.findByKopisPerfId(mt20id);
                    if (existingPerformance.isPresent()) {
                        Performance performance = existingPerformance.get();
                        performance.setDuration(prfruntime);
                        performance.setPrice(pcseguidance);
                        performance.setLowestPrice(String.valueOf(lowestPrice));
                        performance.setHighestPrice(String.valueOf(highestPrice));
                        performance.setTicketingLink(relateurl);
                        performanceRepository.save(performance);
                        System.out.println("Updated Performance: " + performance);
                    } else {
                        Hall hall = hallRepository.findByKopisHallId(fcltynm)
                                .orElseThrow(() -> new RuntimeException("Hall not found: " + fcltynm));

                        Performance performance = Performance.builder()
                                .kopisPerfId(mt20id)
                                .title(prfnm)
                                .performanceType(performanceType)
                                .hall(hall)
                                .startDate(prfpdfrom)
                                .endDate(prfpdto)
                                .poster(poster)
                                .state(prfstate)
                                .duration(prfruntime)
                                .price(pcseguidance)
                                .lowestPrice(String.valueOf(lowestPrice))
                                .highestPrice(String.valueOf(highestPrice))
                                .ticketingLink(relateurl)
                                .build();
                        performanceRepository.save(performance);
                        System.out.println("Saved Performance: " + mt20id + " " + prfnm);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error processing performance detail: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul") // 매일 자정에 실행
    public void putPerfListEveryDay() {
        //putPerfListForAllGenresAndHalls();

        // 당일에 새로 생긴 공연 넣기
        List<String> generes = Arrays.asList("GGGA", "AAAA");
        List<String> hallIds = kopisHallService.getAllHallId();

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        Integer formattedDate = Integer.valueOf(today.format(formatter));

        for (String genere : generes) {
            for (String hallId : hallIds) {
                for (int n = 1; n <= 13; n++) {
                    String formattedNumber = String.format("%02d", n);

                    ResponseEntity<String> response = kopisPerfClient.getPerfs(service, formattedDate, 99999999, genere, hallId + "-" + formattedNumber, 1, 10, "Y");
                    String body = response.getBody();

                    try {
                        assert body != null;
                        putPerfListLogic(body, hallId);
                    } catch (Exception e) {
                        System.out.println("Error processing performance detail: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void putPerfListForAllGenresAndHalls() {
        List<String> generes = Arrays.asList("GGGA", "AAAA");
        List<String> hallIds = kopisHallService.getAllHallId();

        // 20240101부터의 공연 데이터 넣기
        for (String genere : generes) {
            for (String hallId : hallIds) {
                for (int n = 1; n <= 13; n++) {
                    String formattedNumber = String.format("%02d", n);

                    ResponseEntity<String> response = kopisPerfClient.getPerfs(service, 20240101, 99999999, genere, hallId + "-" + formattedNumber, 1, 10, "Y");
                    String body = response.getBody();

                    try {
                        assert body != null;
                        putPerfListLogic(body, hallId);
                    } catch (Exception e) {
                        System.out.println("Error processing performance detail: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private String getElementValue(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }

    private void putPerfListLogic(String body, String hallId) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(body.getBytes()));

        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName("db");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String mt20id = getElementValue(element, "mt20id");
                String prfnm = getElementValue(element, "prfnm");
                String prfpdfrom = getElementValue(element, "prfpdfrom");
                String prfpdto = getElementValue(element, "prfpdto");
                String poster = getElementValue(element, "poster");
                String genrenm = getElementValue(element, "genrenm");
                String prfstate = getElementValue(element, "prfstate");
                if(!Objects.equals(prfstate, "공연중")) continue;

                System.out.println("Parsed performance: " + mt20id + ", " + prfnm + ", " + genrenm);

                PerformanceType performanceType;
                if ("뮤지컬".equals(genrenm)) {
                    performanceType = PerformanceType.MUSICAL;
                } else if ("연극".equals(genrenm)) {
                    performanceType = PerformanceType.PLAY;
                } else {
                    continue;
                }

                Optional<Performance> existingPerformance = performanceRepository.findByKopisPerfId(mt20id);
                if (existingPerformance.isPresent()) {
                    System.out.println("Performance already exists: " + mt20id);
                } else {
                    Hall hall = hallRepository.findByKopisHallId(hallId)
                            .orElseThrow(() -> new RuntimeException("Hall not found: " + hallId));

                    Performance performance = Performance.builder()
                            .kopisPerfId(mt20id)
                            .title(prfnm)
                            .performanceType(performanceType)
                            .hall(hall)
                            .startDate(prfpdfrom)
                            .endDate(prfpdto)
                            .poster(poster)
                            .state(prfstate)
                            .duration("-")
                            .lowestPrice("-")
                            .highestPrice("-")
                            .price("-")
                            .reviewCount(0L)
                            .ratingAverage(0.0)
                            .build();
                    performanceRepository.save(performance);
                    System.out.println("Saved Performance: " + mt20id + " " + prfnm);
                }
            }
        }
    }
}