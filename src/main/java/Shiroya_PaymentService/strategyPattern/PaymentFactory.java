package Shiroya_PaymentService.strategyPattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
}
