package Shiroya_PaymentService.strategyPattern;

import Shiroya_PaymentService.dto.PaymentRequest;

public interface PaymentStrategy {
    boolean pay(PaymentRequest request);
}
