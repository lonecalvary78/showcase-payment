package dev.lonecalvary78.app.payment.model.dto;

public record PaymentRequestDTO(
    String debittedAccountNo,
    String creditedAccountNo,
    Double creditedAmount,
    String paymentPurpose,
    String paymentReference
) {}
