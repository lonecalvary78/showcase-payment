package dev.lonecalvary78.app.payment.internal.processing.facade;

import java.util.logging.Logger;

import dev.lonecalvary78.app.payment.internal.processing.mapper.PaymentMapper;
import dev.lonecalvary78.app.payment.internal.processing.model.dto.PaymentDTO;
import dev.lonecalvary78.app.payment.internal.processing.repository.PaymentRepository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class PaymentFacadeImpl implements PaymentFacade {
    private final Logger logger = Logger.getLogger(PaymentFacadeImpl.class.getName());
    private PaymentRepository paymentRepository;

    public PaymentFacadeImpl(DynamoDbClient dbClient) {
        this.paymentRepository = new PaymentRepository(dbClient);
    }

    @Override
    public PaymentDTO processPayment(PaymentDTO paymentDTO) {
        logger.info("processPayment");
        paymentDTO = sendToBankHost(paymentDTO);
        createPaymentEntry(paymentDTO);
        return paymentDTO;
    }

    private PaymentDTO sendToBankHost(PaymentDTO paymentDTO) {
        logger.info("sendToBankHost");
        //Send payment to bank host for the further process
        return paymentDTO;
    }

    private void createPaymentEntry(PaymentDTO paymentDTO) {
        logger.info("createPaymentEntry");
        paymentRepository.save(PaymentMapper.INSTANCE.fromDTO(paymentDTO));
    }
}
