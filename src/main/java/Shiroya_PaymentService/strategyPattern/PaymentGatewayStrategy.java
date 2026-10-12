package Shiroya_PaymentService.strategyPattern;

import Shiroya_PaymentService.dto.PaymentRequest;
import Shiroya_PaymentService.dto.VerifyRazorpayPaymentRequest;
import com.razorpay.RazorpayException;
import org.json.JSONObject;

public interface PaymentGatewayStrategy {

    JSONObject createPayment(PaymentRequest request) throws RazorpayException;

    String verifyPayment(VerifyRazorpayPaymentRequest request);
}