package Shiroya_PaymentService.dto;

import java.math.BigDecimal;

public record CreateRazorpayOrderRequest(
        String orderId,
        String userId,
        String productId,
        String email,
        int quantity,
        BigDecimal amount
) {}
