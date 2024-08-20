package kopis.k_backend.feign.kopis.performance;

import kopis.k_backend.feign.kopis.hall.KopisHallService;
import kopis.k_backend.job.Job;
import kopis.k_backend.job.JobRepository;
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
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class KopisPerfService {
    private final Executor asyncExecutor;
    private final KopisPerfClient kopisPerfClient;
    private final PerformanceRepository performanceRepository;
    private final HallRepository hallRepository;
    private final KopisHallService kopisHallService;
    private final JobRepository jobRepository;
    private final String service;
    private final Integer cpage;
    private final Integer rows;

    public KopisPerfService(KopisPerfClient kopisPerfClient, PerformanceRepository performanceRepository,
                            HallRepository hallRepository, KopisHallService kopisHallService,
                            JobRepository jobRepository, @Value("${kopis.key}") String apiKey) {
        this.kopisPerfClient = kopisPerfClient;
        this.performanceRepository = performanceRepository;
        this.hallRepository = hallRepository;
        this.kopisHallService = kopisHallService;
        this.jobRepository = jobRepository;
        this.service = apiKey;
        this.cpage = 1;
        this.rows = 5000;

        // 스레드 풀 사이즈를 명시적으로 제한 -> 한 번에 처리할 수 있는 작업의 수 제한하여 서버 폭주하는 것 방지
        this.asyncExecutor = Executors.newFixedThreadPool(20);
    }

    // 공연 상태 공연예정 -> 공연중 & 공연중 -> 공연완료 업데이트
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul") // 매일 자정에 실행
    private void updatePerfStateEveryDayDev(){
        updatePerfState();
    }

    public void updatePerfState(){
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter jobFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        String jobId = today.format(jobFormatter);
        String jobType = "PERFORMANCE_STATE";
        Job jobEntity = new Job(jobId, "-", "IN_PROGRESS", jobType);
        jobRepository.save(jobEntity);

        // 공연 상태가 "공연중"인 모든 공연들을 찾기
        List<Performance> ongoingPerformances = performanceRepository.findByState("공연중");

        for (Performance performance : ongoingPerformances) {
            LocalDate startDate = LocalDate.parse(performance.getStartDate(), formatter);
            LocalDate endDate = LocalDate.parse(performance.getEndDate(), formatter);

            // 종료 날짜가 오늘 이전이라면 상태를 "공연완료"로 업데이트
            if (endDate.isBefore(ChronoLocalDate.from(today))) {
                performance.updateState("공연완료");
                performanceRepository.save(performance);
                System.out.println("Updated Performance: " + performance.getKopisPerfId() + " to '공연완료'");
            }
            // 시작 날짜가 오늘 이후이라면 상태를 "공연예정"으로 업데이트
            else if (startDate.isAfter(ChronoLocalDate.from(today))){
                performance.updateState("공연예정");
                performanceRepository.save(performance);
                System.out.println("Updated Performance: " + performance.getKopisPerfId() + " to '공연예정'");
            }
        }

        // 공연 상태가 "공연예정"인 모든 공연들을 찾기
        List<Performance> waitingPerformances = performanceRepository.findByState("공연예정");
        for (Performance performance : waitingPerformances){
            LocalDate startDate = LocalDate.parse(performance.getStartDate(), formatter);
            LocalDate endDate = LocalDate.parse(performance.getEndDate(), formatter);

            // 시작 날짜가 오늘이거나 오늘 이전이고, 끝 날짜가 오늘 이후거나 오늘이라면 상태를 "공연중"으로 업데이트
            if(startDate.isBefore(ChronoLocalDate.from(today.plusDays(1))) && endDate.isAfter(ChronoLocalDate.from(today.minusDays(1)))) {
                performance.updateState("공연중");
                performanceRepository.save(performance);
                System.out.println("Updated Performance: " + performance.getKopisPerfId() + " to '공연중'");
            }

            // 끝 날짜가 오늘 이전이라면 상태를 "공연완료"로 업데이트
            else if(endDate.isBefore(ChronoLocalDate.from(today))){
                performance.updateState("공연완료");
                performanceRepository.save(performance);
                System.out.println("Updated Performance: " + performance.getKopisPerfId() + " to '공연완료'");
            }
        }

        // 공연 상태가 "공연완료"인 모든 공연들을 찾기
        List<Performance> endedPerformances = performanceRepository.findByState("공연완료");
        for (Performance performance : endedPerformances){
            LocalDate startDate = LocalDate.parse(performance.getStartDate(), formatter);
            LocalDate endDate = LocalDate.parse(performance.getEndDate(), formatter);

            // 시작 날짜가 오늘이거나 오늘 이전이고, 끝 날짜가 오늘 이후거나 오늘이라면 상태를 "공연중"으로 업데이트
            if(startDate.isBefore(ChronoLocalDate.from(today.plusDays(1))) && endDate.isAfter(ChronoLocalDate.from(today.minusDays(1)))) {
                performance.updateState("공연중");
                performanceRepository.save(performance);
                System.out.println("Updated Performance: " + performance.getKopisPerfId() + " to '공연중'");
            }
            // 시작이 오늘 이후라면
            else if(startDate.isAfter(ChronoLocalDate.from(today))){
                performance.updateState("공연예정");
                performanceRepository.save(performance);
                System.out.println("Updated Performance: " + performance.getKopisPerfId() + " to '공연예정'");
            }
        }


        LocalDateTime now = LocalDateTime.now();
        jobEntity.setStatus("COMPLETED"); jobEntity.setEnd(now.format(jobFormatter));
        jobRepository.save(jobEntity); // 완료
    }

    @Scheduled(cron = "0 0 1 * * *", zone = "Asia/Seoul") // 기존 1시
    private void putPerfListEveryDayDev(){
        putPerfList();
    }

    public void putPerfList() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        Integer formattedDate = Integer.valueOf(today.format(formatter));

        LocalDateTime today_2 = LocalDateTime.now();
        DateTimeFormatter jobFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm");
        String jobId = today_2.format(jobFormatter);

        String jobType = "PERFORMANCE_SYNC";
        Job jobEntity = new Job(jobId, "-", "IN_PROGRESS", jobType);
        jobRepository.save(jobEntity);

        CompletableFuture.runAsync(() -> { // 전체 작업을 별도의 비동기 스레드에서 실행하도록 함
            List<String> genres = Arrays.asList("GGGA", "AAAA");
            List<String> hallIds = kopisHallService.getAllHallId();

            List<CompletableFuture<Void>> futures = new ArrayList<>();

            for (String genre : genres) {
                for (String hallId : hallIds) {
                    for (int n = 1; n <= 13; n++) {
                        String formattedNumber = String.format("%02d", n);

                        CompletableFuture<Void> future = executeWithRetry(0, genre, hallId, formattedNumber, formattedDate) // 첫 시도
                                .orTimeout(30, TimeUnit.MINUTES) // 타임아웃 설정
                                .exceptionally(ex -> {
                                    System.err.println("Failed to process hall: " + hallId + ", genre: " + genre + ". Timeout or other error: " + ex.getMessage());
                                    return null; // 실패한 작업에 대해 로직이 중단되지 않도록 함
                                });

                        futures.add(future);

                        // 딜레이 추가
                        try {
                            Thread.sleep(200); // 200ms 딜레이
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            LocalDateTime now = LocalDateTime.now();
            jobEntity.setStatus("COMPLETED"); jobEntity.setEnd(now.format(jobFormatter));
            jobRepository.save(jobEntity); // 완료

        }, asyncExecutor).exceptionally(ex -> {

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter now_formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm");
            jobEntity.setStatus("FAILED"); jobEntity.setEnd(now.format(now_formatter));
            jobRepository.save(jobEntity); // 실패

            System.err.println("Scheduled job failed. Error: " + ex.getMessage());
            return null;
        });
    }

    private CompletableFuture<Void> executeWithRetry(int attempt, String genre, String hallId, String formattedNumber, Integer formattedDate) {
        return CompletableFuture.runAsync(() -> { // 각 공연장에 대한 작업을 비동기적으로 실행 => 모든 공연장에 대해 비동기 처리하여 동시에 여러 작업 처리할 수 있도록 함
            try {
                ResponseEntity<String> response = kopisPerfClient.getPerfs(service, formattedDate, 99999999, genre, hallId + "-" + formattedNumber, cpage, rows, "Y");
                String body = response.getBody();
                if (body != null) {
                    putPerfListLogic(body, hallId);
                } else {
                    System.out.println("Received empty body for hall: " + hallId + ", genre: " + genre);
                }
            } catch (Exception e) {
                if (attempt < 3) { // 최대 3번까지 재시도
                    System.out.println("Retrying... Attempt: " + (attempt + 1));
                    executeWithRetry(attempt + 1, genre, hallId, formattedNumber, formattedDate).join(); // 재귀적으로 호출하여 재시도
                } else {
                    System.out.println("Max retry attempts reached for hall: " + hallId + ", genre: " + genre);
                    e.printStackTrace();
                }
            }
        }, asyncExecutor);
    }

    private String getElementValue(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }

    public void putPerfListLogic(String body, String hallId) throws ParserConfigurationException, IOException, SAXException {
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
                if(Objects.equals(prfstate, "공연완료")) continue; // 우선 공연완료 된 건 받지 않기

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
                            .runtime("-")
                            .lowestPrice("-")
                            .highestPrice("-")
                            .price("-")
                            .reviewCount(0L)
                            .ratingAverage(0.0)
                            .build();
                    performanceRepository.save(performance);

                    putPerfDetail(mt20id); // 디테일 넣기
                    System.out.println("Saved Performance: " + mt20id + " " + prfnm);

                }
            }
        }
    }

    public void putPerfDetail(String perfId) {
        ResponseEntity<String> response = kopisPerfClient.getPerf(service, perfId, "Y");
        String body = response.getBody();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            assert body != null;
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
                    String prfcast = getElementValue(element, "prfcast");
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
                        performance.updateExistingPerformance(prfruntime, pcseguidance, prfcast, String.valueOf(lowestPrice), String.valueOf(highestPrice), relateurl);
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
                                .runtime(prfruntime)
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
}