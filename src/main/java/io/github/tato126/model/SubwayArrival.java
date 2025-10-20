package io.github.tato126.model;

import java.util.Objects;

/**
 * Subway real-time arrival information class.
 *
 * <p>This class represents real-time subway arrival information provided by
 * Seoul Transportation Operation & Information Service (TOPIS). It is designed
 * as an immutable object and should be created using the Builder pattern.
 *
 * <p>Key information categories:
 * <ul>
 *   <li>Line information: subwayLineId, upDownLine, connectedSubwayLineIds</li>
 *   <li>Station information: stationName, currentStationId, previousStationId, nextStationId</li>
 *   <li>Train information: trainNumber, trainStatus, trainLineName</li>
 *   <li>Arrival information: arrivalTime, arrivalStatusCode, firstArrivalMessage, secondArrivalMessage</li>
 * </ul>
 *
 * <p>Example usage:
 * <pre>{@code
 * SubwayArrival arrival = SubwayArrival.builder()
 *     .subwayLineId("1002")
 *     .stationName("강남")
 *     .arrivalTime("120")
 *     .arrivalStatusCode("0")
 *     .build();
 * }</pre>
 *
 * <p><strong>Important:</strong> The {@code receivedTimestamp} field requires time correction.
 * The difference between the current time and receivedTimestamp indicates how much
 * further the train has progressed. See the API guide for details.
 *
 * @since 1.0.0
 * @author chan
 * @see SubwayLine
 * @see ArrivalCode
 * @see <a href="https://data.seoul.go.kr/dataList/OA-12764/F/1/datasetView.do">Seoul Subway API</a>
 */
public class SubwayArrival {

    /**
     * Subway line ID (e.g., 1001 for Line 1, 1002 for Line 2)
     * TODO: Consider adding getSubwayLine() method to convert to SubwayLine enum
     */
    private final String subwayLineId;

    /**
     * Up/down line indicator (상행/내선, 하행/외선)
     */
    private final String upDownLine;

    /**
     * Train destination line name (destination station - next station)
     */
    private final String trainLineName;

    /**
     * Previous station ID
     */
    private final String previousStationId;

    /**
     * Next station ID
     */
    private final String nextStationId;

    /**
     * Current station ID
     */
    private final String currentStationId;

    /**
     * Station name (e.g., "강남")
     */
    private final String stationName;

    /**
     * Number of transfer lines available at this station
     * TODO: Consider changing type from String to int
     */
    private final String transferLineCount;

    /**
     * Arrival order key for sorting trains
     */
    private final String arrivalOrderKey;

    /**
     * Connected subway line IDs (comma-separated, e.g., "1002,1007")
     */
    private final String connectedSubwayLineIds;

    /**
     * Connected station IDs (comma-separated)
     */
    private final String connectedStationIds;

    /**
     * Train status/type (급행, ITX, 일반, 특급)
     */
    private final String trainStatus;

    /**
     * Estimated arrival time in seconds
     * TODO: Consider changing type from String to int for easier calculations
     */
    private final String arrivalTime;

    /**
     * Train number currently running on this line
     */
    private final String trainNumber;

    /**
     * Final destination station ID
     */
    private final String finalDestinationStationId;

    /**
     * Final destination station name
     */
    private final String finalDestinationStationName;

    /**
     * Timestamp when the arrival information was generated.
     * <strong>Warning:</strong> Time correction required - adjust for the difference
     * between current time and this timestamp.
     */
    private final String receivedTimestamp;

    /**
     * First arrival message (e.g., "도착", "출발", "진입")
     */
    private final String firstArrivalMessage;

    /**
     * Second arrival message (e.g., "종합운동장 도착", "12분 후 (광명사거리)")
     */
    private final String secondArrivalMessage;

    /**
     * Arrival status code:
     * 0 (진입), 1 (도착), 2 (출발), 3 (전역출발), 4 (전역진입), 5 (전역도착), 99 (운행중)
     * TODO: Consider changing type from String to int, or add getArrivalCode() method for ArrivalCode enum conversion
     */
    private final String arrivalStatusCode;

    /**
     * Last train indicator: "1" (last train), "0" (not last train)
     * TODO: Consider changing type from String to boolean for more intuitive usage
     */
    private final String isLastTrain;

    /**
     * Private constructor for Builder pattern.
     */
    private SubwayArrival(Builder builder) {
        this.subwayLineId = builder.subwayLineId;
        this.upDownLine = builder.upDownLine;
        this.trainLineName = builder.trainLineName;
        this.previousStationId = builder.previousStationId;
        this.nextStationId = builder.nextStationId;
        this.currentStationId = builder.currentStationId;
        this.stationName = builder.stationName;
        this.transferLineCount = builder.transferLineCount;
        this.arrivalOrderKey = builder.arrivalOrderKey;
        this.connectedSubwayLineIds = builder.connectedSubwayLineIds;
        this.connectedStationIds = builder.connectedStationIds;
        this.trainStatus = builder.trainStatus;
        this.arrivalTime = builder.arrivalTime;
        this.trainNumber = builder.trainNumber;
        this.finalDestinationStationId = builder.finalDestinationStationId;
        this.finalDestinationStationName = builder.finalDestinationStationName;
        this.receivedTimestamp = builder.receivedTimestamp;
        this.firstArrivalMessage = builder.firstArrivalMessage;
        this.secondArrivalMessage = builder.secondArrivalMessage;
        this.arrivalStatusCode = builder.arrivalStatusCode;
        this.isLastTrain = builder.isLastTrain;
    }

    /**
     * Creates a new Builder instance.
     *
     * @return a new Builder for constructing SubwayArrival instances
     */
    public static Builder builder() {
        return new Builder();
    }

    // Getters

    /**
     * Returns the subway line ID.
     *
     * @return the subway line ID (e.g., "1002" for Line 2)
     */
    public String getSubwayLineId() {
        return subwayLineId;
    }

    /**
     * Returns the up/down line indicator.
     *
     * @return the direction (상행/내선 or 하행/외선)
     */
    public String getUpDownLine() {
        return upDownLine;
    }

    /**
     * Returns the train destination line name.
     *
     * @return the destination description
     */
    public String getTrainLineName() {
        return trainLineName;
    }

    /**
     * Returns the previous station ID.
     *
     * @return the previous station ID
     */
    public String getPreviousStationId() {
        return previousStationId;
    }

    /**
     * Returns the next station ID.
     *
     * @return the next station ID
     */
    public String getNextStationId() {
        return nextStationId;
    }

    /**
     * Returns the current station ID.
     *
     * @return the current station ID
     */
    public String getCurrentStationId() {
        return currentStationId;
    }

    /**
     * Returns the station name.
     *
     * @return the station name (e.g., "강남")
     */
    public String getStationName() {
        return stationName;
    }

    /**
     * Returns the number of transfer lines.
     *
     * @return the transfer line count
     */
    public String getTransferLineCount() {
        return transferLineCount;
    }

    /**
     * Returns the arrival order key.
     *
     * @return the order key for sorting arrivals
     */
    public String getArrivalOrderKey() {
        return arrivalOrderKey;
    }

    /**
     * Returns the connected subway line IDs.
     *
     * @return comma-separated line IDs (e.g., "1002,1007")
     */
    public String getConnectedSubwayLineIds() {
        return connectedSubwayLineIds;
    }

    /**
     * Returns the connected station IDs.
     *
     * @return comma-separated station IDs
     */
    public String getConnectedStationIds() {
        return connectedStationIds;
    }

    /**
     * Returns the train status/type.
     *
     * @return the train status (급행, ITX, 일반, 특급)
     */
    public String getTrainStatus() {
        return trainStatus;
    }

    /**
     * Returns the estimated arrival time in seconds.
     *
     * @return the arrival time in seconds
     */
    public String getArrivalTime() {
        return arrivalTime;
    }

    /**
     * Returns the train number.
     *
     * @return the current train number
     */
    public String getTrainNumber() {
        return trainNumber;
    }

    /**
     * Returns the final destination station ID.
     *
     * @return the final destination station ID
     */
    public String getFinalDestinationStationId() {
        return finalDestinationStationId;
    }

    /**
     * Returns the final destination station name.
     *
     * @return the final destination station name
     */
    public String getFinalDestinationStationName() {
        return finalDestinationStationName;
    }

    /**
     * Returns the timestamp when this information was generated.
     * <strong>Note:</strong> Time correction may be required.
     *
     * @return the received timestamp
     */
    public String getReceivedTimestamp() {
        return receivedTimestamp;
    }

    /**
     * Returns the first arrival message.
     *
     * @return the first message (e.g., "도착", "출발", "진입")
     */
    public String getFirstArrivalMessage() {
        return firstArrivalMessage;
    }

    /**
     * Returns the second arrival message.
     *
     * @return the second message with detailed information
     */
    public String getSecondArrivalMessage() {
        return secondArrivalMessage;
    }

    /**
     * Returns the arrival status code.
     *
     * @return the status code (0-5 or 99)
     * @see ArrivalCode
     */
    public String getArrivalStatusCode() {
        return arrivalStatusCode;
    }

    /**
     * Returns the last train indicator.
     *
     * @return "1" if this is the last train, "0" otherwise
     */
    public String getIsLastTrain() {
        return isLastTrain;
    }

    /**
     * Returns a human-readable description of the train's current location status.
     *
     * <p>Interprets the arrival status code to provide a clear description:
     * <ul>
     *   <li>Code 0: Entering the queried station</li>
     *   <li>Code 1: Arrived at the queried station</li>
     *   <li>Code 2: Departed from the queried station</li>
     *   <li>Code 3: Departed from the previous station</li>
     *   <li>Code 4: Entering the previous station</li>
     *   <li>Code 5: Arrived at the previous station</li>
     *   <li>Code 99: In operation</li>
     * </ul>
     *
     * @return a human-readable location status description
     */
    public String getLocationStatus() {
        if (arrivalStatusCode == null || arrivalStatusCode.isEmpty()) {
            return "알 수 없음";
        }

        switch (arrivalStatusCode) {
            case "0":
                return String.format("%s역 진입 중", stationName);
            case "1":
                return String.format("%s역 도착", stationName);
            case "2":
                return String.format("%s역 출발", stationName);
            case "3":
                return String.format("전역 출발 (다음: %s역)", stationName);
            case "4":
                return String.format("전역 진입 중 (다음: %s역)", stationName);
            case "5":
                String prevStation = secondArrivalMessage != null ? secondArrivalMessage : "전역";
                return String.format("%s 도착 (다음: %s역)", prevStation, stationName);
            case "99":
                return String.format("%s역 방향 운행 중", finalDestinationStationName != null ? finalDestinationStationName : "");
            default:
                return "알 수 없음";
        }
    }

    /**
     * Returns the estimated arrival time in minutes.
     *
     * @return arrival time in minutes (rounded up), or 0 if already arrived
     */
    public int getArrivalTimeInMinutes() {
        if (arrivalTime == null || arrivalTime.isEmpty()) {
            return 0;
        }
        try {
            int seconds = Integer.parseInt(arrivalTime);
            return (seconds + 59) / 60; // Round up
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Checks if this is the last train of the day.
     *
     * @return true if this is the last train, false otherwise
     */
    public boolean isLastTrainOfDay() {
        return "1".equals(isLastTrain);
    }

    /**
     * Returns a string representation of this subway arrival.
     *
     * @return a string with station name, line, and arrival time
     */
    @Override
    public String toString() {
        return String.format("SubwayArrival{station='%s', line='%s', arrivalTime='%s', status='%s'}",
                stationName, subwayLineId, arrivalTime, arrivalStatusCode);
    }

    /**
     * Compares this subway arrival to another object for equality.
     *
     * @param o the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubwayArrival that = (SubwayArrival) o;
        return Objects.equals(subwayLineId, that.subwayLineId) &&
                Objects.equals(currentStationId, that.currentStationId) &&
                Objects.equals(trainNumber, that.trainNumber) &&
                Objects.equals(arrivalOrderKey, that.arrivalOrderKey);
    }

    /**
     * Returns a hash code for this subway arrival.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(subwayLineId, currentStationId, trainNumber, arrivalOrderKey);
    }

    /**
     * Builder for constructing SubwayArrival instances.
     *
     * <p>Example usage:
     * <pre>{@code
     * SubwayArrival arrival = SubwayArrival.builder()
     *     .subwayLineId("1002")
     *     .stationName("강남")
     *     .arrivalTime("120")
     *     .build();
     * }</pre>
     */
    public static class Builder {
        private String subwayLineId;
        private String upDownLine;
        private String trainLineName;
        private String previousStationId;
        private String nextStationId;
        private String currentStationId;
        private String stationName;
        private String transferLineCount;
        private String arrivalOrderKey;
        private String connectedSubwayLineIds;
        private String connectedStationIds;
        private String trainStatus;
        private String arrivalTime;
        private String trainNumber;
        private String finalDestinationStationId;
        private String finalDestinationStationName;
        private String receivedTimestamp;
        private String firstArrivalMessage;
        private String secondArrivalMessage;
        private String arrivalStatusCode;
        private String isLastTrain;

        private Builder() {
        }

        public Builder subwayLineId(String subwayLineId) {
            this.subwayLineId = subwayLineId;
            return this;
        }

        public Builder upDownLine(String upDownLine) {
            this.upDownLine = upDownLine;
            return this;
        }

        public Builder trainLineName(String trainLineName) {
            this.trainLineName = trainLineName;
            return this;
        }

        public Builder previousStationId(String previousStationId) {
            this.previousStationId = previousStationId;
            return this;
        }

        public Builder nextStationId(String nextStationId) {
            this.nextStationId = nextStationId;
            return this;
        }

        public Builder currentStationId(String currentStationId) {
            this.currentStationId = currentStationId;
            return this;
        }

        public Builder stationName(String stationName) {
            this.stationName = stationName;
            return this;
        }

        public Builder transferLineCount(String transferLineCount) {
            this.transferLineCount = transferLineCount;
            return this;
        }

        public Builder arrivalOrderKey(String arrivalOrderKey) {
            this.arrivalOrderKey = arrivalOrderKey;
            return this;
        }

        public Builder connectedSubwayLineIds(String connectedSubwayLineIds) {
            this.connectedSubwayLineIds = connectedSubwayLineIds;
            return this;
        }

        public Builder connectedStationIds(String connectedStationIds) {
            this.connectedStationIds = connectedStationIds;
            return this;
        }

        public Builder trainStatus(String trainStatus) {
            this.trainStatus = trainStatus;
            return this;
        }

        public Builder arrivalTime(String arrivalTime) {
            this.arrivalTime = arrivalTime;
            return this;
        }

        public Builder trainNumber(String trainNumber) {
            this.trainNumber = trainNumber;
            return this;
        }

        public Builder finalDestinationStationId(String finalDestinationStationId) {
            this.finalDestinationStationId = finalDestinationStationId;
            return this;
        }

        public Builder finalDestinationStationName(String finalDestinationStationName) {
            this.finalDestinationStationName = finalDestinationStationName;
            return this;
        }

        public Builder receivedTimestamp(String receivedTimestamp) {
            this.receivedTimestamp = receivedTimestamp;
            return this;
        }

        public Builder firstArrivalMessage(String firstArrivalMessage) {
            this.firstArrivalMessage = firstArrivalMessage;
            return this;
        }

        public Builder secondArrivalMessage(String secondArrivalMessage) {
            this.secondArrivalMessage = secondArrivalMessage;
            return this;
        }

        public Builder arrivalStatusCode(String arrivalStatusCode) {
            this.arrivalStatusCode = arrivalStatusCode;
            return this;
        }

        public Builder isLastTrain(String isLastTrain) {
            this.isLastTrain = isLastTrain;
            return this;
        }

        /**
         * Builds the SubwayArrival instance.
         *
         * @return a new SubwayArrival instance
         */
        public SubwayArrival build() {
            return new SubwayArrival(this);
        }
    }
}