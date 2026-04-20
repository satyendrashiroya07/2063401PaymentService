package Shiroya_PaymentService.strategyPattern;

import Shiroya_PaymentService.dto.PaymentRequest;
import org.springframework.stereotype.Component;

@Component
public class UpiPaymentStrategy implements PaymentStrategy {
    public boolean pay(PaymentRequest request) {
        // simulate success
        return true;
    }
}
