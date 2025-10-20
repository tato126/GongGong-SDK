package io.github.tato126.model;

import java.util.Arrays;
import java.util.Optional;

/**
 * Enumeration of Seoul subway lines with their IDs and display names.
 *
 * <p>This enum represents all subway lines operating in Seoul, including
 * Seoul Metro lines (1-9), suburban lines, and GTX lines. Each line has
 * a unique ID assigned by the Seoul public data API and a Korean display name.
 *
 * <p>Subway line IDs are defined by the Seoul Open API:
 * <ul>
 *   <li>1001-1009: Seoul Metro Lines 1-9</li>
 *   <li>1061-1081: Suburban lines (Jungang, Gyeongui-Jungang, etc.)</li>
 *   <li>1032: GTX-A (Great Train Express)</li>
 * </ul>
 *
 * <p>Example usage:
 * <pre>{@code
 * SubwayLine line = SubwayLine.fromId(1002);  // LINE_2
 * System.out.println(line.getDisplayName());  // "2호선"
 * System.out.println(line.getId());           // 1002
 * }</pre>
 *
 * @since 1.0.0
 * @author chan
 * @see <a href="https://data.seoul.go.kr/dataList/OA-12764/F/1/datasetView.do">Seoul Subway API</a>
 */
public enum SubwayLine {

    /**
     * Seoul Metro Line 1
     */
    LINE_1(1001, "1호선"),

    /**
     * Seoul Metro Line 2
     */
    LINE_2(1002, "2호선"),

    /**
     * Seoul Metro Line 3
     */
    LINE_3(1003, "3호선"),

    /**
     * Seoul Metro Line 4
     */
    LINE_4(1004, "4호선"),

    /**
     * Seoul Metro Line 5
     */
    LINE_5(1005, "5호선"),

    /**
     * Seoul Metro Line 6
     */
    LINE_6(1006, "6호선"),

    /**
     * Seoul Metro Line 7
     */
    LINE_7(1007, "7호선"),

    /**
     * Seoul Metro Line 8
     */
    LINE_8(1008, "8호선"),

    /**
     * Seoul Metro Line 9
     */
    LINE_9(1009, "9호선"),

    /**
     * Jungang Line
     */
    JUNGANG(1061, "중앙선"),

    /**
     * Gyeongui-Jungang Line
     */
    GYEONGUI_JUNGANG(1063, "경의중앙선"),

    /**
     * Airport Railroad Express (AREX)
     */
    AIRPORT(1065, "공항철도"),

    /**
     * Gyeongchun Line
     */
    GYEONGCHUN(1067, "경춘선"),

    /**
     * Suin-Bundang Line
     */
    SUIN_BUNDANG(1075, "수인분당선"),

    /**
     * Shinbundang Line
     */
    SINBUNDANG(1077, "신분당선"),

    /**
     * Ui-Sinseol Line
     */
    UI_SINSEOL(1092, "우이신설선"),

    /**
     * Seohae Line
     */
    SEOHAE(1093, "서해선"),

    /**
     * Gyeonggang Line
     */
    GYEONGGANG(1081, "경강선"),

    /**
     * GTX-A (Great Train Express A)
     */
    GTX_A(1032, "GTX-A");

    private final int id;
    private final String displayName;

    /**
     * Constructs a subway line with the specified ID and display name.
     *
     * @param id the unique subway line ID from Seoul Open API
     * @param displayName the Korean display name of the subway line
     */
    SubwayLine(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    /**
     * Returns the unique subway line ID.
     *
     * <p>This ID is used in the Seoul Open API to identify subway lines.
     *
     * @return the subway line ID (e.g., 1001 for Line 1)
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the Korean display name of the subway line.
     *
     * @return the display name (e.g., "1호선" for Line 1)
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Finds a subway line by its ID.
     *
     * <p>This method searches for a subway line with the given ID and returns
     * it wrapped in an {@link Optional}. If no matching line is found, an
     * empty Optional is returned.
     *
     * @param id the subway line ID to search for
     * @return an Optional containing the subway line if found, empty otherwise
     */
    public static Optional<SubwayLine> fromId(int id) {
        return Arrays.stream(values())
                .filter(subwayLine -> subwayLine.id == id)
                .findFirst();
    }

    /**
     * Returns a string representation of this subway line.
     *
     * @return a string in the format "displayName (id)"
     */
    @Override
    public String toString() {
        return String.format("%s (%d)", displayName, id);
    }
}
