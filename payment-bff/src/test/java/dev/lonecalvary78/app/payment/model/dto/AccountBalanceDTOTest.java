package dev.lonecalvary78.app.payment.model.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("AccountBalanceDTO Tests")  
class AccountBalanceDTOTest {

    @Test
    @DisplayName("Should create AccountBalanceDTO with all fields")
    void shouldCreateAccountBalanceDTOWithAllFields() {
        // Given
        var accountNo = "ACC123";
        var balance = 1500.50;

        // When
        var accountBalance = new AccountBalanceDTO(accountNo, balance);

        // Then
        assertThat(accountBalance.accountNo()).isEqualTo(accountNo);
        assertThat(accountBalance.availableBalance()).isEqualTo(balance);
    }

    @Test
    @DisplayName("Should handle zero balance")
    void shouldHandleZeroBalance() {
        // When
        var accountBalance = new AccountBalanceDTO("ACC123", 0.0);

        // Then
        assertThat(accountBalance.availableBalance()).isEqualTo(0.0);
        assertThat(accountBalance.accountNo()).isEqualTo("ACC123");
    }

    @Test
    @DisplayName("Should handle negative balance")
    void shouldHandleNegativeBalance() {
        // When
        var accountBalance = new AccountBalanceDTO("ACC123", -100.0);

        // Then
        assertThat(accountBalance.availableBalance()).isEqualTo(-100.0);
        assertThat(accountBalance.availableBalance()).isNegative();
    }

    @Test
    @DisplayName("Should support record equality")
    void shouldSupportRecordEquality() {
        // Given
        var balance1 = new AccountBalanceDTO("ACC123", 1000.0);
        var balance2 = new AccountBalanceDTO("ACC123", 1000.0);
        var balance3 = new AccountBalanceDTO("ACC456", 1000.0);

        // When & Then
        assertThat(balance1).isEqualTo(balance2);
        assertThat(balance1).isNotEqualTo(balance3);
        assertThat(balance1.hashCode()).isEqualTo(balance2.hashCode());
    }

    @Test
    @DisplayName("Should have proper toString implementation")
    void shouldHaveProperToStringImplementation() {
        // Given
        var accountBalance = new AccountBalanceDTO("ACC123", 1500.50);

        // When
        var toStringResult = accountBalance.toString();

        // Then
        assertThat(toStringResult).contains("ACC123", "1500.5");
        assertThat(toStringResult).contains("AccountBalanceDTO");
    }

    @Test
    @DisplayName("Should handle null values")
    void shouldHandleNullValues() {
        // When
        var accountBalance = new AccountBalanceDTO(null, null);

        // Then
        assertThat(accountBalance.accountNo()).isNull();
        assertThat(accountBalance.availableBalance()).isNull();
    }

    @Test
    @DisplayName("Should handle very large balances")
    void shouldHandleVeryLargeBalances() {
        // Given
        var largeBalance = 999999999.99;

        // When
        var accountBalance = new AccountBalanceDTO("ACC123", largeBalance);

        // Then
        assertThat(accountBalance.availableBalance()).isEqualTo(largeBalance);
        assertThat(accountBalance.availableBalance()).isGreaterThan(1000000);
    }

    @Test
    @DisplayName("Should handle empty account number")
    void shouldHandleEmptyAccountNumber() {
        // When
        var accountBalance = new AccountBalanceDTO("", 1000.0);

        // Then
        assertThat(accountBalance.accountNo()).isEmpty();
        assertThat(accountBalance.availableBalance()).isEqualTo(1000.0);
    }
}
