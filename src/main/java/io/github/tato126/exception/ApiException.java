package io.github.tato126.exception;

/**
 * java doc
 *
 * @author chan
 */
public class ApiException extends RuntimeException {

    private final String errorCode;

    // 메시지만으로 예외 생성
    public ApiException(String message) {
        super(message);
        this.errorCode = null;
    }

    // 메시지와 에러 원인로 예외 생성
    public ApiException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = null;
    }

    // 메시지와 에러 코드로 예외 생성
    public ApiException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    // 메시지, 에러 코드, 원인으로 예외 생성
    public ApiException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    // api 에러 코드 반환
    public String getErrorCode() {
        return errorCode;
    }

    // toString
    @Override
    public String toString() {

        if (errorCode != null) {
            return String.format("%s [%s]: %s", getClass().getSimpleName(), errorCode, super.toString());
        }

        return super.toString();
    }
}
