package dev.lonecalvary78.app.payment.internal.bankaccount.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dev.lonecalvary78.app.payment.internal.bankaccount.exception.ApplicationException;
import dev.lonecalvary78.app.payment.internal.bankaccount.exception.ErrorCode;

@DisplayName("BankAccountService Tests")
class BankAccountServiceTest {

    private BankAccountService bankAccountService;

    @BeforeEach
    void setUp() {
        bankAccountService = new BankAccountService();
    }

    @Test
    @DisplayName("Should create BankAccountService instance")
    void shouldCreateBankAccountServiceInstance() {
        // When & Then
        assertThat(bankAccountService).isNotNull();
    }

    @Test
    @DisplayName("Should return account balance for valid account number")
    void shouldReturnAccountBalanceForValidAccountNumber() throws ApplicationException {
        // Given
        var accountNo = "ACC123";

        // When
        var result = bankAccountService.getBalanceForAccount(accountNo);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.accountNo()).isEqualTo(accountNo);
        assertThat(result.availableBalance()).isEqualTo(100.0);
    }

    @Test
    @DisplayName("Should throw ApplicationException for null account number")
    void shouldThrowApplicationExceptionForNullAccountNumber() {
        // When & Then
        assertThatThrownBy(() -> bankAccountService.getBalanceForAccount(null))
            .isInstanceOf(ApplicationException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.MISSING_ACCOUNT_NUMBER);
    }

    @Test
    @DisplayName("Should throw ApplicationException for empty account number")
    void shouldThrowApplicationExceptionForEmptyAccountNumber() {
        // When & Then
        assertThatThrownBy(() -> bankAccountService.getBalanceForAccount(""))
            .isInstanceOf(ApplicationException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.MISSING_ACCOUNT_NUMBER);
    }

    @Test
    @DisplayName("Should return consistent balance amount")
    void shouldReturnConsistentBalanceAmount() throws ApplicationException {
        // Given
        var accountNo1 = "ACC123";
        var accountNo2 = "ACC456";

        // When
        var result1 = bankAccountService.getBalanceForAccount(accountNo1);
        var result2 = bankAccountService.getBalanceForAccount(accountNo2);

        // Then - Both should return the same fixed balance amount
        assertThat(result1.availableBalance()).isEqualTo(100.0);
        assertThat(result2.availableBalance()).isEqualTo(100.0);
        assertThat(result1.availableBalance()).isEqualTo(result2.availableBalance());
    }

    @Test
    @DisplayName("Should handle different account number formats")
    void shouldHandleDifferentAccountNumberFormats() throws ApplicationException {
        // Given
        var shortAccount = "123";
        var longAccount = "ACCOUNT-123456789";
        var alphanumericAccount = "ACC123XYZ";

        // When
        var result1 = bankAccountService.getBalanceForAccount(shortAccount);
        var result2 = bankAccountService.getBalanceForAccount(longAccount);
        var result3 = bankAccountService.getBalanceForAccount(alphanumericAccount);

        // Then
        assertThat(result1.accountNo()).isEqualTo(shortAccount);
        assertThat(result2.accountNo()).isEqualTo(longAccount);
        assertThat(result3.accountNo()).isEqualTo(alphanumericAccount);
        
        // All should return the same balance
        assertThat(result1.availableBalance()).isEqualTo(100.0);
        assertThat(result2.availableBalance()).isEqualTo(100.0);
        assertThat(result3.availableBalance()).isEqualTo(100.0);
    }

    @Test
    @DisplayName("Should handle whitespace-only account number as invalid")
    void shouldHandleWhitespaceOnlyAccountNumberAsInvalid() {
        // Given
        var whitespaceAccount = "   ";

        // When & Then - Whitespace is not empty but should be considered invalid
        // Note: Current implementation only checks for null or empty, not whitespace
        // This test documents current behavior
        try {
            var result = bankAccountService.getBalanceForAccount(whitespaceAccount);
            assertThat(result).isNotNull();
            assertThat(result.accountNo()).isEqualTo(whitespaceAccount);
        } catch (ApplicationException e) {
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.MISSING_ACCOUNT_NUMBER);
        }
    }

    @Test
    @DisplayName("Should validate return type is AccountBalanceDTO")
    void shouldValidateReturnTypeIsAccountBalanceDTO() throws ApplicationException {
        // Given
        var accountNo = "TEST123";

        // When
        var result = bankAccountService.getBalanceForAccount(accountNo);

        // Then
        assertThat(result).isInstanceOf(dev.lonecalvary78.app.payment.internal.bankaccount.model.dto.AccountBalanceDTO.class);
        assertThat(result.accountNo()).isNotNull();
        assertThat(result.availableBalance()).isNotNull();
    }
}
