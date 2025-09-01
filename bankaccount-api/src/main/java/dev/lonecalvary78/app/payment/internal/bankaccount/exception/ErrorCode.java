package dev.lonecalvary78.app.payment.internal.bankaccount.exception;

public enum ErrorCode {
    MISSING_ACCOUNT_NUMBER("BANKACCOUNT-ERR-001","Missing Account Number",403),
    ACCOUNT_NUMBER_NOT_FOUND("BANKACCOUNT-ERR-002","Bank Account Number is not found", 403);

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
