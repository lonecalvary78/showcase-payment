package dev.lonecalvary78.app.payment.internal.processing.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.lonecalvary78.app.payment.internal.processing.facade.PaymentFacade;
import dev.lonecalvary78.app.payment.internal.processing.facade.PaymentFacadeImpl;
import dev.lonecalvary78.app.payment.internal.processing.infra.circuitbreak.CircuitBreakUtil;
import dev.lonecalvary78.app.payment.internal.processing.model.dto.PaymentDTO;
import io.helidon.config.Config;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class PaymentService {
    private final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    private PaymentFacade paymentFacade;

    public PaymentService(DynamoDbClient dbClient) {
        this.paymentFacade = new PaymentFacadeImpl(dbClient);
    }

    public PaymentDTO processPayment(PaymentDTO paymentDTO) {
        logger.info("processPayment");
        var circuitBreaker = CircuitBreakUtil.newCircuitBreaker(Config.create().get("circuit-breaker"));
        return circuitBreaker.invoke(()->paymentFacade.processPayment(paymentDTO));
    }
}
