package Shiroya_PaymentService.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private String orderId;
    private String userId;
    private double amount;
    private String productId;
    private String email;
    private int quantity;
    private String paymentType;
}
