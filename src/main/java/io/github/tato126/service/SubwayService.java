package io.github.tato126.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tato126.config.ApiConfig;
import io.github.tato126.exception.ApiException;
import io.github.tato126.http.HttpClient;
import io.github.tato126.model.SubwayArrival;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for subway real-time arrival information API.
 *
 * <p>This service provides a high-level interface for querying real-time
 * subway arrival information from the Seoul Open Data API. It handles:
 * <ul>
 *   <li>URL construction with proper encoding</li>
 *   <li>HTTP communication via {@link HttpClient}</li>
 *   <li>JSON response parsing and object mapping (using Jackson)</li>
 *   <li>Error handling and validation</li>
 * </ul>
 *
 * <p>Example usage:
 * <pre>{@code
 * ApiConfig config = new ApiConfig("your-api-key");
 * SubwayService service = new SubwayService(config);
 *
 * // Get arrivals for a station
 * List<SubwayArrival> arrivals = service.getRealtimeArrival("강남");
 *
 * // Print results
 * arrivals.forEach(arrival ->
 *     System.out.println(arrival.getStationName() + ": " +
 *                        arrival.getArrivalTime() + "초 후 도착")
 * );
 * }</pre>
 *
 * @author chan
 * @see SubwayArrival
 * @see ApiConfig
 * @see HttpClient
 * @since 1.0.0
 */
public class SubwayService {

    private static final Logger log = LoggerFactory.getLogger(SubwayService.class);

    private static final String SERVICE_NAME = "realtimeStationArrival";
    private static final String RESPONSE_FORMAT = "json";
    private static final int DEFAULT_START_INDEX = 0;
    private static final int DEFAULT_END_INDEX = 100;

    private final ApiConfig apiConfig;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    /**
     * Constructs a SubwayService with the specified configuration.
     *
     * @param config the API configuration containing API key and settings
     * @throws NullPointerException if config is null
     */
    public SubwayService(ApiConfig config) {

        if (config == null) {
            throw new NullPointerException("config must not be null");
        }

        this.apiConfig = config;
        this.httpClient = new HttpClient(config);
        this.objectMapper = new ObjectMapper();

        log.debug("SubwayService initialized with baseUrl={}", config.getBaseUrl());
    }

    /**
     * Retrieves real-time arrival information for the specified station.
     *
     * <p>This method queries the Seoul Open Data API for real-time subway
     * arrival information at the given station. It returns a list of upcoming
     * train arrivals, sorted by arrival time.
     *
     * <p>The station name should be in Korean (e.g., "강남", "서울역").
     * Note that some stations require specific names as documented in the
     * API guide (e.g., "공릉(서울산업대입구)" instead of "공릉").
     *
     * @param stationName the Korean name of the subway station
     * @return a list of subway arrivals, empty list if no arrivals found
     * @throws IllegalArgumentException if stationName is null or empty
     * @throws ApiException             if API returns an error or data is invalid
     * @see <a href="https://data.seoul.go.kr/dataList/OA-12764/F/1/datasetView.do">Seoul Subway API</a>
     */
    public List<SubwayArrival> getRealtimeArrival(String stationName) {
        return getRealtimeArrival(stationName, DEFAULT_START_INDEX, DEFAULT_END_INDEX);
    }

    /**
     * Retrieves real-time arrival information with pagination.
     *
     * <p>This overloaded method allows specifying the start and end index
     * for pagination. This is useful when:
     * <ul>
     *   <li>Using the sample API key (limited to 5 results)</li>
     *   <li>Limiting the number of results for performance</li>
     *   <li>Implementing pagination in your application</li>
     * </ul>
     *
     * @param stationName the Korean name of the subway station
     * @param startIndex  the starting index (0-based, inclusive)
     * @param endIndex    the ending index (inclusive, max 1000 results per request)
     * @return a list of subway arrivals
     * @throws IllegalArgumentException if parameters are invalid
     * @throws ApiException             if API returns an error
     */
    public List<SubwayArrival> getRealtimeArrival(String stationName, int startIndex, int endIndex) {

        // 파라미터 검증
        validateStationName(stationName);
        validatePagination(startIndex, endIndex);

        // url 생성
        String url = buildUrl(stationName, startIndex, endIndex);
        log.debug("Request URL: {}", url);

        // api 호출
        String jsonResponse = httpClient.get(url);
        log.debug("Received response: {}", jsonResponse.length());

        // json 파싱
        List<SubwayArrival> arrivals = parseResponse(jsonResponse);
        log.info("Successfully fetched {} arrivals for station '{}'", arrivals.size(), stationName);

        return arrivals;
    }

    /**
     * Builds the API request URL with proper encoding.
     *
     * <p>URL format: {baseUrl}/{apiKey}/{format}/{service}/{start}/{end}/{stationName}
     *
     * @param stationName the station name to query
     * @param startIndex  the start index for pagination
     * @param endIndex    the end index for pagination
     * @return the complete API URL
     * @throws ApiException if station name encoding fails
     */
    private String buildUrl(String stationName, int startIndex, int endIndex) {

        try {
            String encodedStationName = URLEncoder.encode(stationName, StandardCharsets.UTF_8.toString());

            // URL format:
            return String.format("%s/%s/%s/%s/%d/%d/%s",
                    apiConfig.getBaseUrl(),
                    apiConfig.getApiKey(),
                    RESPONSE_FORMAT,
                    SERVICE_NAME,
                    startIndex,
                    endIndex,
                    encodedStationName);
        } catch (UnsupportedEncodingException e) {
            throw new ApiException("Failed to encode station name: " + stationName, e);
        }
    }

    /**
     * Parses the JSON response and converts to SubwayArrival objects.
     *
     * <p>This method handles the Seoul Open Data API response format:
     * <pre>{@code
     * {
     *   "errorMessage": { ... },  // Error info (if present)
     *   "realtimeArrivalList": [  // Arrival data
     *     { "statnNm": "강남", "barvlDt": "120", ... }
     *   ]
     * }
     * }</pre>
     *
     * @param jsonResponse the raw JSON response from API
     * @return list of parsed SubwayArrival objects
     * @throws ApiException if JSON parsing fails or API returns an error
     */
    private List<SubwayArrival> parseResponse(String jsonResponse) {

        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            log.debug("Received response: {}", root);

            // 1. 에러 체크
            if (root.has("errorMessage")) {
                JsonNode error = root.get("errorMessage");
                String code = error.get("code").asText();
                String message = error.get("message").asText();

                if (!code.equals("INFO-000")) {
                    log.error("API error code:{}, message={}", code, message);
                    throw new ApiException(String.format("API error [%s]: %s", code, message));
                }

                log.error("API Response: code={}, message={}", code, message);
            }

            // 2. 도착 정보 리스트 확인
            if (!root.has("realtimeArrivalList")) {
                log.warn("No realtimeArrivalList in response, returning empty list");
                return new ArrayList<>();
            }

            // 3. 각 도착 정보를 SubWayArrival로 변환
            JsonNode arrivalList = root.get("realtimeArrivalList");
            List<SubwayArrival> results = new ArrayList<>();

            for (JsonNode arrivalNode : arrivalList) {
                SubwayArrival arrival = mapToSubwayArrival(arrivalNode);
                results.add(arrival);
            }

            return results;
        } catch (Exception e) {
            log.error("Failed to parse JSON response", e);
            throw new ApiException("Failed to parse JSON response" + e.getMessage(), e);
        }
    }

    /**
     * Maps a JSON node to a SubwayArrival instance.
     *
     * <p>This method converts the API's abbreviated field names to our
     * full field names:
     * <ul>
     *   <li>statnNm → stationName</li>
     *   <li>subwayId → subwayLineId</li>
     *   <li>barvlDt → arrivalTime</li>
     *   <li>etc...</li>
     * </ul>
     *
     * @param json the JSON node representing a single arrival
     * @return a SubwayArrival instance
     */
    private SubwayArrival mapToSubwayArrival(JsonNode json) {
        return SubwayArrival.builder()
                .subwayLineId(getTextOrEmpty(json, "subwayId"))           // API: subwayId
                .upDownLine(getTextOrEmpty(json, "updnLine"))             // API: updnLine
                .trainLineName(getTextOrEmpty(json, "trainLineNm"))       // API: trainLineNm
                .previousStationId(getTextOrEmpty(json, "statnFid"))      // API: statnFid
                .nextStationId(getTextOrEmpty(json, "statnTid"))          // API: statnTid
                .currentStationId(getTextOrEmpty(json, "statnId"))        // API: statnId
                .stationName(getTextOrEmpty(json, "statnNm"))             // API: statnNm
                .transferLineCount(getTextOrEmpty(json, "trnsitCo"))      // API: trnsitCo
                .arrivalOrderKey(getTextOrEmpty(json, "ordkey"))          // API: ordkey
                .connectedSubwayLineIds(getTextOrEmpty(json, "subwayList"))  // API: subwayList
                .connectedStationIds(getTextOrEmpty(json, "statnList"))   // API: statnList
                .trainStatus(getTextOrEmpty(json, "btrainSttus"))         // API: btrainSttus
                .arrivalTime(getTextOrEmpty(json, "barvlDt"))             // API: barvlDt
                .trainNumber(getTextOrEmpty(json, "btrainNo"))            // API: btrainNo
                .finalDestinationStationId(getTextOrEmpty(json, "bstatnId"))  // API: bstatnId
                .finalDestinationStationName(getTextOrEmpty(json, "bstatnNm"))  // API: bstatnNm
                .receivedTimestamp(getTextOrEmpty(json, "recptnDt"))      // API: recptnDt
                .firstArrivalMessage(getTextOrEmpty(json, "arvlMsg2"))    // API: arvlMsg2
                .secondArrivalMessage(getTextOrEmpty(json, "arvlMsg3"))   // API: arvlMsg3
                .arrivalStatusCode(getTextOrEmpty(json, "arvlCd"))        // API: arvlCd
                .isLastTrain(getTextOrEmpty(json, "lstcarAt"))            // API: lstcarAt
                .build();
    }

    /**
     * Safely extracts a string value from JSON node, returning empty string if not present.
     *
     * @param node      the JSON node to extract from
     * @param fieldName the field name to look for
     * @return the string value or empty string if null/missing
     */
    private String getTextOrEmpty(JsonNode node, String fieldName) {

        JsonNode field = node.get(fieldName);
        if (field != null && !field.isNull()) {
            return field.asText();
        }

        return "";
    }

    /**
     * Validates the station name parameter.
     *
     * @param stationName the station name to validate
     * @throws IllegalArgumentException if station name is null or empty
     */
    private void validateStationName(String stationName) {
        if (stationName == null || stationName.trim().isEmpty()) {
            throw new IllegalArgumentException("StationName must not be null or empty");
        }
    }

    /**
     * Validates pagination parameters.
     *
     * @param startIndex the start index (must be non-negative)
     * @param endIndex   the end index (must be >= startIndex and within 1000 range)
     * @throws IllegalArgumentException if pagination parameters are invalid
     */
    private void validatePagination(int startIndex, int endIndex) {
        if (startIndex < 0) {
            throw new IllegalArgumentException("Start index must be non-negative");
        }
        if (endIndex < startIndex) {
            throw new IllegalArgumentException("End index must be greater than or equal to start index");
        }
        if (endIndex - startIndex > 1000) {
            throw new IllegalArgumentException("Cannot request more than 1000 results at once");
        }
    }
}