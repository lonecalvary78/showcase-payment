package dev.lonecalvary78.app.payment.internal.bankaccount.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ErrorCode Tests")
class ErrorCodeTest {

    @Test
    @DisplayName("Should have correct MISSING_ACCOUNT_NUMBER error details")
    void shouldHaveCorrectMissingAccountNumberErrorDetails() {
        // Given
        var errorCode = ErrorCode.MISSING_ACCOUNT_NUMBER;

        // When & Then
        assertThat(errorCode.getCode()).isEqualTo("BANKACCOUNT-ERR-001");
        assertThat(errorCode.getMessage()).isEqualTo("Missing Account Number");
        assertThat(errorCode.getResponseStatus()).isEqualTo(403);
    }

    @Test
    @DisplayName("Should have correct ACCOUNT_NUMBER_NOT_FOUND error details")
    void shouldHaveCorrectAccountNumberNotFoundErrorDetails() {
        // Given
        var errorCode = ErrorCode.ACCOUNT_NUMBER_NOT_FOUND;

        // When & Then
        assertThat(errorCode.getCode()).isEqualTo("BANKACCOUNT-ERR-002");
        assertThat(errorCode.getMessage()).isEqualTo("Bank Account Number is not found");
        assertThat(errorCode.getResponseStatus()).isEqualTo(403);
    }

    @Test
    @DisplayName("Should have all required error codes defined")
    void shouldHaveAllRequiredErrorCodesDefined() {
        // Given
        var errorCodes = ErrorCode.values();

        // When & Then
        assertThat(errorCodes).hasSize(2);
        assertThat(errorCodes).contains(
            ErrorCode.MISSING_ACCOUNT_NUMBER,
            ErrorCode.ACCOUNT_NUMBER_NOT_FOUND
        );
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
    @DisplayName("Should have unique error codes")
    void shouldHaveUniqueErrorCodes() {
        // When & Then
        assertThat(ErrorCode.MISSING_ACCOUNT_NUMBER.getCode())
            .isNotEqualTo(ErrorCode.ACCOUNT_NUMBER_NOT_FOUND.getCode());
    }

    @Test
    @DisplayName("Should follow naming convention for error codes")
    void shouldFollowNamingConventionForErrorCodes() {
        // Given & When
        for (ErrorCode errorCode : ErrorCode.values()) {
            // Then - Should follow BANKACCOUNT-ERR-XXX pattern
            assertThat(errorCode.getCode()).startsWith("BANKACCOUNT-ERR-");
            assertThat(errorCode.getCode()).hasSizeGreaterThanOrEqualTo(18); // BANKACCOUNT-ERR-XXX format
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
        var error1 = ErrorCode.MISSING_ACCOUNT_NUMBER;
        var error2 = ErrorCode.MISSING_ACCOUNT_NUMBER;
        var error3 = ErrorCode.ACCOUNT_NUMBER_NOT_FOUND;

        // When & Then
        assertThat(error1).isEqualTo(error2);
        assertThat(error1).isNotEqualTo(error3);
        assertThat(error1.ordinal()).isNotEqualTo(error3.ordinal());
    }
}
