package dev.lonecalvary78.app.payment.internal.processing.model.entity;

import java.util.UUID;

import lombok.Data;
import lombok.Getter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@DynamoDbBean
public class Payment {
    public static final String TABLE_NAME="payment";

    @Getter(onMethod = @__(@DynamoDbPartitionKey))
    private UUID id;

    @Getter(onMethod = @__(@DynamoDbAttribute("debitted_account_no")))
    private String debittedAccountNo;

    @Getter(onMethod = @__(@DynamoDbAttribute("credited_account_no")))
    private String creditedAccountNo;

    @Getter(onMethod = @__(@DynamoDbAttribute("amount")))
    private Double creditedAmount;

    @Getter(onMethod = @__(@DynamoDbAttribute("payment_purpose")))
    private String paymentPurpose;

    @Getter(onMethod = @__(@DynamoDbAttribute("payment_reference")))    
    private String paymentReference;
    private String status;
}
