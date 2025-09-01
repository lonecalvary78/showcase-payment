package dev.lonecalvary78.app.payment.internal.bankaccount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.lonecalvary78.app.payment.internal.bankaccount.handler.BankAccountResourceHandler;
import io.helidon.config.Config;
import io.helidon.logging.common.LogConfig;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.HttpRouting;


/**
 * Bank Account API application.
 */
public class BankAccountApplication {
    private static final Logger logger = LoggerFactory.getLogger(BankAccountApplication.class);
    
    public static void main(String[] args) {
        LogConfig.configureRuntime();
        var appConfig = Config.create();
        
        logger.info("Starting Bank Account API...");
        
        var appServer = WebServer.builder()
                                 .config(appConfig.get("server"))
                                 .routing(BankAccountApplication::routing)
                                 .build();
        appServer.start();
        
        logger.info("Bank Account API started successfully");
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down Bank Account API...");
            appServer.stop();
        }));
    }

    private static void routing(HttpRouting.Builder routingBuilder) {
        routingBuilder.register("/api/v1/bankaccounts", new BankAccountResourceHandler());
    }
}