package dev.lonecalvary78.app.payment.handler;

import dev.lonecalvary78.app.payment.model.dto.PaymentRequestDTO;
import dev.lonecalvary78.app.payment.service.PaymentService;
import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.HttpService;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;

public class PaymentRoutingHandler implements HttpService {
    private PaymentService paymentService;

    public PaymentRoutingHandler() {
        this.paymentService = new PaymentService();
    }

    @Override
    public void routing(HttpRules rules) {
        rules.get("/", this::index)
        .post("/",this::submitNewPayment);
    }

    private void index(ServerRequest request, ServerResponse response) {
        response.send("ok");
    }

    private void submitNewPayment(ServerRequest request, ServerResponse response) {
        try {
            var paymentRequestDTO = request.content().asOptional(PaymentRequestDTO.class).get();
            String userSubject = "demo-user-" + System.currentTimeMillis();
            paymentService.submitNewPayment(paymentRequestDTO, userSubject);
            response.status(201).send();
        } catch(Exception throwException) {
            handleThrownException(throwException, response);
        }
    }

    private void handleThrownException(Exception thrownException, ServerResponse response) {
        if (thrownException.getMessage() != null && thrownException.getMessage().contains("Insufficient fund")) {
            response.status(400).send("{\"error\":\"Insufficient funds\",\"message\":\"" + thrownException.getMessage() + "\"}");
        } else if (thrownException.getMessage() != null && thrownException.getMessage().contains("Account not found")) {
            response.status(404).send("{\"error\":\"Account not found\",\"message\":\"" + thrownException.getMessage() + "\"}");
        } else {
            response.status(500).send("{\"error\":\"Internal server error\",\"message\":\"" + thrownException.getMessage() + "\"}");
        }
    }
}
