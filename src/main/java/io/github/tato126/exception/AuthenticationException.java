package io.github.tato126.exception;

/**
 * java doc
 *
 * @author chan
 */
public class AuthenticationException extends ApiException {

    // 메시지와 에러 코드로 예외 생성
    public AuthenticationException(String message, String errorCode) {
        super(message, errorCode);
    }

    // 메시지, 예러 코드, 원인으로 예외 생성
    public AuthenticationException(String message, String errorCode, Throwable cause) {
        super(message, errorCode, cause);
    }
}
