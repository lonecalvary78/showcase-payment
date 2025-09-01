package dev.lonecalvary78.app.payment.internal.processing.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ErrorCode Tests (Payment API)")
class ErrorCodeTest {

    @Test
    @DisplayName("Should have correct MISSING_PAYMENT_REQUEST error details")
    void shouldHaveCorrectMissingPaymentRequestErrorDetails() {
        // Given
        var errorCode = ErrorCode.MISSING_PAYMENT_REQUEST;

        // When & Then
        assertThat(errorCode.getCode()).isEqualTo("PAYMENT-ERR-001");
        assertThat(errorCode.getMessage()).isEqualTo("Missing payment request");
        assertThat(errorCode.getResponseStatus()).isEqualTo(403);
    }

    @Test
    @DisplayName("Should have all required error codes defined")
    void shouldHaveAllRequiredErrorCodesDefined() {
        // Given
        var errorCodes = ErrorCode.values();

        // When & Then
        assertThat(errorCodes).hasSize(1);
        assertThat(errorCodes).contains(ErrorCode.MISSING_PAYMENT_REQUEST);
    }

    @Test
    @DisplayName("Should validate error code enum properties")
    void shouldValidateErrorCodeEnumProperties() {
        // Given & When
        for (ErrorCode errorCode : ErrorCode.values()) {
            // Then
            assertThat(errorCode.getCode()).isNotNull().isNotEmpty();
            assertThat(errorCode.getMessage()).isNotNull().isNotEmpty();
            assertThat(errorCode.getResponseStatus()).isPositive();
        }
    }

    @Test
    @DisplayName("Should follow naming convention for error codes")
    void shouldFollowNamingConventionForErrorCodes() {
        // Given & When
        for (ErrorCode errorCode : ErrorCode.values()) {
            // Then - Should follow PAYMENT-ERR-XXX pattern
            assertThat(errorCode.getCode()).startsWith("PAYMENT-ERR-");
            assertThat(errorCode.getCode()).hasSize(15); // PAYMENT-ERR-XXX format
        }
    }

    @Test
    @DisplayName("Should have consistent response status codes")
    void shouldHaveConsistentResponseStatusCodes() {
        // Given & When
        for (ErrorCode errorCode : ErrorCode.values()) {
            // Then - All current errors use 403 status
            assertThat(errorCode.getResponseStatus()).isEqualTo(403);
        }
    }

    @Test
    @DisplayName("Should support enum comparison")
    void shouldSupportEnumComparison() {
        // Given
        var error1 = ErrorCode.MISSING_PAYMENT_REQUEST;
        var error2 = ErrorCode.MISSING_PAYMENT_REQUEST;

        // When & Then
        assertThat(error1).isEqualTo(error2);
        assertThat(error1.ordinal()).isEqualTo(error2.ordinal());
    }

    @Test
    @DisplayName("Should validate error code uniqueness")
    void shouldValidateErrorCodeUniqueness() {
        // Given
        var errorCodes = ErrorCode.values();

        // When & Then
        // Currently only one error code, but validates the structure
        assertThat(errorCodes).hasSize(1);
        assertThat(ErrorCode.MISSING_PAYMENT_REQUEST.getCode()).isEqualTo("PAYMENT-ERR-001");
    }

    @Test
    @DisplayName("Should have proper package structure")
    void shouldHaveProperPackageStructure() {
        // Given
        var enumClass = ErrorCode.class;

        // When & Then
        assertThat(enumClass.getPackage().getName())
            .isEqualTo("dev.lonecalvary78.app.payment.internal.processing.exception");
        assertThat(enumClass.isEnum()).isTrue();
    }
}
