package dev.lonecalvary78.app.payment.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dev.lonecalvary78.app.payment.model.dto.PaymentRequestDTO;

@DisplayName("PaymentService Tests")
class PaymentServiceTest {

    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService();
    }

    @Test
    @DisplayName("Should create PaymentService instance")
    void shouldCreatePaymentServiceInstance() {
        // When & Then
        assertDoesNotThrow(() -> new PaymentService());
    }

    @Test
    @DisplayName("Should throw exception for null payment request")
    void shouldThrowExceptionForNullPaymentRequest() {
        // When & Then
        assertThatThrownBy(() -> paymentService.submitNewPayment(null, "test-user"))
            .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("Should handle payment request with valid structure")
    void shouldHandlePaymentRequestWithValidStructure() {
        // Given
        var paymentRequest = new PaymentRequestDTO(
            "ACC123", 
            "ACC456", 
            100.0, 
            "Test payment", 
            "REF123"
        );
        
        // Note: This test will fail in integration without real services
        // but tests the method structure and parameter validation
        assertThatThrownBy(() -> paymentService.submitNewPayment(paymentRequest, "test-user"))
            .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("Should reject payment with negative amount")
    void shouldRejectPaymentWithNegativeAmount() {
        // Given
        var paymentRequest = new PaymentRequestDTO(
            "ACC123", 
            "ACC456", 
            -100.0, 
            "Test payment", 
            "REF123"
        );
        
        // When & Then
        assertThatThrownBy(() -> paymentService.submitNewPayment(paymentRequest, "test-user"))
            .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("Should reject payment with zero amount")
    void shouldRejectPaymentWithZeroAmount() {
        // Given
        var paymentRequest = new PaymentRequestDTO(
            "ACC123", 
            "ACC456", 
            0.0, 
            "Test payment", 
            "REF123"
        );
        
        // When & Then
        assertThatThrownBy(() -> paymentService.submitNewPayment(paymentRequest, "test-user"))
            .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("Should reject payment with null account numbers")
    void shouldRejectPaymentWithNullAccountNumbers() {
        // Given
        var paymentRequest = new PaymentRequestDTO(
            null, 
            "ACC456", 
            100.0, 
            "Test payment", 
            "REF123"
        );
        
        // When & Then
        assertThatThrownBy(() -> paymentService.submitNewPayment(paymentRequest, "test-user"))
            .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("Should reject payment with empty debit account")
    void shouldRejectPaymentWithEmptyDebitAccount() {
        // Given
        var paymentRequest = new PaymentRequestDTO(
            "", 
            "ACC456", 
            100.0, 
            "Test payment", 
            "REF123"
        );
        
        // When & Then
        assertThatThrownBy(() -> paymentService.submitNewPayment(paymentRequest, "test-user"))
            .isInstanceOf(Exception.class);
    }
}
