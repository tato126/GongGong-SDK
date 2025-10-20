package io.github.tato126.exception;

/**
 * Base exception class for all GongGong SDK exceptions.
 *
 * <p>All exceptions thrown by the SDK inherit from this class, providing
 * a consistent exception hierarchy and error handling mechanism.
 *
 * <p>This exception can carry an optional error code that corresponds to
 * the API's error response codes (e.g., INFO-100, ERROR-300).
 *
 * <p>Example usage:
 * <pre>{@code
 * try {
 *     client.subway().getRealtimeArrival("Gangnam");
 * } catch (ApiException e) {
 *     System.err.println("Error: " + e.getMessage());
 *     if (e.getErrorCode() != null) {
 *         System.err.println("Error code: " + e.getErrorCode());
 *     }
 * }
 * }</pre>
 *
 * @since 1.0.0
 * @author chan
 * @see NetworkException
 * @see AuthenticationException
 * @see ValidationException
 */
public class ApiException extends RuntimeException {

    /**
     * The error code returned by the API, if available.
     * This corresponds to codes like INFO-100, ERROR-300, etc.
     */
    private final String errorCode;

    /**
     * Constructs a new API exception with the specified detail message.
     *
     * @param message the detail message explaining the exception
     */
    public ApiException(String message) {
        super(message);
        this.errorCode = null;
    }

    /**
     * Constructs a new API exception with the specified detail message and cause.
     *
     * <p>This constructor is useful for wrapping lower-level exceptions
     * (e.g., IOException) with additional context.
     *
     * @param message the detail message explaining the exception
     * @param cause the underlying cause of this exception
     */
    public ApiException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = null;
    }

    /**
     * Constructs a new API exception with the specified detail message and error code.
     *
     * <p>The error code should match the API's error response codes
     * (e.g., "INFO-100" for invalid authentication key).
     *
     * @param message the detail message explaining the exception
     * @param errorCode the API error code (e.g., "INFO-100", "ERROR-300")
     */
    public ApiException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Constructs a new API exception with message, error code, and cause.
     *
     * <p>This is the most comprehensive constructor, useful when wrapping
     * exceptions while preserving both the API error code and the original cause.
     *
     * @param message the detail message explaining the exception
     * @param errorCode the API error code (e.g., "INFO-100", "ERROR-300")
     * @param cause the underlying cause of this exception
     */
    public ApiException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Returns the API error code associated with this exception.
     *
     * @return the error code, or {@code null} if no error code was provided
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Returns a string representation of this exception.
     *
     * <p>If an error code is present, it will be included in the format:
     * {@code ExceptionName [ERROR_CODE]: message}
     *
     * @return a string representation including the error code if available
     */
    @Override
    public String toString() {
        if (errorCode != null) {
            return String.format("%s [%s]: %s",
                getClass().getSimpleName(), errorCode, getMessage());
        }
        return super.toString();
    }
}
