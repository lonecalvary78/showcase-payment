package dev.lonecalvary78.app.payment.internal.bankaccount.service;

import dev.lonecalvary78.app.payment.internal.bankaccount.exception.ApplicationException;
import dev.lonecalvary78.app.payment.internal.bankaccount.exception.ErrorCode;
import dev.lonecalvary78.app.payment.internal.bankaccount.model.dto.AccountBalanceDTO;

public class BankAccountService {
    public BankAccountService() {}

    public AccountBalanceDTO getBalanceForAccount(String accounttNo) throws ApplicationException {
        if(accounttNo == null || accounttNo.isEmpty()) {
            throw new ApplicationException(ErrorCode.MISSING_ACCOUNT_NUMBER);  
        }
        return AccountBalanceDTO.of(accounttNo, Double.valueOf(100));
    }
}
