package kopis.k_backend.scraping.playdb;

import kopis.k_backend.performance.domain.Actor;
import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.performance.domain.PerformanceActor;
import kopis.k_backend.performance.repository.ActorRepository;
import kopis.k_backend.performance.repository.PerformanceActorRepository;
import kopis.k_backend.performance.repository.PerformanceRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScrapPlayDbService {
    private final ActorRepository actorRepository;
    private final PerformanceActorRepository performanceActorRepository;
    private final PerformanceRepository performanceRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    // controller에서 호출
    public void scrapeAndSaveActors(Long performanceId) {

        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new RuntimeException("Performance not found"));

        String title = performance.getTitle(); System.out.println("title: " + title);
        List<String> performanceLinks = searchPerformances(title);

        String hallName = performance.getHall().getHallName(); System.out.println("hallName: " + hallName);
        Optional<String> matchingPerformanceLink = findMatchingPerformance(performanceLinks, hallName);
        System.out.println("matchingPerformanceLink: " + matchingPerformanceLink);

        if (matchingPerformanceLink.isPresent()) {
            System.out.println(matchingPerformanceLink.get().split("/")[3]);
            List<ActorAndRole> actorsAndRoles = getCastInfo(matchingPerformanceLink.get().split("/")[3]);
            saveActors(actorsAndRoles, performance);

            System.out.println("PLAYDB의 공연 배우 정보를 DB에 넣었습니다.");
        } else {
            System.out.println( "PLAYDB에 공연의 배우 정보나 공연 정보가 없습니다.");
            performance.updateCast("no performance in playdb");
            performanceRepository.save(performance);
        }
    }

    // 공연 검색
    private List<String> searchPerformances(String title) {
        String searchUrl = "http://m.playdb.co.kr/Search/View?query=" + title;
        String response = restTemplate.getForObject(searchUrl, String.class);

        Document doc = Jsoup.parse(response);
        List<String> performanceLinks = new ArrayList<>();

        for (Element performanceElement : doc.select("div.article_goods_list > ul > li > a")) {
            String performanceLink = performanceElement.attr("href");

            if (performanceLink.startsWith("/Play/Detail")) {
                performanceLinks.add(performanceLink);
                System.out.println(performanceLink);
            }
        }
        return performanceLinks;
    }

    // 공연 찾기
    private Optional<String> findMatchingPerformance(List<String> performanceLinks, String hallName) {
        String bestMatchLink = null;
        int maxCommonLength = 0;
        LocalDate today = LocalDate.now();

        for (String link : performanceLinks) {
            String performanceUrl = "http://m.playdb.co.kr" + link;
            String response = restTemplate.getForObject(performanceUrl, String.class);

            Document doc = Jsoup.parse(response);

            // "장소"와 관련된 dd 요소를 찾기
            Element venueElement = doc.select("dt:contains(장소) + dd").first();
            // 종료 날짜와 관련된 dd 요소를 찾기
            Element endDateElement = doc.select("dt:contains(일시) + dd").first();

            if (venueElement != null) {
                String hall = venueElement.text().trim();
                System.out.println("Hall: " + hall);

                assert endDateElement != null;
                String endDateText = endDateElement.text().split("~")[1].trim(); // 종료 날짜만 추출
                LocalDate endDate = LocalDate.parse(endDateText, DateTimeFormatter.ofPattern("yyyy.MM.dd"));

                // 공연 종료일이 오늘 이후인지 확인
                if (endDate.isAfter(today)) {
                    // 공통 부분 문자열의 길이 계산
                    int commonLength = longestCommonSubstring(hallName, hall);

                    if (commonLength > maxCommonLength) { // 같다면 처음 나온 공연 선택
                        maxCommonLength = commonLength;
                        bestMatchLink = link;
                    }
                }

            }
        }

        return Optional.ofNullable(bestMatchLink);
    }

    // LCS(최대 공통 문자열)을 통해 가장 많은 글자가 겹치는 hall을 가진 공연 선택하게끔
    private int longestCommonSubstring(String str1, String str2) {
        int[][] lcs = new int[str1.length() + 1][str2.length() + 1];
        int longest = 0;

        for (int i = 1; i <= str1.length(); i++) {
            for (int j = 1; j <= str2.length(); j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    lcs[i][j] = lcs[i - 1][j - 1] + 1;
                    longest = Math.max(longest, lcs[i][j]);
                } else {
                    lcs[i][j] = 0;
                }
            }
        }

        return longest;
    }

    // 배우 정보 추출
    private List<ActorAndRole> getCastInfo(String performanceId) {
        String castUrl = "http://m.playdb.co.kr/Play/CAST/" + performanceId;
        String response = restTemplate.getForObject(castUrl, String.class);

        assert response != null;
        Document doc = Jsoup.parse(response);
        List<ActorAndRole> actorsAndRoles = new ArrayList<>();

        for (Element roleElement : doc.select(".goods_cast_detail > li")) {
            String role = Objects.requireNonNull(roleElement.select("p").first()).text();
            if(role.endsWith("역")) role = role.substring(0, role.length() - 1);
            System.out.println("role: " + role);

            for (Element actorElement : roleElement.select(".cast_list > li")) {
                String actorName = actorElement.select(".name").text();
                System.out.println("actorName: " + actorName);

                String actorImage = actorElement.select("img").attr("src");
                System.out.println("actorImage: " + actorImage);

                ActorAndRole actorAndRole = new ActorAndRole(actorName, role, actorImage);
                actorsAndRoles.add(actorAndRole);
            }
        }

        return actorsAndRoles;
    }

    // db에 저장
    private void saveActors(List<ActorAndRole> actorsAndRoles, Performance performance) {
        boolean flag = false;

        for (ActorAndRole actorAndRole : actorsAndRoles) {
            Optional<Actor> existingActor = actorRepository.findByActorNameAndActorProfile(
                    actorAndRole.getActorName(),
                    actorAndRole.getActorProfile()
            );
            Actor actorEntity; // 이름과 사진까지 같아야 같은 배우

            if (existingActor.isPresent()) {
                System.out.println("이미 존재하는 배우: " + actorAndRole.getActorName());
                actorEntity = existingActor.get();
            } else {
                actorEntity = Actor.builder()
                        .actorName(actorAndRole.getActorName())
                        .actorProfile(actorAndRole.getActorProfile())
                        .build();
                actorEntity = actorRepository.save(actorEntity); // 기존에 없는 배우라면 db에 추가하기
            }

            PerformanceActor performanceActor = PerformanceActor.builder()
                    .actor(actorEntity)
                    .performance(performance)
                    .characterName(actorAndRole.getRole())
                    .build();
            flag = true;
            performanceActorRepository.save(performanceActor);
        }
        if(flag) {
            performance.updateCast("PLAYDB");
            performanceRepository.save(performance);
        }
        else {
            performance.updateCast("no cast in playdb");
            performanceRepository.save(performance);
        }
    }

    public List<Long> findPerformancesWithNonPlayDBCast() {
        List<Long> performanceIds = new ArrayList<>();

        // 모든 Performance 엔티티를 가져온다.
        List<Performance> performances = performanceRepository.findAll();

        for (Performance performance : performances) {
            // cast 컬럼 값이 "playDB" 혹은 "no cast in playdb"가 아닌 경우 id를 리스트에 추가한다.
            if (!"PLAYDB".equals(performance.getCast()) || !"no cast in playdb".equals(performance.getCast())) {
                performanceIds.add(performance.getId());
            }
        }

        return performanceIds;
    }


    // 내부 클래스 정의: Actor와 배역 이름을 함께 저장하기 위한 클래스
    @Getter
    private static class ActorAndRole {
        private final String actorName;
        private final String role;
        private final String actorProfile;

        public ActorAndRole(String actorName, String role, String actorProfile) {
            this.actorName = actorName;
            this.role = role;
            this.actorProfile = actorProfile;
        }

    }
}