package io.github.tato126.model;

import java.util.Arrays;
import java.util.Optional;

/**
 * Enumeration of train arrival status codes.
 *
 * <p>This enum represents the various stages of a train's arrival at a station,
 * from approaching to departing. The codes are defined by the Seoul Open API
 * and indicate the current status of a train in relation to a specific station.
 *
 * <p>Arrival codes:
 * <ul>
 *   <li>0 (ENTERING): Train is entering the station</li>
 *   <li>1 (ARRIVED): Train has arrived at the station</li>
 *   <li>2 (DEPARTED): Train has departed from the station</li>
 *   <li>3 (PREV_DEPARTED): Train departed from the previous station</li>
 *   <li>4 (PREV_ENTERING): Train is entering the previous station</li>
 *   <li>5 (PREV_ARRIVED): Train arrived at the previous station</li>
 *   <li>99 (RUNNING): Train is currently running between stations</li>
 * </ul>
 *
 * <p>Example usage:
 * <pre>{@code
 * ArrivalCode code = ArrivalCode.fromCode(0);  // ENTERING
 * System.out.println(code.getDescription());   // "진입"
 * 
 * if (code == ArrivalCode.ARRIVED) {
 *     System.out.println("Train has arrived!");
 * }
 * }</pre>
 *
 * @since 1.0.0
 * @author chan
 * @see <a href="https://data.seoul.go.kr/dataList/OA-12764/F/1/datasetView.do">Seoul Subway API</a>
 */
public enum ArrivalCode {

    /**
     * Train is entering the station (진입)
     */
    ENTERING(0, "진입"),

    /**
     * Train has arrived at the station (도착)
     */
    ARRIVED(1, "도착"),

    /**
     * Train has departed from the station (출발)
     */
    DEPARTED(2, "출발"),

    /**
     * Train has departed from the previous station (전역출발)
     */
    PREV_DEPARTED(3, "전역출발"),

    /**
     * Train is entering the previous station (전역진입)
     */
    PREV_ENTERING(4, "전역진입"),

    /**
     * Train has arrived at the previous station (전역도착)
     */
    PREV_ARRIVED(5, "전역도착"),

    /**
     * Train is running between stations (운행중)
     */
    RUNNING(99, "운행중");

    private final int code;

    private final String description;

    /**
     * Constructs an arrival code with the specified code and description.
     *
     * @param code the numeric code from Seoul Open API
     * @param description the Korean description of the arrival status
     */
    ArrivalCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Returns the numeric arrival code.
     *
     * <p>This code is used in the Seoul Open API responses.
     *
     * @return the arrival code (0-5, or 99)
     */
    public int getCode() {
        return code;
    }

    /**
     * Returns the Korean description of the arrival status.
     *
     * @return the description (e.g., "진입" for ENTERING)
     */
    public String getDescription() {
        return description;
    }

    /**
     * Finds an arrival code by its numeric code.
     *
     * <p>This method searches for an arrival code with the given numeric value
     * and returns it wrapped in an {@link Optional}. If no matching code is found,
     * an empty Optional is returned.
     *
     * @param code the numeric arrival code to search for
     * @return an Optional containing the arrival code if found, empty otherwise
     */
    public static Optional<ArrivalCode> fromCode(int code) {
        return Arrays.stream(values())
                .filter(arrivalCode -> arrivalCode.code == code)
                .findFirst();
    }

    /**
     * Returns a string representation of this arrival code.
     *
     * @return a string in the format "description (code)"
     */
    @Override
    public String toString() {
        return String.format("%s (%d)", description, code);
    }
}
