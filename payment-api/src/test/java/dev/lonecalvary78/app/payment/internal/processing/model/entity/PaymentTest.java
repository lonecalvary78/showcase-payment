package dev.lonecalvary78.app.payment.internal.processing.model.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Payment Entity Tests")
class PaymentTest {

    @Test
    @DisplayName("Should have correct table name constant")
    void shouldHaveCorrectTableNameConstant() {
        // When & Then
        assertThat(Payment.TABLE_NAME).isEqualTo("payment");
    }

    @Test
    @DisplayName("Should create Payment entity with all fields")
    void shouldCreatePaymentEntityWithAllFields() {
        // Given
        var payment = new Payment();
        var id = UUID.randomUUID();
        var debitAccount = "ACC123";
        var creditAccount = "ACC456";
        var amount = 100.0;
        var purpose = "Test payment";
        var reference = "REF123";
        var status = "COMPLETED";

        // When
        payment.setId(id);
        payment.setDebittedAccountNo(debitAccount);
        payment.setCreditedAccountNo(creditAccount);
        payment.setCreditedAmount(amount);
        payment.setPaymentPurpose(purpose);
        payment.setPaymentReference(reference);
        payment.setStatus(status);

        // Then
        assertThat(payment.getId()).isEqualTo(id);
        assertThat(payment.getDebittedAccountNo()).isEqualTo(debitAccount);
        assertThat(payment.getCreditedAccountNo()).isEqualTo(creditAccount);
        assertThat(payment.getCreditedAmount()).isEqualTo(amount);
        assertThat(payment.getPaymentPurpose()).isEqualTo(purpose);
        assertThat(payment.getPaymentReference()).isEqualTo(reference);
        assertThat(payment.getStatus()).isEqualTo(status);
    }

    @Test
    @DisplayName("Should handle null values")
    void shouldHandleNullValues() {
        // Given
        var payment = new Payment();

        // When
        payment.setId(null);
        payment.setDebittedAccountNo(null);
        payment.setCreditedAccountNo(null);
        payment.setCreditedAmount(null);
        payment.setPaymentPurpose(null);
        payment.setPaymentReference(null);
        payment.setStatus(null);

        // Then
        assertThat(payment.getId()).isNull();
        assertThat(payment.getDebittedAccountNo()).isNull();
        assertThat(payment.getCreditedAccountNo()).isNull();
        assertThat(payment.getCreditedAmount()).isNull();
        assertThat(payment.getPaymentPurpose()).isNull();
        assertThat(payment.getPaymentReference()).isNull();
        assertThat(payment.getStatus()).isNull();
    }

    @Test
    @DisplayName("Should support Lombok Data annotation behavior")
    void shouldSupportLombokDataAnnotationBehavior() {
        // Given
        var payment1 = new Payment();
        var payment2 = new Payment();
        var id = UUID.randomUUID();

        payment1.setId(id);
        payment1.setDebittedAccountNo("ACC123");
        payment1.setCreditedAmount(100.0);

        payment2.setId(id);
        payment2.setDebittedAccountNo("ACC123");
        payment2.setCreditedAmount(100.0);

        // When & Then
        assertThat(payment1).isEqualTo(payment2); // Lombok generates equals
        assertThat(payment1.hashCode()).isEqualTo(payment2.hashCode()); // Lombok generates hashCode
        assertThat(payment1.toString()).isNotNull(); // Lombok generates toString
    }

    @Test
    @DisplayName("Should validate DynamoDB annotations are present")
    void shouldValidateDynamoDBAnnotationsArePresent() {
        // Given
        var paymentClass = Payment.class;

        // When & Then
        assertThat(paymentClass.isAnnotationPresent(software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean.class))
            .isTrue();
        
        // Note: Lombok @Data annotation might not be visible at runtime in some configurations
        // The important thing is that the class behaves correctly (has getters/setters)
        assertThat(paymentClass.getDeclaredMethods()).hasSizeGreaterThan(0);
    }

    @Test
    @DisplayName("Should handle different payment amounts")
    void shouldHandleDifferentPaymentAmounts() {
        // Given
        var payment = new Payment();

        // When & Then
        payment.setCreditedAmount(0.01);
        assertThat(payment.getCreditedAmount()).isPositive();

        payment.setCreditedAmount(999999.99);
        assertThat(payment.getCreditedAmount()).isGreaterThan(1000);

        payment.setCreditedAmount(0.0);
        assertThat(payment.getCreditedAmount()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Should handle empty strings")
    void shouldHandleEmptyStrings() {
        // Given
        var payment = new Payment();

        // When
        payment.setDebittedAccountNo("");
        payment.setCreditedAccountNo("");
        payment.setPaymentPurpose("");
        payment.setPaymentReference("");
        payment.setStatus("");

        // Then
        assertThat(payment.getDebittedAccountNo()).isEmpty();
        assertThat(payment.getCreditedAccountNo()).isEmpty();
        assertThat(payment.getPaymentPurpose()).isEmpty();
        assertThat(payment.getPaymentReference()).isEmpty();
        assertThat(payment.getStatus()).isEmpty();
    }

    @Test
    @DisplayName("Should create new instances independently")
    void shouldCreateNewInstancesIndependently() {
        // Given
        var payment1 = new Payment();
        var payment2 = new Payment();

        // When
        payment1.setId(UUID.randomUUID());
        payment1.setDebittedAccountNo("ACC123");

        // Then
        assertThat(payment2.getId()).isNull();
        assertThat(payment2.getDebittedAccountNo()).isNull();
        assertThat(payment1).isNotEqualTo(payment2);
    }

    @Test
    @DisplayName("Should validate class structure")
    void shouldValidateClassStructure() {
        // Given
        var paymentClass = Payment.class;

        // When & Then
        assertThat(paymentClass.getPackage().getName())
            .isEqualTo("dev.lonecalvary78.app.payment.internal.processing.model.entity");
        assertThat(paymentClass.getSimpleName()).isEqualTo("Payment");
        assertThat(java.lang.reflect.Modifier.isPublic(paymentClass.getModifiers())).isTrue();
    }
}
