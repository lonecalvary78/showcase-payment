package dev.lonecalvary78.app.payment.internal.processing;

import dev.lonecalvary78.app.payment.internal.processing.handler.PaymentResourceHandler;
import dev.lonecalvary78.app.payment.internal.processing.infra.db.DatabaseManager;
import dev.lonecalvary78.app.payment.internal.processing.model.entity.Payment;
import io.helidon.config.Config;
import io.helidon.logging.common.LogConfig;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.HttpRouting;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.logging.Logger;

/**
 * Payment API application.
 */
public class PaymentApplication {
    private static final Logger LOGGER = Logger.getLogger(PaymentApplication.class.getName());
    
    public static void main(String[] args) {
       LogConfig.configureRuntime();
       var appConfig = Config.create();
       var dbClient = DatabaseManager.newClient(appConfig);
       
       LOGGER.info("Starting Payment API...");
       
       var appServer = WebServer.builder()
           .config(appConfig.get("server"))
           .routing(routingBuilder -> routing(routingBuilder, dbClient, appConfig))
           .build();
           
       appServer.start();
       DatabaseManager.initTable(dbClient, Payment.TABLE_NAME, Payment.class);
       
       LOGGER.info("Payment API started successfully");
       
       Runtime.getRuntime().addShutdownHook(new Thread(() -> {
           LOGGER.info("Shutting down Payment API...");
           appServer.stop();
       }));       
    }

    private static void routing(HttpRouting.Builder routingBuilder, DynamoDbClient dbClient, Config config) {
        // Register payment endpoints
        routingBuilder.register("/api/v1/payments", new PaymentResourceHandler(dbClient));
    }
}