package dev.lonecalvary78.app.payment.integration;

import io.helidon.common.config.Config;
import io.helidon.webclient.api.WebClient;

/**
 * Utility class for creating API clients.
 */
public class APIClientUtil {
    
    private APIClientUtil() {
        // Utility class
    }
    
    public static WebClient newClientForBankAccount(Config apiClientConfig) {
        var bankAccountEndpointUri = apiClientConfig.get("bankaccount.endpoint").asString().get();
        return WebClient.builder()
           .baseUri(bankAccountEndpointUri)
           .build();
    }

    public static WebClient newClientForPayment(Config apiClientConfig) {
        var paymentEndpointUri = apiClientConfig.get("payment.endpoint").asString().get();
        return WebClient.builder()
           .baseUri(paymentEndpointUri)
           .build();
    }
}
