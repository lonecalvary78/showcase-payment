package dev.lonecalvary78.app.payment.internal.processing.infra.db;

import java.net.URI;

import dev.lonecalvary78.app.payment.internal.processing.model.entity.Payment;
import io.helidon.config.Config;
import lombok.experimental.UtilityClass;
import software.amazon.awssdk.auth.credentials.AnonymousCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@UtilityClass
public class DatabaseManager {
    private static final String LOCALSTACK_MODE="localstack";

    public DynamoDbClient newClient(Config databaseConfig) {
        var databaseMode = databaseConfig.get("db.mode").asString().get();
        if(databaseMode.equals(LOCALSTACK_MODE)) {
            var endpointUrl = databaseConfig.get("db.localstack.endpoint").asString().get();
            var region = databaseConfig.get("db.localstack.region").asString().get();
            return DynamoDbClient.builder()
            .endpointOverride(URI.create(endpointUrl))
            .region(Region.of(region))
            .credentialsProvider(AnonymousCredentialsProvider.create())
            .build();
        } else {
            return DynamoDbClient.create();
        }
    }

    public void initTable(DynamoDbClient dbClient, String tableName, Class<Payment> paymentClass) {
        if(!isTableExist(dbClient, tableName)) {
            var dbTable = DynamoDbEnhancedClient.builder().dynamoDbClient(dbClient).build().table(tableName, TableSchema.fromClass(paymentClass));
            dbTable.createTable();
        }
    }

    private boolean isTableExist(DynamoDbClient dbClient, String targetTableName) {
        return dbClient.listTables().tableNames().stream().anyMatch(tableName->tableName.equals(targetTableName));
    }
}
