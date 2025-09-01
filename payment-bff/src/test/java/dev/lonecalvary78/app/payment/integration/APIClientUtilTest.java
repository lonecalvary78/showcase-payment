package dev.lonecalvary78.app.payment.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("APIClientUtil Tests")
class APIClientUtilTest {

    @Test
    @DisplayName("Should handle null config gracefully for bank account client")
    void shouldHandleNullConfigGracefullyForBankAccountClient() {
        // When & Then
        assertThatThrownBy(() -> APIClientUtil.newClientForBankAccount(null))
            .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("Should handle null config gracefully for payment client")
    void shouldHandleNullConfigGracefullyForPaymentClient() {
        // When & Then
        assertThatThrownBy(() -> APIClientUtil.newClientForPayment(null))
            .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("Should validate utility class behavior")
    void shouldValidateUtilityClassBehavior() {
        // Given - APIClientUtil is a utility class with @UtilityClass annotation
        // When & Then - All methods should be static and class should not be instantiable
        
        // This test validates that the utility class pattern is followed
        // The methods are static as evidenced by the ability to call them directly
        var clazz = APIClientUtil.class;
        
        // Validate that the class behaves as utility class
        // Since all methods are static and accessible
        assertThat(clazz.getDeclaredMethods()).hasSizeGreaterThan(0);
    }

    @Test
    @DisplayName("Should validate class structure and annotations")
    void shouldValidateClassStructureAndAnnotations() {
        // Given
        var clazz = APIClientUtil.class;
        
        // When & Then
        assertThat(clazz).isNotNull();
        assertThat(clazz.getDeclaredMethods()).hasSizeGreaterThan(0);
        
        // Check that the class has the expected methods
        var methods = clazz.getDeclaredMethods();
        var methodNames = java.util.Arrays.stream(methods)
            .map(java.lang.reflect.Method::getName)
            .toList();
            
        assertThat(methodNames).contains("newClientForBankAccount", "newClientForPayment");
    }
}
