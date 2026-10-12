package Shiroya_PaymentService.paymentService;


import Shiroya_PaymentService.dto.VerifyRazorpayPaymentRequest;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class RazorpayService {

    private final RazorpayClient razorpayClient;

    public RazorpayService(RazorpayClient razorpayClient) {
        this.razorpayClient = razorpayClient;
    }

    public JSONObject createOrder(
            double amountInPaise, String receiptId)
            throws RazorpayException {

        if (amountInPaise <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be greater than zero");
        }

        JSONObject request = new JSONObject();
        int amountInPaiseInt = (int) amountInPaise;
        request.put("amount", amountInPaiseInt);
        request.put("currency", "INR");
        request.put("receipt", receiptId);

        Order order = razorpayClient.orders.create(request);

        JSONObject response = new JSONObject();
        response.put("razorpayOrderId", Collections.singleton(order.get("id")));
        response.put("amount", Collections.singleton(order.get("amount")));
        response.put("currency", Collections.singleton(order.get("currency")));

        return response;
    }

    public String verifyPayment(VerifyRazorpayPaymentRequest request) {
        return "";
    }
}

