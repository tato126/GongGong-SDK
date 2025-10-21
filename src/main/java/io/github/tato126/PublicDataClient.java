package io.github.tato126;

import io.github.tato126.config.ApiConfig;
import io.github.tato126.service.SubwayService;

import java.time.Duration;

/**
 * Main entry point for the GongGong SDK.
 *
 * <p>This class provides a simplified facade for accessing various Korean public data APIs.
 * It manages configuration and service initialization internally, offering a cleaner API
 * compared to working with individual service classes directly.
 *
 * <p>Basic usage with API key:
 * <pre>{@code
 * PublicDataClient client = new PublicDataClient("your-api-key");
 * List<SubwayArrival> arrivals = client.subway().getRealtimeArrival("강남", 0, 5);
 * }</pre>
 *
 * <p>Advanced usage with custom configuration:
 * <pre>{@code
 * PublicDataClient client = PublicDataClient.builder()
 *     .apiKey("your-api-key")
 *     .connectTimeout(Duration.ofSeconds(30))
 *     .readTimeout(Duration.ofSeconds(30))
 *     .build();
 *
 * List<SubwayArrival> arrivals = client.subway().getRealtimeArrival("강남");
 * }</pre>
 *
 * @since 1.0.0
 * @author chan
 */
public class PublicDataClient {

    private final ApiConfig config;
    private SubwayService subwayService;

    /**
     * Constructs a PublicDataClient with the specified API key.
     *
     * <p>Uses default settings for timeouts and base URLs.
     *
     * @param apiKey the API authentication key (required, must not be null or empty)
     * @throws IllegalArgumentException if apiKey is null or empty
     */
    public PublicDataClient(String apiKey) {
        this(new ApiConfig(apiKey));
    }

    /**
     * Constructs a PublicDataClient with the specified configuration.
     *
     * @param config the API configuration (required, must not be null)
     * @throws IllegalArgumentException if config is null
     */
    public PublicDataClient(ApiConfig config) {

        if (config == null) {
            throw new IllegalArgumentException("ApiConfig must not be null");
        }
        this.config = config;
    }

    /**
     * Returns a SubwayService instance for accessing Seoul Metro real-time arrival data.
     *
     * <p>The service instance is lazily initialized and cached for reuse.
     *
     * @return a SubwayService instance
     */
    public SubwayService subway() {

        if (subwayService == null) {
            subwayService = new SubwayService(config);
        }
        return subwayService;
    }

    /**
     * Creates a new builder instance for constructing a PublicDataClient.
     *
     * @return a new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Returns the underlying API configuration.
     *
     * @return the API configuration
     */
    public ApiConfig getConfig() {
        return config;
    }

    /**
     * Builder class for constructing PublicDataClient instances with custom configuration.
     *
     * <p>This builder follows the fluent API pattern, allowing method chaining:
     * <pre>{@code
     * PublicDataClient client = PublicDataClient.builder()
     *     .apiKey("your-key")
     *     .connectTimeout(Duration.ofSeconds(30))
     *     .readTimeout(Duration.ofSeconds(30))
     *     .build();
     * }</pre>
     */
    public static class Builder {

        private final ApiConfig.Builder configBuilder;

        /**
         * Private constructor to enforce builder creation through {@link PublicDataClient#builder()}.
         */
        private Builder() {
            this.configBuilder = ApiConfig.builder();
        }

        /**
         * Sets the API authentication key (required).
         *
         * @param apiKey the API authentication key
         * @return this builder for method chaining
         */
        public Builder apiKey(String apiKey) {
            configBuilder.apiKey(apiKey);
            return this;
        }

        /**
         * Sets the subway API base URL (optional).
         *
         * <p>Default: Seoul Open Data subway API URL
         *
         * @param baseUrl the base URL for subway API
         * @return this builder for method chaining
         */
        public Builder baseUrl(String baseUrl) {
            configBuilder.baseUrl(baseUrl);
            return this;
        }

        /**
         * Sets the connection timeout (optional).
         *
         * <p>Default: 10 seconds
         *
         * @param connectTimeout the connection timeout duration
         * @return this builder for method chaining
         */
        public Builder connectTimeout(Duration connectTimeout) {
            configBuilder.connectTimeout(connectTimeout);
            return this;
        }

        /**
         * Sets the read timeout (optional).
         *
         * <p>Default: 10 seconds
         *
         * @param readTimeout the read timeout duration
         * @return this builder for method chaining
         */
        public Builder readTimeout(Duration readTimeout) {
            configBuilder.readTimeout(readTimeout);
            return this;
        }

        /**
         * Builds a PublicDataClient instance with the configured settings.
         *
         * @return a new PublicDataClient instance
         * @throws IllegalArgumentException if any required value is missing or invalid
         */
        public PublicDataClient build() {
            return new PublicDataClient(configBuilder.build());
        }
    }
}
