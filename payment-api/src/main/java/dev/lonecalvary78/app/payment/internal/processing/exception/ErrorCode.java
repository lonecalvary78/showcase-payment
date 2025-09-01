package dev.lonecalvary78.app.payment.internal.processing.exception;

public enum ErrorCode {
    MISSING_PAYMENT_REQUEST("PAYMENT-ERR-001","Missing payment request", 403);
 
    private final String code;
    private final String message;
    private final int responseStatus;

    ErrorCode(String code, String message, int responseStatus) {
        this.code = code;
        this.message = message;
        this.responseStatus = responseStatus;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public int getResponseStatus() {
        return this.responseStatus;
    }
}
