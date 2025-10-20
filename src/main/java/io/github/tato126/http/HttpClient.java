package io.github.tato126.http;

import io.github.tato126.config.ApiConfig;
import io.github.tato126.exception.NetworkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * HTTP client wrapper for simplified HTTP communication.
 *
 * <p>This class wraps Java 21's built-in {@link java.net.http.HttpClient}
 * to provide a simpler, more focused interface for making HTTP GET requests
 * to the public data APIs.
 *
 * <p>Key features:
 * <ul>
 *   <li>Automatic timeout management (connection and read timeouts)</li>
 *   <li>Comprehensive error handling and exception translation</li>
 *   <li>Detailed logging for debugging and monitoring</li>
 *   <li>Thread-safe and reusable</li>
 * </ul>
 *
 * <p>This client throws {@link NetworkException} for all network-related errors,
 * providing a consistent exception handling experience.
 *
 * <p>Example usage:
 * <pre>{@code
 * ApiConfig config = new ApiConfig("your-api-key");
 * HttpClient client = new HttpClient(config);
 * String response = client.get("https://api.example.com/data");
 * }</pre>
 *
 * @since 1.0.0
 * @author chan
 * @see ApiConfig
 * @see NetworkException
 */
public class HttpClient {

    private static final Logger log = LoggerFactory.getLogger(HttpClient.class);

    private final java.net.http.HttpClient client;
    private final Duration connectTimeout;
    private final Duration readTimeout;

    /**
     * Constructs a new HTTP client with the specified configuration.
     *
     * <p>This constructor initializes the underlying Java {@link java.net.http.HttpClient}
     * with the timeout settings from the provided configuration.
     *
     * <p>The client is configured with:
     * <ul>
     *   <li>Connection timeout: Maximum time to establish a connection</li>
     *   <li>Read timeout: Maximum time to wait for response after connection</li>
     * </ul>
     *
     * @param config the API configuration containing timeout settings
     * @throws NullPointerException if config is null
     * @see ApiConfig#getConnectTimeout()
     * @see ApiConfig#getReadTimeout()
     */
    public HttpClient(ApiConfig config) {
        if (config == null) {
            throw new NullPointerException("config is null");
        }

        this.connectTimeout = config.getConnectTimeout();
        this.readTimeout = config.getReadTimeout();

        this.client = java.net.http.HttpClient.newBuilder()
                .connectTimeout(connectTimeout)
                .build();

        log.debug("Httpclient initialized with connectTimeout={}ms, readTimeout={}ms", connectTimeout.toMillis(), readTimeout.toMillis());
    }

    /**
     * Performs an HTTP GET request to the specified URL.
     *
     * <p>This method sends a GET request to the specified URL and returns the
     * response body as a string. The request is subject to the read timeout
     * configured in the constructor.
     *
     * <p>The method performs the following steps:
     * <ol>
     *   <li>Validates the URL is not null or empty</li>
     *   <li>Builds an HTTP GET request with timeout</li>
     *   <li>Sends the request and waits for response</li>
     *   <li>Validates HTTP status code (must be 2xx)</li>
     *   <li>Returns response body as string</li>
     * </ol>
     *
     * <p>Error handling:
     * <ul>
     *   <li>{@link IOException}: Network connectivity issues (timeout, connection failure)</li>
     *   <li>{@link InterruptedException}: Thread interruption during request</li>
     *   <li>{@link IllegalArgumentException}: Invalid URL format</li>
     *   <li>Non-2xx status codes: HTTP errors (4xx, 5xx)</li>
     * </ul>
     *
     * @param url the URL to send the GET request to
     * @return the response body as a string
     * @throws IllegalArgumentException if the URL is null or empty
     * @throws NetworkException if network error occurs, request is interrupted,
     *         URL is invalid, or HTTP status code is not in the 2xx range
     * @see java.net.http.HttpClient#send(HttpRequest, HttpResponse.BodyHandler)
     */
    public String get(String url) {

        // Valid URL
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("URL must not be null or empty");
        }

        log.debug("Sending GET request to {}", url);

        try {

            // Build Http Get request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(readTimeout)
                    .GET()
                    .build();

            // Send request and receive response
            // HttpResponse.BodyHandlers.ofString() converts response body to String format
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Log response details (status code and content length for debugging)
            log.debug("Received response status={}, contentLength={} bytes", response.statusCode(), response.body().length());

            // Check Http status code
            int statusCode = response.statusCode();

            if (statusCode < 200 || statusCode >= 300) {
                throw new NetworkException(String.format("HTTP request failed with status=%d: %s", statusCode, url));
            }

            return response.body();
        } catch (IOException e) {
            // Handles network errors (connection failure, timeout, etc.)
            log.error("Network error while requesting: {}", url, e);
            throw new NetworkException("Network error: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            // Handles thread interruption during HTTP request
            Thread.currentThread().interrupt(); // Restore interrupt status
            log.error("Request interrupted: {}", url, e);
            throw new NetworkException("Request was interrupted: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            // Handles invalid URL format
            log.error("Invalid URL: {}", url, e);
            throw new NetworkException("Invalid URL: " + e.getMessage(), e);
        }
    }

    /**
     * Returns the connection timeout duration.
     *
     * <p>This is the maximum time allowed to establish a connection
     * with the remote server.
     *
     * @return the connection timeout duration
     */
    public Duration getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * Returns the read timeout duration.
     *
     * <p>This is the maximum time allowed to wait for response data
     * after the connection has been established.
     *
     * @return the read timeout duration
     */
    public Duration getReadTimeout() {
        return readTimeout;
    }
}
