package dev.lonecalvary78.app.payment.internal.bankaccount.handler;

import dev.lonecalvary78.app.payment.internal.bankaccount.api.response.ErrorResponse;
import dev.lonecalvary78.app.payment.internal.bankaccount.exception.ApplicationException;
import dev.lonecalvary78.app.payment.internal.bankaccount.service.BankAccountService;
import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.HttpService;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;

public class BankAccountResourceHandler implements HttpService {
    private BankAccountService bankAccountService;

    public BankAccountResourceHandler() {
        this.bankAccountService = new BankAccountService();
    }

    @Override
    public void routing(HttpRules rules) {
        rules.get("/balances", this::getAccountBalance);
    }

    private void getAccountBalance(ServerRequest request, ServerResponse response) {
        try {
            var accountNo = request.query().first("accountNo").asOptional().orElse("");
            response.send(bankAccountService.getBalanceForAccount(accountNo));
        } catch(ApplicationException applicationException) {
            handleThrownException(applicationException, response);
        }
    }
    
    private void handleThrownException(ApplicationException applicationException, ServerResponse response) {
        var errorCode = applicationException.getErrorCode(); 
        response.status(errorCode.getResponseStatus()).send(ErrorResponse.of(errorCode.getCode(), errorCode.getMessage()));
    }
}
