package dev.lonecalvary78.app.payment.model.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("PaymentRequestDTO Tests")
class PaymentRequestDTOTest {

    @Test
    @DisplayName("Should create PaymentRequestDTO with all fields")
    void shouldCreatePaymentRequestDTOWithAllFields() {
        // Given
        var debitAccount = "ACC123";
        var creditAccount = "ACC456";
        var amount = 100.0;
        var purpose = "Test payment";
        var reference = "REF123";

        // When
        var paymentRequest = new PaymentRequestDTO(debitAccount, creditAccount, amount, purpose, reference);

        // Then
        assertThat(paymentRequest.debittedAccountNo()).isEqualTo(debitAccount);
        assertThat(paymentRequest.creditedAccountNo()).isEqualTo(creditAccount);
        assertThat(paymentRequest.creditedAmount()).isEqualTo(amount);
        assertThat(paymentRequest.paymentPurpose()).isEqualTo(purpose);
        assertThat(paymentRequest.paymentReference()).isEqualTo(reference);
    }

    @Test
    @DisplayName("Should handle null values gracefully")
    void shouldHandleNullValuesGracefully() {
        // When
        var paymentRequest = new PaymentRequestDTO(null, null, null, null, null);

        // Then
        assertThat(paymentRequest.debittedAccountNo()).isNull();
        assertThat(paymentRequest.creditedAccountNo()).isNull();
        assertThat(paymentRequest.creditedAmount()).isNull();
        assertThat(paymentRequest.paymentPurpose()).isNull();
        assertThat(paymentRequest.paymentReference()).isNull();
    }

    @Test
    @DisplayName("Should support record equality")
    void shouldSupportRecordEquality() {
        // Given
        var paymentRequest1 = new PaymentRequestDTO("ACC123", "ACC456", 100.0, "Test", "REF123");
        var paymentRequest2 = new PaymentRequestDTO("ACC123", "ACC456", 100.0, "Test", "REF123");
        var paymentRequest3 = new PaymentRequestDTO("ACC789", "ACC456", 100.0, "Test", "REF123");

        // When & Then
        assertThat(paymentRequest1).isEqualTo(paymentRequest2);
        assertThat(paymentRequest1).isNotEqualTo(paymentRequest3);
        assertThat(paymentRequest1.hashCode()).isEqualTo(paymentRequest2.hashCode());
    }

    @Test
    @DisplayName("Should have proper toString implementation")
    void shouldHaveProperToStringImplementation() {
        // Given
        var paymentRequest = new PaymentRequestDTO("ACC123", "ACC456", 100.0, "Test", "REF123");

        // When
        var toStringResult = paymentRequest.toString();

        // Then
        assertThat(toStringResult).contains("ACC123", "ACC456", "100.0", "Test", "REF123");
        assertThat(toStringResult).contains("PaymentRequestDTO");
    }

    @Test
    @DisplayName("Should handle different amount types")
    void shouldHandleDifferentAmountTypes() {
        // Given & When
        var smallAmount = new PaymentRequestDTO("ACC123", "ACC456", 0.01, "Small", "REF1");
        var largeAmount = new PaymentRequestDTO("ACC123", "ACC456", 999999.99, "Large", "REF2");
        var zeroAmount = new PaymentRequestDTO("ACC123", "ACC456", 0.0, "Zero", "REF3");

        // Then
        assertThat(smallAmount.creditedAmount()).isPositive();
        assertThat(largeAmount.creditedAmount()).isGreaterThan(1000);
        assertThat(zeroAmount.creditedAmount()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Should handle empty strings")
    void shouldHandleEmptyStrings() {
        // When
        var paymentRequest = new PaymentRequestDTO("", "", 100.0, "", "");

        // Then
        assertThat(paymentRequest.debittedAccountNo()).isEmpty();
        assertThat(paymentRequest.creditedAccountNo()).isEmpty();
        assertThat(paymentRequest.paymentPurpose()).isEmpty();
        assertThat(paymentRequest.paymentReference()).isEmpty();
        assertThat(paymentRequest.creditedAmount()).isEqualTo(100.0);
    }
}
