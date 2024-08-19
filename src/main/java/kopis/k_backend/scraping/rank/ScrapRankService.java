package kopis.k_backend.scraping.rank;

import kopis.k_backend.job.Job;
import kopis.k_backend.job.JobRepository;
import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.performance.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScrapRankService {
    // 인터파크: 65 % / yes24: 20 % / melon: 8 % / 티켓 링크: 7 %
    private final PerformanceRepository performanceRepository;
    private final JobRepository jobRepository;

    LocalDate today = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.M.d");

    private void scrapeRankEveryDay() {
        String start = today.format(formatter);
        String type = "SCRAPE_INTERPARK_RANKING";
        Job jobEntity = new Job(start, "-", "IN_PROGRESS", type);
        jobRepository.save(jobEntity);

        // 스크래핑 작업
        Map<Long, Integer> scoreMap = new HashMap<>();
        //interparkRankings(scoreMap); // 인터파크 x
        //yes24Rankings(scoreMap); // yes24
        //melonRankings(scoreMap); // 멜론 8 - 4위까지

        // 최종 점수 결과를 로그로 출력 (또는 DB에 저장 등)
        for (Map.Entry<Long, Integer> entry : scoreMap.entrySet()) {
            log.info("공연 ID: " + entry.getKey() + ", 점수: " + entry.getValue());
        }

        LocalDate now = LocalDate.now();
        jobEntity.setStatus("COMPLETED");
        jobEntity.setEnd(now.format(formatter));
        jobRepository.save(jobEntity); // 완료
    }


/*    // 인터파크 공연들 점수 매기고 저장
    private void interparkRankings(Map<Long, Integer> scoreMap) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            String url = "https://tickets.interpark.com/contents/ranking?genre=MUSICAL";
            driver.get(url);

            Thread.sleep(5000); // 페이지가 로드되기 위해 잠시 대기 (필요에 따라 조정)

            List<PerformanceInfo> rankings = scrapeInterparkRankings(driver);
            assignScores(rankings, scoreMap, 65); // 인터파크는 점수에 65 곱하기
        } catch (Exception e) {
            log.error("인터파크 스크래핑 중 오류 발생: ", e);
        } finally {
            driver.quit();
        }
    }

    // 인터파크 스크래핑
    private List<PerformanceInfo> scrapeInterparkRankings(WebDriver driver) {
        List<PerformanceInfo> rankings = new ArrayList<>();

        // 스크래핑
        List<WebElement> elements = driver.findElements(By.cssSelector(".responsive-ranking-list_rankingItem__PuQJP")); // 개수가 50개 출력되나
        for (WebElement element : elements) {
            try {
                System.out.println("Processing element: " + element);

                String title = extractTitleFromBrackets(element.findElement(By.cssSelector(".responsive-ranking-list_goodsName__aHHGY")).getText().trim());
                System.out.println("Title: " + title);

                String theater = element.findElement(By.cssSelector(".responsive-ranking-list_placeName__9HN2O")).getText().trim();
                System.out.println("Theater: " + theater);

                String startDate = element.findElement(By.cssSelector(".responsive-ranking-list_dateWrap__jBu5n li")).getText().trim();
                System.out.println("Start Date: " + startDate);

                String endDate = element.findElements(By.cssSelector(".responsive-ranking-list_dateWrap__jBu5n li")).get(1).getText().trim();
                System.out.println("End Date: " + endDate);

                if (!title.isEmpty() && !theater.isEmpty() && !startDate.isEmpty() && !endDate.isEmpty()) {
                    String dateRange = startDate + " ~ " + endDate;
                    rankings.add(parsePerformanceInfo(title, theater, dateRange));
                } else {
                    System.out.println("Incomplete data found: " + title + " " + theater + " " + startDate + " ~ " + endDate);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error processing element: " + element);
            }
        }

        System.out.println("rankings" + rankings);
        return rankings;
    }*/

    /*// Yes24 공연들 점수 매기고 저장
    private void yes24Rankings(Map<Long, Integer> scoreMap) {
        try {
            String url = "http://ticket.yes24.com/New/Rank/Ranking.aspx?genre=15457";
            Document doc = Jsoup.connect(url).get();

            List<PerformanceInfo> rankings = scrapeYes24Rankings(doc);
            assignScores(rankings, scoreMap, 65); // Yes24 점수에 65 곱하기
        } catch (Exception e) {
            System.err.println("Yes24 스크래핑 중 오류 발생: " + e.getMessage());
        }
    }

    // Yes24 스크래핑
    private List<PerformanceInfo> scrapeYes24Rankings(Document doc) {
        List<PerformanceInfo> rankings = new ArrayList<>();
        System.out.println("Full HTML content:\n" + doc.outerHtml());

        // 1위부터 3위까지 스크래핑
        Elements top3Elements = doc.select(".rank-best");
        for (Element element : top3Elements) {
            try {
                String title = element.select(".rlb-tit").text().trim();
                String dateRange = element.select(".rlb-sub-tit").text().trim();
                String theater = element.select(".rank-best-sub").text().trim();
                System.out.println(title + " " + theater + " " + dateRange);

                if (!title.isEmpty() && !dateRange.isEmpty() && !theater.isEmpty()) {
                    rankings.add(parsePerformanceInfo(title, theater, dateRange));
                } else {
                    System.out.println("Incomplete data found: " + title + " " + theater + " " + dateRange);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error processing element: " + element);
            }
        }

        // 4위부터 10위까지 스크래핑
        Elements elements = doc.select(".rank-list-tit");
        int endIndex = Math.min(elements.size(), 7);  // 요소의 개수를 확인하고 최대 7개까지만 처리
        for (int i = 0; i < endIndex; i++) {
            Element element = elements.get(i);
            try {
                String title = element.select("a").text().trim();
                Element parent = element.parent();
                String dateRange = parent.select(".rank-list-grade + div p").text().trim();
                String theater = parent.select(".rank-list-grade + div p + p").text().trim();
                System.out.println(title + " " + theater + " " + dateRange);

                if (!title.isEmpty() && !dateRange.isEmpty() && !theater.isEmpty()) {
                    rankings.add(parsePerformanceInfo(title, theater, dateRange));
                } else {
                    System.out.println("Incomplete data found: " + title + " " + theater + " " + dateRange);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error processing element: " + element);
            }
        }

        System.out.println("Rankings: " + rankings);
        return rankings;
    }*/


    /*   // 멜론 공연들 점수 매기고 저장
    private void melonRankings(Map<Long, Integer> scoreMap) {
        try {
            String url = "https://ticket.melon.com/concert/index.htm?genreType=GENRE_ART";
            Document doc = Jsoup.connect(url).get();

            List<PerformanceInfo> rankings = scrapeMelonRankings(doc);
            assignScores(rankings, scoreMap, 10); // 멜론은 점수에 65 곱하기
        } catch (Exception e) {
            System.err.println("멜론 스크래핑 중 오류 발생: " + e.getMessage());
        }
    }

    // 멜론 스크래핑 4위 까지 가능
    private List<PerformanceInfo> scrapeMelonRankings(Document doc) {
        List<PerformanceInfo> rankings = new ArrayList<>();
        System.out.println("Full HTML content:\n" + doc.outerHtml());

        // 스크래핑
        Elements elements = doc.select(".box_ranking_list > ul > li"); // 랭킹 리스트 요소 선택

        for (Element element : elements) {
            try {
                String title = extractTitleFromBrackets(element.select(".title").text().trim());
                String theater = element.select(".location").text().trim();
                String dateRange = element.select(".day").text().trim();
                System.out.println(title + " " + theater + " " + dateRange);

                if (!title.isEmpty() && !theater.isEmpty() && !dateRange.isEmpty()) {
                    rankings.add(parsePerformanceInfo(title, theater, dateRange));
                } else {
                    System.out.println("Incomplete data found: " + title + " " + theater + " " + dateRange);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error processing element: " + element);
            }
        }

        System.out.println("Rankings: " + rankings);
        return rankings;
    }*/

    // 정규식을 이용해 <> 사이의 텍스트 추출
    private String extractTitleFromBrackets(String fullTitle) {
        Pattern pattern = Pattern.compile("〈(.*?)〉");
        Matcher matcher = pattern.matcher(fullTitle);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return fullTitle; // <> 사이의 텍스트가 없으면 전체 타이틀 반환
    }

    private PerformanceInfo parsePerformanceInfo(String title, String theater, String dateRange) {
        String[] dates = dateRange.split("~");

        // 시작 날짜 처리
        String startDateStr = dates[0].trim();
        LocalDate startDate = parseDateWithOptionalYear(startDateStr, null);

        // startDate의 연도 가져오기
        int startYear = startDate.getYear();
        System.out.println("Start Year: " + startYear);

        // 종료 날짜 처리
        String endDateStr = dates[1].trim();
        LocalDate endDate = parseDateWithOptionalYear(endDateStr, String.valueOf(startYear));

        return new PerformanceInfo(title, theater, startDate, endDate);
    }

    private LocalDate parseDateWithOptionalYear(String dateStr, String year) {
        if (dateStr.matches("\\d{1,2}\\.\\d{1,2}")) {
            if (year == null) {
                year = String.valueOf(LocalDate.now().getYear());
            }
            dateStr = year + "." + dateStr;
        }
        return LocalDate.parse(dateStr, formatter);
    }

    private void assignScores(List<PerformanceInfo> rankings, Map<Long, Integer> scoreMap, double multiplier) {
        int[] baseScores = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};

        for (int i = 0; i < rankings.size(); i++) {
            PerformanceInfo info = rankings.get(i);
            Optional<Performance> matchedPerformance = matchPerformance(info);

            if (matchedPerformance.isPresent()) {
                Long performanceId = matchedPerformance.get().getId();
                int score = (int) (baseScores[i] * multiplier);
                scoreMap.put(performanceId, score);
            }
        }
    }

    private Optional<Performance> matchPerformance(PerformanceInfo performanceInfo) {
        List<Performance> matchedPerformances = new ArrayList<>();
        int maxTitleLcsLength = 0;

        for (Performance performance : performanceRepository.findAll()) {
            int titleLcsLength = longestCommonSubstring(performance.getTitle(), performanceInfo.getTitle());
            if (titleLcsLength > maxTitleLcsLength) {
                maxTitleLcsLength = titleLcsLength;
                matchedPerformances.clear();
                matchedPerformances.add(performance);
            } else if (titleLcsLength == maxTitleLcsLength) {
                matchedPerformances.add(performance);
            }
        }

        List<Performance> finalMatches = new ArrayList<>();
        int maxHallLcsLength = 0;
        for (Performance performance : matchedPerformances) {
            String hallName = performance.getHall().getHallName();
            int hallLcsLength = longestCommonSubstring(hallName, performanceInfo.getTheater());

            if (hallLcsLength > maxHallLcsLength) {
                maxHallLcsLength = hallLcsLength;
                finalMatches.clear();
                finalMatches.add(performance);
            } else if (hallLcsLength == maxHallLcsLength) {
                finalMatches.add(performance);
            }
        }

        for (Performance performance : finalMatches) {
            if (isDateMatch(LocalDate.parse(performance.getStartDate(), formatter), LocalDate.parse(performance.getEndDate(), formatter), performanceInfo.getStartDate(), performanceInfo.getEndDate())) {
                log.info("Matched Performance: " + performance.getTitle());
                return Optional.of(performance);
            }
        }
        return Optional.empty();
    }

    private boolean isDateMatch(LocalDate dbStartDate, LocalDate dbEndDate, LocalDate scrapedStartDate, LocalDate scrapedEndDate) {
        return !dbStartDate.isAfter(scrapedStartDate.plusDays(3)) && !dbEndDate.isBefore(scrapedEndDate.minusDays(3));
    }

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

    private static class PerformanceInfo {
        private final String title;
        private final String theater;
        private final LocalDate startDate;
        private final LocalDate endDate;

        public PerformanceInfo(String title, String theater, LocalDate startDate, LocalDate endDate) {
            this.title = title;
            this.theater = theater;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public String getTitle() {
            return title;
        }

        public String getTheater() {
            return theater;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }
    }
}
