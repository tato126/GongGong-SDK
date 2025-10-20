package io.github.tato126.exception;

/**
 * Exception thrown when network-related errors occur during API communication.
 *
 * <p>This exception indicates problems with network connectivity, such as:
 * <ul>
 *   <li>Connection timeout</li>
 *   <li>Read timeout</li>
 *   <li>DNS resolution failure</li>
 *   <li>Connection refused</li>
 *   <li>Network unreachable</li>
 * </ul>
 *
 * <p>Network exceptions are typically retryable, as they may be caused by
 * temporary network issues.
 *
 * <p>Example usage:
 * <pre>{@code
 * try {
 *     client.subway().getRealtimeArrival("Seoul");
 * } catch (NetworkException e) {
 *     System.err.println("Network error: " + e.getMessage());
 *     // Consider retrying the request
 * }
 * }</pre>
 *
 * @since 1.0.0
 * @author chan
 * @see ApiException
 */
public class NetworkException extends ApiException {

    /**
     * Constructs a new network exception with the specified detail message.
     *
     * @param message the detail message explaining the network error
     */
    public NetworkException(String message) {
        super(message);
    }

    /**
     * Constructs a new network exception with the specified detail message and cause.
     *
     * <p>This constructor is typically used to wrap lower-level network exceptions
     * (e.g., {@link java.net.SocketTimeoutException}, {@link java.io.IOException})
     * with additional context.
     *
     * @param message the detail message explaining the network error
     * @param cause the underlying network exception that caused this error
     */
    public NetworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
