package dev.lonecalvary78.app.payment.internal.processing.handler;

import dev.lonecalvary78.app.payment.internal.processing.api.response.ErrorResponse;
import dev.lonecalvary78.app.payment.internal.processing.exception.ApplicationException;
import dev.lonecalvary78.app.payment.internal.processing.exception.ErrorCode;
import dev.lonecalvary78.app.payment.internal.processing.model.dto.PaymentDTO;
import dev.lonecalvary78.app.payment.internal.processing.service.PaymentService;
import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.HttpService;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class PaymentResourceHandler implements HttpService {
    private PaymentService paymentService;

    public PaymentResourceHandler(DynamoDbClient dbClient) {
        this.paymentService = new PaymentService(dbClient);
    }
    
    @Override
    public void routing(HttpRules rules) {
        rules.get("/", this::index)
             .post("/",this::processPayment);
    }

    private void index(ServerRequest request, ServerResponse response) {
        response.send();
    }

    private void processPayment(ServerRequest request, ServerResponse response) {
        try {
            var paymentDTO = request.content().asOptional(PaymentDTO.class).orElseThrow(()-> new ApplicationException(ErrorCode.MISSING_PAYMENT_REQUEST));
            response.send(paymentService.processPayment(paymentDTO));
        } catch(Exception exception) {
            handleThrownException(exception, response);
        }
    }

    private void handleThrownException(Exception thrownException, ServerResponse response) {
        if(thrownException instanceof ApplicationException) {
            var errorCode = ((ApplicationException)thrownException).getErrorCode();
            if(errorCode != null) {
                response.status(errorCode.getResponseStatus()).send(ErrorResponse.of(errorCode.getCode(), errorCode.getMessage()));
            }
        } else {
            response.status(403).send(ErrorResponse.of("GENERIC",thrownException.getMessage()));
        }
    }
}
