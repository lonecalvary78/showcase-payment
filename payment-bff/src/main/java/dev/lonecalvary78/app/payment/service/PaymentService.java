package dev.lonecalvary78.app.payment.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.lonecalvary78.app.payment.integration.APIClientUtil;
import dev.lonecalvary78.app.payment.model.dto.AccountBalanceDTO;
import dev.lonecalvary78.app.payment.model.dto.PaymentRequestDTO;
import io.helidon.common.media.type.MediaTypes;
import io.helidon.config.Config;
import io.helidon.webclient.api.HttpClientResponse;

/**
 * Service for handling payment operations.
 */
public class PaymentService {
    private final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    
    public void submitNewPayment(PaymentRequestDTO paymentRequestDTO, String userSubject) throws Exception {
        // Validate input parameters
        validatePaymentRequest(paymentRequestDTO);
        
        logger.info("submitNewPayment for payment reference: " + paymentRequestDTO.paymentReference() + " by user: " + userSubject);
        checkAccountBalance(paymentRequestDTO.debittedAccountNo(), paymentRequestDTO.creditedAmount());
        processNewPayment(paymentRequestDTO);
    }
    
    private void validatePaymentRequest(PaymentRequestDTO paymentRequestDTO) throws Exception {
        if (paymentRequestDTO == null) {
            throw new IllegalArgumentException("Payment request cannot be null");
        }
        
        if (paymentRequestDTO.debittedAccountNo() == null || paymentRequestDTO.debittedAccountNo().trim().isEmpty()) {
            throw new IllegalArgumentException("Debited account number cannot be null or empty");
        }
        
        if (paymentRequestDTO.creditedAccountNo() == null || paymentRequestDTO.creditedAccountNo().trim().isEmpty()) {
            throw new IllegalArgumentException("Credited account number cannot be null or empty");
        }
        
        if (paymentRequestDTO.creditedAmount() == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        
        if (paymentRequestDTO.creditedAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }

    private void checkAccountBalance(String debitedAccountNo, Double debitedAmount) throws Exception {
        logger.info("checkAccountBalance for account: " + debitedAccountNo);
        var integrationConfig = Config.create().get("integration");
        var bankAccountAPIClient = APIClientUtil.newClientForBankAccount(integrationConfig);
        
        AccountBalanceDTO accountBalanceDTO = null;
        try {
            HttpClientResponse response = bankAccountAPIClient.get("/bankaccounts/balances")
                .queryParam("accountNo", debitedAccountNo)
                .accept(MediaTypes.APPLICATION_JSON)
                .request(); // Execute the request
                
            if (response.status().code() == 200) {
                accountBalanceDTO = response.as(AccountBalanceDTO.class);
                logger.info("Account balance retrieved successfully for account: " + debitedAccountNo);
            } else {
                throw new RuntimeException("Failed to check account balance: " + response.status());
            }
        } catch (Exception thrownException) {
            logger.error("checkAccountBalance[errorMessage: {}]",thrownException.getMessage(),thrownException);
            throw thrownException; // Re-throw the exception
        }

        if(accountBalanceDTO != null) {
            if(accountBalanceDTO.availableBalance()<=0 || 
               BigDecimal.valueOf(accountBalanceDTO.availableBalance())
                   .subtract(BigDecimal.valueOf(debitedAmount)).doubleValue()<0) {
                throw new Exception("Insufficient fund");
            }
        }
    }

    private void processNewPayment(PaymentRequestDTO paymentRequestDTO) {
        var integrationConfig = Config.create().get("integration");
        var paymentAPIClient = APIClientUtil.newClientForPayment(integrationConfig);
        
        try {
            HttpClientResponse response = paymentAPIClient.post("/payments")
                .contentType(MediaTypes.APPLICATION_JSON)
                .accept(MediaTypes.APPLICATION_JSON)
                .submit(paymentRequestDTO); 
                
            if (response.status().code() == 200 || response.status().code() == 201) {
                var paymentResult = response.as(String.class);
                logger.info("Payment processed successfully: " + paymentResult);
            } else {
                throw new RuntimeException("Failed to process payment: " + response.status());
            }
        } catch (Exception thrownException) {
            logger.error("processNewPayment[errorMessage: {}]",thrownException.getMessage(), thrownException);
        }
    }
}
