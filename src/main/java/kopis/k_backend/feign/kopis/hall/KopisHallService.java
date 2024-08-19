package kopis.k_backend.feign.kopis.hall;

import kopis.k_backend.job.Job;
import kopis.k_backend.job.JobRepository;
import kopis.k_backend.performance.domain.Hall;
import kopis.k_backend.performance.repository.HallRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class KopisHallService {
    private final KopisHallClient kopisHallClient;
    private final HallRepository hallRepository;
    private final JobRepository jobRepository;
    private final String service;
    private final Integer cpage;
    private final Integer rows;

    public KopisHallService(KopisHallClient kopisHallClient, HallRepository hallRepository,
                            JobRepository jobRepository, @Value("${kopis.key}") String apiKey) {
        this.kopisHallClient = kopisHallClient;
        this.hallRepository = hallRepository;
        this.jobRepository = jobRepository;
        this.service = apiKey;
        this.cpage = 1;
        this.rows = 10000;
    }

    public List<String> getAllHallId() {
        return hallRepository.findAllKopisHallIds();
    }


    @Scheduled(cron = "0 0 23 1 * ?", zone = "Asia/Seoul") // 매달 1일 23시에 공연장 재탐색 -> 새로운 공연장 추가 + 기존 공연장 정보 수정
    public void putHallList() {
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter jobFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm");

        String jobId = today.format(jobFormatter);
        String jobType = "HALL_SYNC";
        Job jobEntity = new Job(jobId, "-", "IN_PROGRESS", jobType);
        jobRepository.save(jobEntity);

        // Kopis api에 요청을 보내기 전에 apiKey를 쿼리로 설정
        ResponseEntity<String> response = kopisHallClient.getHalls(service, cpage, rows);

        // API Response로부터 body 뽑아내기
        String body = response.getBody();

        try {
            // XML 문서 빌더 생성
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            assert body != null; // body가 null인 경우 프로그램 중단
            Document doc = builder.parse(new ByteArrayInputStream(body.getBytes()));

            // 루트 엘리먼트 가져오기
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("db");

            // 각 공연장 데이터 추출
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String hallNameElement = getElementValue(element, "fcltynm");
                    String hallIdElement = getElementValue(element, "mt10id");
                    String sidonm = getElementValue(element, "sidonm");
                    String gugunnm = getElementValue(element, "gugunnm");
                    String streetAddress = ""; // XML 데이터에 streetAddress 정보가 없으므로 빈 문자열로 설정

                    Optional<Hall> existingHall = hallRepository.findByKopisHallId(hallIdElement);
                    if (existingHall.isPresent()) {

                        if(Objects.equals(hallIdElement, "FC001176")) continue; // 한얼소극장 데이터가 이상함
                        putHallDetail(hallIdElement); // 기존 공연장 내용 수정

                        System.out.println("Hall with kopisHallId " + hallIdElement + " already exists. Skipping.");

                    } else {
                        Hall newHall = Hall.builder()
                                .hallName(hallNameElement)
                                .kopisHallId(hallIdElement)
                                .sidonm(sidonm)
                                .gugunnm(gugunnm)
                                .streetAddress(streetAddress)
                                .build();
                        hallRepository.save(newHall);

                        if(Objects.equals(hallIdElement, "FC001176")) continue; // 한얼소극장 데이터가 이상함
                        putHallDetail(hallIdElement); // 새로운 공연장 정보 추가

                        System.out.println("Saved Hall: " + newHall);
                    }

                    System.out.println("hallName: " + hallNameElement + " / hallId: " + hallIdElement);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        LocalDateTime now = LocalDateTime.now();
        jobEntity.setStatus("COMPLETED"); jobEntity.setEnd(now.format(jobFormatter));
        jobRepository.save(jobEntity); // 완료
    }

    private String getElementValue(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }

    public void putHallDetail(String hallId) {
        // Kopis api에 요청을 보내기 전에 apiKey를 쿼리로 설정
        ResponseEntity<String> response = kopisHallClient.getHall(service, hallId, "Y");

        // API Response로부터 body 뽑아내기
        String body = response.getBody();

        try {
            // XML 문서 빌더 생성
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            assert body != null;
            Document doc = builder.parse(new ByteArrayInputStream(body.getBytes()));

            // 루트 엘리먼트 가져오기
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("db");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String fcltynm = getElementValue(element, "fcltynm");
                    String mt10id = getElementValue(element, "mt10id");
                    String sidonm = getElementValue(element, "sidonm");
                    String gugunnm = getElementValue(element, "gugunnm");
                    String streetAddress = getElementValue(element, "adres");
                    String restaurant = getElementValue(element, "restaurant");
                    String cafe = getElementValue(element, "cafe");
                    String store = getElementValue(element, "store");
                    String nolibang = getElementValue(element, "nolibang");
                    String suyu = getElementValue(element, "suyu");
                    String parkinglot = getElementValue(element, "parkinglot");
                    String telno = getElementValue(element, "telno");
                    String relateurl = getElementValue(element, "relateurl");

                    Optional<Hall> existingHall = hallRepository.findByKopisHallId(mt10id);
                    if (existingHall.isPresent()) {
                        Hall hall = existingHall.get();
                        hall.updateExistingHall(streetAddress, restaurant, cafe, store, nolibang, suyu, parkinglot, telno, relateurl);
                        hallRepository.save(hall);
                        System.out.println("Updated Hall: " + hall);
                    } else {
                        Hall hall = Hall.builder()
                                .kopisHallId(mt10id)
                                .hallName(fcltynm)
                                .sidonm(sidonm)
                                .gugunnm(gugunnm)
                                .streetAddress(streetAddress)
                                .restaurant(restaurant)
                                .cafe(cafe)
                                .store(store)
                                .nolibang(nolibang)
                                .suyu(suyu)
                                .parkinglot(parkinglot)
                                .telno(telno)
                                .relateurl(relateurl)
                                .build();
                        hallRepository.save(hall);
                        System.out.println("Saved Hall: " + hall);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error processing hall detail: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
