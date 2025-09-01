package dev.lonecalvary78.app.payment.internal.bankaccount.api.response;

import jakarta.json.bind.annotation.JsonbPropertyOrder;

@JsonbPropertyOrder({"code","message"})
public record ErrorResponse(String code, String message) {
    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(code, message);
    }
}
