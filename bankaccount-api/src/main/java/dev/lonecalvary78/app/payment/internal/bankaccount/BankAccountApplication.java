package dev.lonecalvary78.app.payment.internal.bankaccount;

import dev.lonecalvary78.app.payment.internal.bankaccount.handler.BankAccountResourceHandler;
import io.helidon.config.Config;
import io.helidon.logging.common.LogConfig;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.HttpRouting;

import java.util.logging.Logger;

/**
 * Bank Account API application.
 */
public class BankAccountApplication {
    private static final Logger LOGGER = Logger.getLogger(BankAccountApplication.class.getName());
    
    public static void main(String[] args) {
        LogConfig.configureRuntime();
        var appConfig = Config.create();
        
        LOGGER.info("Starting Bank Account API...");
        
        var appServer = WebServer.builder()
                                 .config(appConfig.get("server"))
                                 .routing(routingBuilder -> routing(routingBuilder, appConfig))
                                 .build();
        appServer.start();
        
        LOGGER.info("Bank Account API started successfully");
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Shutting down Bank Account API...");
            appServer.stop();
        }));
    }

    private static void routing(HttpRouting.Builder routingBuilder, Config config) {
        // Register bank account endpoints
        routingBuilder.register("/api/v1/bankaccounts", new BankAccountResourceHandler());
    }
}