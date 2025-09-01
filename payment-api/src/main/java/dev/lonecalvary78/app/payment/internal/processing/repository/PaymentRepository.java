package dev.lonecalvary78.app.payment.internal.processing.repository;

import java.util.UUID;

import dev.lonecalvary78.app.payment.internal.processing.model.entity.Payment;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class PaymentRepository {
    private DynamoDbTable<Payment> paymentTable;
    
    public PaymentRepository(DynamoDbClient dbClient) {
        paymentTable = DynamoDbEnhancedClient.builder().dynamoDbClient(dbClient).build().table(Payment.TABLE_NAME, TableSchema.fromClass(Payment.class));
    }

    public void save(Payment payment) {
        if(payment == null) {
            throw new IllegalArgumentException("payment is required for save operation");
        }
        if(payment.getId() == null)
            payment.setId(UUID.randomUUID());
        paymentTable.putItem(payment);
    }
}
