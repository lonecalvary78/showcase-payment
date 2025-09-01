package dev.lonecalvary78.app.payment.internal.processing.exception;

public class ApplicationException extends Exception {
    private final ErrorCode errorCode;
    public ApplicationException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }
}
