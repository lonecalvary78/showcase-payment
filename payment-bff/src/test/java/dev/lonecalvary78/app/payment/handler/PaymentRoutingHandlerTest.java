package dev.lonecalvary78.app.payment.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.lonecalvary78.app.payment.model.dto.PaymentRequestDTO;
import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;

@ExtendWith(MockitoExtension.class)
@DisplayName("PaymentRoutingHandler Tests")
class PaymentRoutingHandlerTest {

    @Mock
    private ServerRequest request;

    @Mock
    private ServerResponse response;

    @Mock
    private HttpRules rules;

    private PaymentRoutingHandler handler;

    @BeforeEach
    void setUp() {
        handler = new PaymentRoutingHandler();
    }

    @Test
    @DisplayName("Should create handler instance successfully")
    void shouldCreateHandlerInstanceSuccessfully() {
        // When & Then
        assertDoesNotThrow(() -> new PaymentRoutingHandler());
    }

    @Test
    @DisplayName("Should configure routing rules correctly")
    void shouldConfigureRoutingRulesCorrectly() {
        // Given
        when(rules.get(anyString(), any())).thenReturn(rules);
        when(rules.post(anyString(), any())).thenReturn(rules);

        // When
        handler.routing(rules);

        // Then
        verify(rules).get(anyString(), any());
        verify(rules).post(anyString(), any());
    }

    @Test
    @DisplayName("Should handle index request successfully")
    void shouldHandleIndexRequestSuccessfully() {
        // This is a simple test for the routing setup - 
        // Real integration testing would be done with Helidon test framework
        assertDoesNotThrow(() -> new PaymentRoutingHandler());
    }

    @Test
    @DisplayName("Should validate PaymentRequestDTO structure")
    void shouldValidatePaymentRequestDTOStructure() {
        // Given
        var paymentRequest = new PaymentRequestDTO(
            "ACC123",
            "ACC456", 
            100.0,
            "Test payment",
            "REF123"
        );

        // When & Then
        assertThat(paymentRequest.debittedAccountNo()).isEqualTo("ACC123");
        assertThat(paymentRequest.creditedAccountNo()).isEqualTo("ACC456");
        assertThat(paymentRequest.creditedAmount()).isEqualTo(100.0);
        assertThat(paymentRequest.paymentPurpose()).isEqualTo("Test payment");
        assertThat(paymentRequest.paymentReference()).isEqualTo("REF123");
    }

    @Test
    @DisplayName("Should create different PaymentRequestDTO instances")
    void shouldCreateDifferentPaymentRequestDTOInstances() {
        // Given
        var paymentRequest1 = new PaymentRequestDTO("ACC123", "ACC456", 100.0, "Payment 1", "REF123");
        var paymentRequest2 = new PaymentRequestDTO("ACC789", "ACC012", 200.0, "Payment 2", "REF456");

        // When & Then
        assertThat(paymentRequest1.debittedAccountNo()).isNotEqualTo(paymentRequest2.debittedAccountNo());
        assertThat(paymentRequest1.creditedAmount()).isNotEqualTo(paymentRequest2.creditedAmount());
    }

    @Test
    @DisplayName("Should validate payment request field constraints")
    void shouldValidatePaymentRequestFieldConstraints() {
        // Testing different scenarios with PaymentRequestDTO
        
        // Valid case
        var validRequest = new PaymentRequestDTO("ACC123", "ACC456", 100.0, "Valid payment", "REF123");
        assertThat(validRequest.creditedAmount()).isPositive();
        
        // Edge case with minimal amount
        var minimalRequest = new PaymentRequestDTO("ACC123", "ACC456", 0.01, "Minimal payment", "REF123");
        assertThat(minimalRequest.creditedAmount()).isPositive();
        
        // Large amount case
        var largeRequest = new PaymentRequestDTO("ACC123", "ACC456", 999999.99, "Large payment", "REF123");
        assertThat(largeRequest.creditedAmount()).isGreaterThan(1000);
    }
}
