package dev.lonecalvary78.app.payment.internal.bankaccount.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ApplicationException Tests")
class ApplicationExceptionTest {

    @Test
    @DisplayName("Should create ApplicationException with ErrorCode")
    void shouldCreateApplicationExceptionWithErrorCode() {
        // Given
        var errorCode = ErrorCode.MISSING_ACCOUNT_NUMBER;

        // When
        var exception = new ApplicationException(errorCode);

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getErrorCode()).isEqualTo(errorCode);
        assertThat(exception).isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("Should create ApplicationException with ACCOUNT_NUMBER_NOT_FOUND error")
    void shouldCreateApplicationExceptionWithAccountNumberNotFoundError() {
        // Given
        var errorCode = ErrorCode.ACCOUNT_NUMBER_NOT_FOUND;

        // When
        var exception = new ApplicationException(errorCode);

        // Then
        assertThat(exception.getErrorCode()).isEqualTo(errorCode);
        assertThat(exception.getErrorCode().getCode()).isEqualTo("BANKACCOUNT-ERR-002");
        assertThat(exception.getErrorCode().getMessage()).isEqualTo("Bank Account Number is not found");
        assertThat(exception.getErrorCode().getResponseStatus()).isEqualTo(403);
    }

    @Test
    @DisplayName("Should extend Exception class")
    void shouldExtendExceptionClass() {
        // Given
        var exception = new ApplicationException(ErrorCode.MISSING_ACCOUNT_NUMBER);

        // When & Then
        assertThat(exception).isInstanceOf(Exception.class);
        assertThat(exception).isInstanceOf(Throwable.class);
    }

    @Test
    @DisplayName("Should have null message by default")
    void shouldHaveNullMessageByDefault() {
        // Given
        var exception = new ApplicationException(ErrorCode.MISSING_ACCOUNT_NUMBER);

        // When & Then
        assertThat(exception.getMessage()).isNull();
    }

    @Test
    @DisplayName("Should preserve error code immutability")
    void shouldPreserveErrorCodeImmutability() {
        // Given
        var originalErrorCode = ErrorCode.MISSING_ACCOUNT_NUMBER;
        var exception = new ApplicationException(originalErrorCode);

        // When
        var retrievedErrorCode = exception.getErrorCode();

        // Then
        assertThat(retrievedErrorCode).isSameAs(originalErrorCode);
        assertThat(retrievedErrorCode.getCode()).isEqualTo(originalErrorCode.getCode());
    }

    @Test
    @DisplayName("Should support different error codes")
    void shouldSupportDifferentErrorCodes() {
        // Given
        var exception1 = new ApplicationException(ErrorCode.MISSING_ACCOUNT_NUMBER);
        var exception2 = new ApplicationException(ErrorCode.ACCOUNT_NUMBER_NOT_FOUND);

        // When & Then
        assertThat(exception1.getErrorCode()).isNotEqualTo(exception2.getErrorCode());
        assertThat(exception1.getErrorCode().getCode()).isNotEqualTo(exception2.getErrorCode().getCode());
    }

    @Test
    @DisplayName("Should be throwable as exception")
    void shouldBeThrowableAsException() {
        // Given
        var errorCode = ErrorCode.MISSING_ACCOUNT_NUMBER;

        // When & Then
        try {
            throw new ApplicationException(errorCode);
        } catch (ApplicationException e) {
            assertThat(e.getErrorCode()).isEqualTo(errorCode);
        }
    }

    @Test
    @DisplayName("Should have proper class structure")
    void shouldHaveProperClassStructure() {
        // Given
        var exceptionClass = ApplicationException.class;

        // When & Then
        assertThat(exceptionClass.getSuperclass()).isEqualTo(Exception.class);
        assertThat(exceptionClass.getPackage().getName())
            .isEqualTo("dev.lonecalvary78.app.payment.internal.bankaccount.exception");
    }
}
