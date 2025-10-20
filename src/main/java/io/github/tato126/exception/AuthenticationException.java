package io.github.tato126.exception;

/**
 * Exception thrown when API authentication or authorization fails.
 *
 * <p>This exception indicates problems with API credentials or permissions, such as:
 * <ul>
 *   <li>Invalid API key (INFO-100)</li>
 *   <li>Expired API key</li>
 *   <li>Insufficient permissions</li>
 *   <li>API key not activated</li>
 * </ul>
 *
 * <p>Authentication exceptions are typically not retryable and require
 * user intervention to correct the API key or credentials.
 *
 * <p>Example usage:
 * <pre>{@code
 * try {
 *     client.subway().getRealtimeArrival("Gangnam");
 * } catch (AuthenticationException e) {
 *     System.err.println("Authentication failed: " + e.getMessage());
 *     System.err.println("Error code: " + e.getErrorCode());
 *     // Prompt user to check their API key
 * }
 * }</pre>
 *
 * @since 1.0.0
 * @author chan
 * @see ApiException
 */
public class AuthenticationException extends ApiException {

    /**
     * Constructs a new authentication exception with message and error code.
     *
     * <p>The error code typically corresponds to API error responses like "INFO-100"
     * for invalid authentication keys.
     *
     * @param message the detail message explaining the authentication failure
     * @param errorCode the API error code (e.g., "INFO-100")
     */
    public AuthenticationException(String message, String errorCode) {
        super(message, errorCode);
    }

    /**
     * Constructs a new authentication exception with message, error code, and cause.
     *
     * <p>This constructor is useful when wrapping lower-level exceptions while
     * preserving the API error code.
     *
     * @param message the detail message explaining the authentication failure
     * @param errorCode the API error code (e.g., "INFO-100")
     * @param cause the underlying cause of this exception
     */
    public AuthenticationException(String message, String errorCode, Throwable cause) {
        super(message, errorCode, cause);
    }
}
