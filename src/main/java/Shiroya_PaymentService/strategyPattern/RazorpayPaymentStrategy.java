package Shiroya_PaymentService.strategyPattern;

import Shiroya_PaymentService.dto.PaymentRequest;
import Shiroya_PaymentService.dto.VerifyRazorpayPaymentRequest;
import Shiroya_PaymentService.paymentService.RazorpayService;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component("RAZORPAY")
@RequiredArgsConstructor
public class RazorpayPaymentStrategy implements PaymentGatewayStrategy {

    private final RazorpayService razorpayPaymentService;

    @Override
    public JSONObject createPayment(PaymentRequest request) throws RazorpayException {
        // Adapt this call to the fields in your actual PaymentRequest.
        return razorpayPaymentService.createOrder(
                /* CreateRazorpayOrderRequest built from request */
                request.getAmount(), request.getProductId());
    }

    @Override
    public String verifyPayment(VerifyRazorpayPaymentRequest request) {
        return razorpayPaymentService.verifyPayment(request);
    }
}
