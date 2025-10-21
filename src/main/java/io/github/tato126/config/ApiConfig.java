package io.github.tato126.config;

import java.time.Duration;
import java.util.Objects;

/**
 * Configuration class for public data API settings.
 *
 * <p>This class manages API authentication and HTTP communication settings.
 * It is immutable and thread-safe, meaning it cannot be modified after creation.
 *
 * <p>The class uses the Builder pattern for flexible configuration:
 * <pre>{@code
 * // Basic usage with API key only
 * ApiConfig config = new ApiConfig("your-api-key");
 *
 * // Advanced usage with custom settings
 * ApiConfig config = ApiConfig.builder()
 *     .apiKey("your-api-key")
 *     .connectTimeout(Duration.ofSeconds(30))
 *     .readTimeout(Duration.ofSeconds(30))
 *     .build();
 * }</pre>
 *
 * @since 1.0.0
 * @author chan
 */
public final class ApiConfig {

    /**
     * Default base URL for Seoul Open Data subway API.
     */
    private static final String DEFAULT_URL = "http://swopenAPI.seoul.go.kr/api/subway";

    /**
     * Default connection timeout (10 seconds).
     */
    private static final Duration DEFAULT_CONNECT_TIMEOUT = Duration.ofSeconds(10);

    /**
     * Default read timeout (10 seconds).
     */
    private static final Duration DEFAULT_READ_TIMEOUT = Duration.ofSeconds(10);

    private final String apiKey;
    private final String baseUrl;
    private final Duration connectTimeout;
    private final Duration readTimeout;

    /**
     * Constructs an ApiConfig with the specified API key and default settings.
     *
     * <p>This constructor uses default values for:
     * <ul>
     *   <li>Base URL: {@value DEFAULT_URL}</li>
     *   <li>Connect timeout: 10 seconds</li>
     *   <li>Read timeout: 10 seconds</li>
     * </ul>
     *
     * @param apiKey the API authentication key (required, must not be null or empty)
     * @throws IllegalArgumentException if apiKey is null or empty
     */
    public ApiConfig(String apiKey) {
        this(apiKey, DEFAULT_URL, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT);
    }

    /**
     * Constructs an ApiConfig with all settings specified.
     *
     * <p>This constructor is private and used internally by the builder.
     *
     * @param apiKey the API authentication key
     * @param baseUrl the base URL for subway API
     * @param connectTimeout the connection timeout duration
     * @param readTimeout the read timeout duration
     * @throws IllegalArgumentException if any required parameter is invalid
     */
    private ApiConfig(String apiKey, String baseUrl, Duration connectTimeout, Duration readTimeout) {
        validateApiKey(apiKey);
        validateTimeout(connectTimeout, "Connect timeout");
        validateTimeout(readTimeout, "Read timeout");

        this.apiKey = apiKey;
        this.baseUrl = Objects.requireNonNull(baseUrl, "baseUrl must not be null");
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
    }

    /**
     * Validates the API key.
     *
     * @param apiKey the API key to validate
     * @throws IllegalArgumentException if the API key is null or empty
     */
    private void validateApiKey(String apiKey) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "API key is required. Please obtain one from https://data.seoul.go.kr");
        }
    }

    /**
     * Validates a timeout duration.
     *
     * @param timeout the timeout to validate
     * @param name the name of the timeout (for error messages)
     * @throws IllegalArgumentException if the timeout is null, negative, or zero
     */
    private void validateTimeout(Duration timeout, String name) {
        Objects.requireNonNull(timeout, name + " must not be null");
        if (timeout.isNegative() || timeout.isZero()) {
            throw new IllegalArgumentException(name + " must be positive");
        }
    }

    /**
     * Creates a new builder instance for constructing an ApiConfig.
     *
     * @return a new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Returns the API authentication key.
     *
     * @return the API key
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Returns the base URL for the subway API.
     *
     * @return the subway API base URL
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Returns the connection timeout duration.
     *
     * @return the connection timeout
     */
    public Duration getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * Returns the read timeout duration.
     *
     * @return the read timeout
     */
    public Duration getReadTimeout() {
        return readTimeout;
    }

    /**
     * Returns a string representation of this configuration.
     *
     * <p>The API key is masked for security, showing only the last 4 characters.
     *
     * @return a string representation of this config
     */
    @Override
    public String toString() {
        // Mask API key for security (show only last 4 characters)
        String maskedKey = "***" + apiKey.substring(Math.max(0, apiKey.length() - 4));

        return "ApiConfig{" +
                "apiKey='" + maskedKey + '\'' +
                ", baseUrl='" + baseUrl + '\'' +
                ", connectTimeout=" + connectTimeout.toMillis() + "ms" +
                ", readTimeout=" + readTimeout.toMillis() + "ms" +
                '}';
    }

    /**
     * Builder class for constructing ApiConfig instances.
     *
     * <p>This builder follows the fluent API pattern, allowing method chaining:
     * <pre>{@code
     * ApiConfig config = ApiConfig.builder()
     *     .apiKey("your-key")
     *     .connectTimeout(Duration.ofSeconds(30))
     *     .readTimeout(Duration.ofSeconds(30))
     *     .build();
     * }</pre>
     */
    public static class Builder {

        private String apiKey;
        private String baseUrl = DEFAULT_URL;
        private Duration connectTimeout = DEFAULT_CONNECT_TIMEOUT;
        private Duration readTimeout = DEFAULT_READ_TIMEOUT;

        /**
         * Private constructor to enforce builder creation through {@link ApiConfig#builder()}.
         */
        private Builder() {}

        /**
         * Sets the API authentication key (required).
         *
         * @param apiKey the API authentication key
         * @return this builder for method chaining
         */
        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        /**
         * Sets the subway API base URL (optional).
         *
         * <p>Default: {@value DEFAULT_URL}
         *
         * @param baseUrl the base URL for subway API
         * @return this builder for method chaining
         */
        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
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
            this.connectTimeout = connectTimeout;
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
            this.readTimeout = readTimeout;
            return this;
        }

        /**
         * Builds an ApiConfig instance with the configured settings.
         *
         * @return a new ApiConfig instance
         * @throws IllegalArgumentException if any required value is missing or invalid
         */
        public ApiConfig build() {
            return new ApiConfig(apiKey, baseUrl, connectTimeout, readTimeout);
        }
    }
}
