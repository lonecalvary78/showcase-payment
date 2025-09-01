package dev.lonecalvary78.app.payment.internal.processing.model.dto;

import java.util.UUID;

public record PaymentDTO(
    UUID id,
    String debittedAccountNo,
    String creditedAccountNo,
    Double creditedAmount,
    String paymentPurpose,
    String paymentReference,
    String status) {}
