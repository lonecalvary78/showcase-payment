package dev.lonecalvary78.app.payment.internal.bankaccount.model.dto;

import jakarta.json.bind.annotation.JsonbPropertyOrder;

@JsonbPropertyOrder({"accountNo","availableBalance"})
public record AccountBalanceDTO(
    String accountNo,
    Double availableBalance
) {
    public static AccountBalanceDTO of(
        String accountNo,
        Double availableBalance) {
            return new AccountBalanceDTO(accountNo, availableBalance);
    }
}
