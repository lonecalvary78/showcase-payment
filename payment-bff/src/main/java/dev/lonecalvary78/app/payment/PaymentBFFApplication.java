package dev.lonecalvary78.app.payment;

import dev.lonecalvary78.app.payment.handler.PaymentRoutingHandler;
import io.helidon.common.config.Config;
import io.helidon.logging.common.LogConfig;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.HttpRouting;

import java.util.logging.Logger;

/**
 * Payment BFF Application for handling payment frontend requests.
 */
public class PaymentBFFApplication {
    
    private static final Logger LOGGER = Logger.getLogger(PaymentBFFApplication.class.getName());
    
    public static void main(String[] args) {
        LogConfig.configureRuntime();
        var appConfig = Config.create();
        
        LOGGER.info("Starting Payment BFF...");
        
        var appServer = WebServer.builder()
            .config(appConfig.get("server"))
            .routing(routingBuilder -> routing(routingBuilder, appConfig))
            .build();
            
        appServer.start();
        
        LOGGER.info("Payment BFF started successfully");
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Shutting down Payment BFF...");
            appServer.stop();
        }));
    }

    private static void routing(HttpRouting.Builder routingBuilder, Config config) {
        // Register handlers
        routingBuilder.register("/api/v1/payments", new PaymentRoutingHandler());
    }

}