package Shiroya_PaymentService.strategyPattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PaymentFactory {

    @Autowired
    private UpiPaymentStrategy upi;

    @Autowired
    private CardPaymentStrategy card;

    public PaymentStrategy getStrategy(String type) {
        return switch (type) {
            case "UPI" -> upi;
            case "CARD" -> card;
            default -> throw new RuntimeException("Invalid payment type");
        };
    }

    private final Map<String, PaymentGatewayStrategy> gatewayStrategies;

    public PaymentFactory(
            Map<String, PaymentGatewayStrategy> gatewayStrategies) {
        this.gatewayStrategies = gatewayStrategies;
    }

    public PaymentGatewayStrategy getGatewayStrategy(String paymentType) {
        PaymentGatewayStrategy strategy =
                gatewayStrategies.get(paymentType.toUpperCase());

        if (strategy == null) {
            throw new IllegalArgumentException(
                    "Unsupported payment gateway: " + paymentType);
        }

        return strategy;
    }
}
