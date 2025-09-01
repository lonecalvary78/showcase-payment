package dev.lonecalvary78.app.payment.internal.processing.model.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("PaymentDTO Tests")
class PaymentDTOTest {

    @Test
    @DisplayName("Should create PaymentDTO with all fields")
    void shouldCreatePaymentDTOWithAllFields() {
        // Given
        var id = UUID.randomUUID();
        var debitAccount = "ACC123";
        var creditAccount = "ACC456";
        var amount = 100.0;
        var purpose = "Test payment";
        var reference = "REF123";
        var status = "COMPLETED";

        // When
        var paymentDTO = new PaymentDTO(id, debitAccount, creditAccount, amount, purpose, reference, status);

        // Then
        assertThat(paymentDTO.id()).isEqualTo(id);
        assertThat(paymentDTO.debittedAccountNo()).isEqualTo(debitAccount);
        assertThat(paymentDTO.creditedAccountNo()).isEqualTo(creditAccount);
        assertThat(paymentDTO.creditedAmount()).isEqualTo(amount);
        assertThat(paymentDTO.paymentPurpose()).isEqualTo(purpose);
        assertThat(paymentDTO.paymentReference()).isEqualTo(reference);
        assertThat(paymentDTO.status()).isEqualTo(status);
    }

    @Test
    @DisplayName("Should handle null values gracefully")
    void shouldHandleNullValuesGracefully() {
        // When
        var paymentDTO = new PaymentDTO(null, null, null, null, null, null, null);

        // Then
        assertThat(paymentDTO.id()).isNull();
        assertThat(paymentDTO.debittedAccountNo()).isNull();
        assertThat(paymentDTO.creditedAccountNo()).isNull();
        assertThat(paymentDTO.creditedAmount()).isNull();
        assertThat(paymentDTO.paymentPurpose()).isNull();
        assertThat(paymentDTO.paymentReference()).isNull();
        assertThat(paymentDTO.status()).isNull();
    }

    @Test
    @DisplayName("Should support record equality")
    void shouldSupportRecordEquality() {
        // Given
        var id = UUID.randomUUID();
        var payment1 = new PaymentDTO(id, "ACC123", "ACC456", 100.0, "Test", "REF123", "COMPLETED");
        var payment2 = new PaymentDTO(id, "ACC123", "ACC456", 100.0, "Test", "REF123", "COMPLETED");
        var payment3 = new PaymentDTO(UUID.randomUUID(), "ACC123", "ACC456", 100.0, "Test", "REF123", "COMPLETED");

        // When & Then
        assertThat(payment1).isEqualTo(payment2);
        assertThat(payment1).isNotEqualTo(payment3);
        assertThat(payment1.hashCode()).isEqualTo(payment2.hashCode());
    }

    @Test
    @DisplayName("Should have proper toString implementation")
    void shouldHaveProperToStringImplementation() {
        // Given
        var id = UUID.randomUUID();
        var paymentDTO = new PaymentDTO(id, "ACC123", "ACC456", 100.0, "Test", "REF123", "COMPLETED");

        // When
        var toStringResult = paymentDTO.toString();

        // Then
        assertThat(toStringResult).contains("ACC123", "ACC456", "100.0", "Test", "REF123", "COMPLETED");
        assertThat(toStringResult).contains("PaymentDTO");
        assertThat(toStringResult).contains(id.toString());
    }

    @Test
    @DisplayName("Should handle different payment statuses")
    void shouldHandleDifferentPaymentStatuses() {
        // Given
        var id = UUID.randomUUID();
        var completedPayment = new PaymentDTO(id, "ACC123", "ACC456", 100.0, "Test", "REF123", "COMPLETED");
        var pendingPayment = new PaymentDTO(id, "ACC123", "ACC456", 100.0, "Test", "REF123", "PENDING");
        var failedPayment = new PaymentDTO(id, "ACC123", "ACC456", 100.0, "Test", "REF123", "FAILED");

        // When & Then
        assertThat(completedPayment.status()).isEqualTo("COMPLETED");
        assertThat(pendingPayment.status()).isEqualTo("PENDING");
        assertThat(failedPayment.status()).isEqualTo("FAILED");
    }

    @Test
    @DisplayName("Should handle different amount types")
    void shouldHandleDifferentAmountTypes() {
        // Given
        var id = UUID.randomUUID();
        var smallAmount = new PaymentDTO(id, "ACC123", "ACC456", 0.01, "Small", "REF1", "COMPLETED");
        var largeAmount = new PaymentDTO(id, "ACC123", "ACC456", 999999.99, "Large", "REF2", "COMPLETED");
        var zeroAmount = new PaymentDTO(id, "ACC123", "ACC456", 0.0, "Zero", "REF3", "COMPLETED");

        // When & Then
        assertThat(smallAmount.creditedAmount()).isPositive();
        assertThat(largeAmount.creditedAmount()).isGreaterThan(1000);
        assertThat(zeroAmount.creditedAmount()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Should handle empty strings")
    void shouldHandleEmptyStrings() {
        // Given
        var id = UUID.randomUUID();

        // When
        var paymentDTO = new PaymentDTO(id, "", "", 100.0, "", "", "");

        // Then
        assertThat(paymentDTO.debittedAccountNo()).isEmpty();
        assertThat(paymentDTO.creditedAccountNo()).isEmpty();
        assertThat(paymentDTO.paymentPurpose()).isEmpty();
        assertThat(paymentDTO.paymentReference()).isEmpty();
        assertThat(paymentDTO.status()).isEmpty();
        assertThat(paymentDTO.creditedAmount()).isEqualTo(100.0);
        assertThat(paymentDTO.id()).isEqualTo(id);
    }

    @Test
    @DisplayName("Should validate UUID field")
    void shouldValidateUUIDField() {
        // Given
        var uuid1 = UUID.randomUUID();
        var uuid2 = UUID.randomUUID();
        var payment1 = new PaymentDTO(uuid1, "ACC123", "ACC456", 100.0, "Test", "REF123", "COMPLETED");
        var payment2 = new PaymentDTO(uuid2, "ACC123", "ACC456", 100.0, "Test", "REF123", "COMPLETED");

        // When & Then
        assertThat(payment1.id()).isNotEqualTo(payment2.id());
        assertThat(payment1.id()).isInstanceOf(UUID.class);
        assertThat(payment2.id()).isInstanceOf(UUID.class);
        assertThat(payment1).isNotEqualTo(payment2); // Different IDs make them different
    }
}
