package kopis.k_backend.performance.service;

import kopis.k_backend.global.entity.Coordinates;
import kopis.k_backend.global.service.AddressService;
import kopis.k_backend.performance.domain.Performance;
import kopis.k_backend.performance.domain.PerformanceType;
import kopis.k_backend.performance.repository.PerformanceRepository;
import kopis.k_backend.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PerformanceRecommendService {
    private final PerformanceRepository performanceRepository;
    private final AddressService addressService;

    public List<Performance> getRecommendPerformance(Integer type, Integer startYear, Integer startMonth, Integer startDate, Integer endYear, Integer endMonth, Integer endDate, String location, Integer minPrice, Integer maxPrice) {
        // 공연 타입 필터링

        if (type == null) {
            throw new IllegalArgumentException("Performance type cannot be null");
        }
        PerformanceType perfType;
        if (type == 0) {
            perfType = PerformanceType.MUSICAL;
        } else if (type == 1) {
            perfType = PerformanceType.PLAY;
        } else {
            throw new IllegalArgumentException("Invalid performance type");
        }

        // 공연 관람 날짜 필터링
        String startDay = startYear + "." +
                String.format("%02d", startMonth) + "." +
                String.format("%02d", startDate);
        String endDay = endYear + "." +
                String.format("%02d", endMonth) + "." +
                String.format("%02d", endDate);


        // 필터링 조건에 맞는 공연 목록 조회
        List<Performance> performances =
                performanceRepository.findPerformancesByCriteria(
                perfType, endDay, startDay, location, minPrice, maxPrice
        );

        return performances;
    }

    public List<Performance> getTopRatedPerformances(List<Performance> performances) {
        // 리뷰와 별점 기준으로 내림차순 정렬
        return performances.stream()
                .sorted(Comparator.comparing(Performance::getRatingAverage).reversed()
                        .thenComparing(Comparator.comparing(Performance::getReviewCount).reversed()))
                .limit(10)  // 상위 10개 공연만 가져옴
                .collect(Collectors.toList());
    }

    public List<Performance> getPerformancesByFavoriteActors(User user) {
        return performanceRepository.findPerformancesByFavoriteActorsAndState(user.getId());
    }

    public List<Performance> getUpcomingPerformances(List<Performance> performances) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        return performances.stream()
                .filter(performance -> !LocalDate.parse(performance.getStartDate(), formatter).isBefore(today)) // 오늘 이후에 시작하는 공연만 필터링
                .sorted(Comparator.comparing(performance -> LocalDate.parse(performance.getStartDate(), formatter))) // 공연 시작 날짜가 오늘과 가까운 날짜 순으로 오름차순 정렬
                .limit(10)  // 상위 10개 공연만 가져옴
                .collect(Collectors.toList());
    }


    public List<Performance> getPerformancesWithUpcomingEndDate(List<Performance> performances) {
        LocalDate today = LocalDate.now();  // 현재 날짜를 가져옵니다.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");  // 날짜 형식을 지정합니다.

        return performances.stream()
                .filter(performance -> !LocalDate.parse(performance.getEndDate(), formatter).isBefore(today))  // 종료 날짜가 오늘과 같거나 이후인 공연
                .sorted(Comparator.comparing(performance -> LocalDate.parse(performance.getEndDate(), formatter)))  // 공연 종료 날짜가 오늘과 가장 가까운 순으로 오름차순 정렬
                .limit(10)  // 상위 10개 공연만 가져옵니다.
                .collect(Collectors.toList());
    }

    public List<Performance> getPerformancesSortedByDistance(User user, List<Performance> performances) {
        // 사용자 주소 좌표 변환
        Coordinates userCoordinates = addressService.getCoordinate(user.getAddress());

        return performances.stream()
                .map(performance -> {
                    // 공연장 주소 좌표 변환
                    Coordinates hallCoordinates = addressService.getCoordinate(performance.getHall().getStreetAddress());

                    // 거리 계산
                    double distance = AddressService.calculateDistance(
                            Double.parseDouble(userCoordinates.getY()), Double.parseDouble(userCoordinates.getX()),
                            Double.parseDouble(hallCoordinates.getY()), Double.parseDouble(hallCoordinates.getX())
                    );

                    // 거리와 함께 공연 반환
                    return new PerformanceWithDistance(performance, distance);
                })
                .sorted(Comparator.comparingDouble(PerformanceWithDistance::getDistance)) // 거리 순으로 정렬
                .limit(10) // 상위 10개 공연 선택
                .map(PerformanceWithDistance::getPerformance) // Performance 객체만 반환
                .collect(Collectors.toList());
    }

    // 내부 클래스 또는 별도의 파일로 이동 가능
    @Getter
    private static class PerformanceWithDistance {
        private final Performance performance;
        private final double distance;

        public PerformanceWithDistance(Performance performance, double distance) {
            this.performance = performance;
            this.distance = distance;
        }

    }


}
