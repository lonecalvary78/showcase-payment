package dev.lonecalvary78.app.payment.internal.processing.facade;

import dev.lonecalvary78.app.payment.internal.processing.model.dto.PaymentDTO;

public interface PaymentFacade {
    public PaymentDTO processPayment(PaymentDTO paymentDTO);
}
