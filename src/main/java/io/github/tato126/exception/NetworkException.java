package io.github.tato126.exception;

/**
 * java doc
 *
 * @author chan
 */
public class NetworkException extends ApiException {

    // 메시지로 예외 생성
    public NetworkException(String message) {
        super(message);
    }

    // 메시지와 원인으로 예외 생성
    public NetworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
