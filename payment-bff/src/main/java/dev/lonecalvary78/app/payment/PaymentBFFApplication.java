package dev.lonecalvary78.app.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.lonecalvary78.app.payment.handler.PaymentRoutingHandler;
import io.helidon.common.config.Config;
import io.helidon.logging.common.LogConfig;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.HttpRouting;

/**
 * Payment BFF Application for handling payment frontend requests.
 */
public class PaymentBFFApplication {
    
    private static final Logger logger = LoggerFactory.getLogger(PaymentBFFApplication.class);
    
    public static void main(String[] args) {
        LogConfig.configureRuntime();
        var appConfig = Config.create();
        
        logger.info("Starting Payment BFF...");
        
        var appServer = WebServer.builder()
            .config(appConfig.get("server"))
            .routing(routingBuilder -> routing(routingBuilder, appConfig))
            .build();
            
        appServer.start();
        
        logger.info("Payment BFF started successfully");
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down Payment BFF...");
            appServer.stop();
        }));
    }

    private static void routing(HttpRouting.Builder routingBuilder, Config config) {
        // Register handlers
        routingBuilder.register("/api/v1/payments", new PaymentRoutingHandler());
    }

}