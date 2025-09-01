package dev.lonecalvary78.app.payment.internal.processing.infra.circuitbreak;

import java.time.Duration;

import io.helidon.config.Config;
import io.helidon.faulttolerance.CircuitBreaker;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CircuitBreakUtil {
    public CircuitBreaker newCircuitBreaker(Config circuitBreakerConfig) {
        var volumeSize = circuitBreakerConfig.get("volume-size").asInt().orElse(10);
        var errorRatio = circuitBreakerConfig.get("error-ratio").asInt().orElse(30);
        var delayTime = circuitBreakerConfig.get("delay-time").asInt().orElse(200);
        var successThreshold = circuitBreakerConfig.get("success-threshold").asInt().orElse(2);
        return CircuitBreaker.builder()
        .volume(volumeSize)
        .errorRatio(errorRatio)
        .delay(Duration.ofMillis(delayTime))
        .successThreshold(successThreshold).build();
    }
}
