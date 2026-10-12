package Shiroya_PaymentService.dto;

public record VerifyRazorpayPaymentRequest(
        String razorpayOrderId,
        String razorpayPaymentId,
        String razorpaySignature
) {}
