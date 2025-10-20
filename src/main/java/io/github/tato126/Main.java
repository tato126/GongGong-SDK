package io.github.tato126;

import io.github.tato126.config.ApiConfig;
import io.github.tato126.model.SubwayArrival;
import io.github.tato126.service.SubwayService;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        // 1. api 설정
        ApiConfig config = new ApiConfig("sample");

        // 2. 서비스 생성
        SubwayService service = new SubwayService(config);

        // 3. 강남역 도착 정보 조회
        List<SubwayArrival> arrivals = service.getRealtimeArrival("강남", 0, 4);

        // 4. 결과 출력
        System.out.println("=== 강남역 도착 예정 열차 ===\n");

        arrivals.forEach(arrival -> {
            System.out.println("📍 현재 위치: " + arrival.getLocationStatus());
            System.out.println("🚇 호선: " + arrival.getSubwayLineId());
            System.out.println("🎯 종착역: " + arrival.getFinalDestinationStationName());
            System.out.println("⏰ 도착 예정: " + arrival.getArrivalTimeInMinutes() + "분 후 (" + arrival.getArrivalTime() + "초)");
            System.out.println("📢 상태: " + arrival.getFirstArrivalMessage());
            System.out.println("🔴 막차: " + (arrival.isLastTrainOfDay() ? "예" : "아니오"));
            System.out.println("---");
        });
    }
}