package dev.lonecalvary78.app.payment.model.dto;

public record AccountBalanceDTO(
    String accountNo,
    Double availableBalance
) {}
