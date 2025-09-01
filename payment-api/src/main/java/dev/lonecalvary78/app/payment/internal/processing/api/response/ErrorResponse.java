package dev.lonecalvary78.app.payment.internal.processing.api.response;

public record ErrorResponse(String code, String message) {
    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(code, message);
    }
}
