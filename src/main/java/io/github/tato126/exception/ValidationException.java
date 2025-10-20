package io.github.tato126.exception;

/**
 * Exception thrown when input validation fails.
 *
 * <p>This exception indicates problems with request parameters or input data, such as:
 * <ul>
 *   <li>Missing required parameters (ERROR-300)</li>
 *   <li>Invalid parameter format (ERROR-301)</li>
 *   <li>Parameter value out of valid range (ERROR-331, ERROR-332)</li>
 *   <li>Invalid parameter type (ERROR-333)</li>
 *   <li>Logical validation failures (ERROR-334)</li>
 * </ul>
 *
 * <p>Validation exceptions indicate client-side errors that should be corrected
 * before retrying the request.
 *
 * <p>Example usage:
 * <pre>{@code
 * try {
 *     client.subway().getRealtimeArrival("");  // Empty station name
 * } catch (ValidationException e) {
 *     System.err.println("Validation error: " + e.getMessage());
 *     if (e.getErrorCode() != null) {
 *         System.err.println("Error code: " + e.getErrorCode());
 *     }
 *     // Fix the input and retry
 * }
 * }</pre>
 *
 * @since 1.0.0
 * @author chan
 * @see ApiException
 */
public class ValidationException extends ApiException {

    /**
     * Constructs a new validation exception with the specified detail message.
     *
     * @param message the detail message explaining the validation failure
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Constructs a new validation exception with message and error code.
     *
     * <p>The error code corresponds to API validation error responses like
     * "ERROR-300" for missing required values or "ERROR-331" for invalid
     * start index values.
     *
     * @param message the detail message explaining the validation failure
     * @param errorCode the API error code (e.g., "ERROR-300", "ERROR-331")
     */
    public ValidationException(String message, String errorCode) {
        super(message, errorCode);
    }
}
