package Shiroya_PaymentService.dto;

public record RazorpayOrderResponse(
        String internalOrderId,
        String razorpayOrderId,
        long amount,
        String currency,
        String keyId
) {}
