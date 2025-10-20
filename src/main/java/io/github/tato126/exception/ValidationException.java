package io.github.tato126.exception;

/**
 * java doc
 *
 * @author chan
 */
public class ValidationException extends ApiException {

    // 메시지로 예외 생성
    public ValidationException(String message) {
        super(message);
    }

    // 메시지와 에러 코드로 예외 생성
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

}
