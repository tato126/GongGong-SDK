\# 공공 데이터 포털 Java SDK 프로젝트

## 프로젝트 개요
한국 공공데이터포털(data.go.kr)의 API를 쉽게 사용할 수 있도록 만드는 Java SDK

## 목표
- 복잡한 API 호출을 간단하게
- 여러 공공 데이터 API를 하나의 라이브러리로 통합
- 초보자도 쉽게 사용할 수 있는 인터페이스 제공

## 1단계: 프로젝트 셋업

### 디렉토리 구조
```
public-data-sdk/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── publicdata/
│   │               ├── PublicDataClient.java (메인 클래스)
│   │               ├── config/
│   │               │   └── ApiConfig.java (설정)
│   │               ├── api/
│   │               │   ├── WeatherAPI.java (날씨 API)
│   │               │   └── BusAPI.java (버스 API)
│   │               ├── model/
│   │               │   ├── Weather.java (날씨 데이터 모델)
│   │               │   └── Bus.java (버스 데이터 모델)
│   │               └── util/
│   │                   ├── HttpClient.java (HTTP 통신)
│   │                   └── XmlParser.java (XML 파싱)
│   └── test/
│       └── java/
├── pom.xml (Maven 설정)
└── README.md
```

### 필요한 라이브러리
```xml
<!-- pom.xml -->
<dependencies>
    <!-- HTTP 통신 -->
    <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>httpclient</artifactId>
        <version>4.5.14</version>
    </dependency>
    
    <!-- JSON 파싱 -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.15.2</version>
    </dependency>
    
    <!-- XML 파싱 -->
    <dependency>
        <groupId>com.fasterxml.jackson.dataformat</groupId>
        <artifactId>jackson-dataformat-xml</artifactId>
        <version>2.15.2</version>
    </dependency>
    
    <!-- 테스트 -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## 2단계: 핵심 클래스 구현

### 2.1 메인 클라이언트 (PublicDataClient.java)
```java
package com.publicdata;

import com.publicdata.api.WeatherAPI;
import com.publicdata.config.ApiConfig;

public class PublicDataClient {
    private final ApiConfig config;
    private final WeatherAPI weatherAPI;
    
    public PublicDataClient(String apiKey) {
        this.config = new ApiConfig(apiKey);
        this.weatherAPI = new WeatherAPI(config);
    }
    
    public WeatherAPI weather() {
        return weatherAPI;
    }
}
```

### 2.2 설정 클래스 (ApiConfig.java)
```java
package com.publicdata.config;

public class ApiConfig {
    private final String apiKey;
    private final String baseUrl = "http://apis.data.go.kr";
    
    public ApiConfig(String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalArgumentException("API Key는 필수입니다");
        }
        this.apiKey = apiKey;
    }
    
    public String getApiKey() {
        return apiKey;
    }
    
    public String getBaseUrl() {
        return baseUrl;
    }
}
```

### 2.3 HTTP 클라이언트 (HttpClient.java)
```java
package com.publicdata.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {
    
    public String get(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        
        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new Exception("HTTP 오류: " + responseCode);
        }
        
        BufferedReader in = new BufferedReader(
            new InputStreamReader(conn.getInputStream(), "UTF-8")
        );
        StringBuilder response = new StringBuilder();
        String line;
        
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();
        
        return response.toString();
    }
}
```

## 3단계: 날씨 API 구현

### 3.1 날씨 데이터 모델 (Weather.java)
```java
package com.publicdata.model;

public class Weather {
    private String location;
    private double temperature;
    private String description;
    private int humidity;
    
    // Constructor
    public Weather(String location, double temperature, String description, int humidity) {
        this.location = location;
        this.temperature = temperature;
        this.description = description;
        this.humidity = humidity;
    }
    
    // Getters
    public String getLocation() { return location; }
    public double getTemperature() { return temperature; }
    public String getDescription() { return description; }
    public int getHumidity() { return humidity; }
    
    @Override
    public String toString() {
        return String.format("Weather{location='%s', temp=%.1f°C, desc='%s', humidity=%d%%}",
            location, temperature, description, humidity);
    }
}
```

### 3.2 날씨 API 클래스 (WeatherAPI.java)
```java
package com.publicdata.api;

import com.publicdata.config.ApiConfig;
import com.publicdata.model.Weather;
import com.publicdata.util.HttpClient;

public class WeatherAPI {
    private final ApiConfig config;
    private final HttpClient httpClient;
    
    public WeatherAPI(ApiConfig config) {
        this.config = config;
        this.httpClient = new HttpClient();
    }
    
    public Weather getCurrentWeather(String city) throws Exception {
        // URL 생성
        String url = buildWeatherUrl(city);
        
        // HTTP 요청
        String response = httpClient.get(url);
        
        // 응답 파싱 (실제로는 XML/JSON 파싱 필요)
        return parseWeatherResponse(response, city);
    }
    
    private String buildWeatherUrl(String city) {
        return String.format("%s/1360000/VilageFcstInfoService/getVilageFcst?serviceKey=%s&numOfRows=10&pageNo=1&dataType=JSON",
            config.getBaseUrl(),
            config.getApiKey());
    }
    
    private Weather parseWeatherResponse(String response, String city) {
        // 간단한 파싱 (실제로는 JSON 라이브러리 사용)
        // 여기서는 데모용으로 더미 데이터 반환
        return new Weather(city, 20.5, "맑음", 65);
    }
}
```

## 4단계: 사용 예제

### 기본 사용법
```java
public class Main {
    public static void main(String[] args) {
        try {
            // 1. 클라이언트 생성
            PublicDataClient client = new PublicDataClient("YOUR_API_KEY");
            
            // 2. 날씨 조회
            Weather weather = client.weather().getCurrentWeather("서울");
            
            // 3. 결과 출력
            System.out.println(weather);
            // 출력: Weather{location='서울', temp=20.5°C, desc='맑음', humidity=65%}
            
        } catch (Exception e) {
            System.err.println("오류 발생: " + e.getMessage());
        }
    }
}
```

## 5단계: 공공데이터포털 API 키 발급

1. https://www.data.go.kr 접속
2. 회원가입 및 로그인
3. "오픈API" 메뉴 선택
4. 원하는 API 검색 (예: "동네예보조회서비스")
5. "활용신청" 버튼 클릭
6. 인증키 발급 (일반적으로 즉시 발급)

## 6단계: 확장 가능한 API들

### 추가 가능한 API 목록
- **BusAPI**: 버스 도착 정보
- **SubwayAPI**: 지하철 시간표
- **AirQualityAPI**: 미세먼지 정보
- **RealEstateAPI**: 부동산 실거래가
- **TourAPI**: 관광지 정보

### 확장 예시 (BusAPI)
```java
public class BusAPI {
    public List<Bus> getBusArrival(String busNumber) {
        // 버스 도착 정보 조회
    }
}
```

## 개발 단계별 체크리스트

### Phase 1: 기본 구조 (1-2일)
- [ ] 프로젝트 생성 (Maven/Gradle)
- [ ] 디렉토리 구조 설정
- [ ] 기본 클래스 생성

### Phase 2: HTTP 통신 (1-2일)
- [ ] HttpClient 구현
- [ ] GET 요청 테스트
- [ ] 에러 처리

### Phase 3: 첫 번째 API (2-3일)
- [ ] 공공데이터 API 키 발급
- [ ] 날씨 API 연동
- [ ] 응답 파싱
- [ ] 테스트

### Phase 4: 개선 및 확장 (계속)
- [ ] 다른 API 추가
- [ ] 문서화
- [ ] 테스트 코드 작성
- [ ] 에러 처리 강화

## 학습 자료

### 필수 개념
- HTTP 요청/응답
- JSON/XML 파싱
- 예외 처리
- Maven 의존성 관리

### 참고 링크
- 공공데이터포털: https://www.data.go.kr
- Jackson JSON: https://github.com/FasterXML/jackson
- Apache HttpClient: https://hc.apache.org/

## 다음 단계

1. **먼저 해보기**: 공공데이터포털에서 API 키 발급받기
2. **간단하게 시작**: 날씨 API 하나만 연동해보기
3. **점진적 확장**: 다른 API 추가하기
4. **개선하기**: 에러 처리, 캐싱 등 추가

## 팁

- 처음엔 완벽하게 만들려고 하지 마세요
- 하나씩 단계별로 진행하세요
- 에러가 나면 로그를 꼭 확인하세요
- API 문서를 자주 참고하세요

---

**준비되셨나요? 이제 실제 코드를 작성해볼까요?**
