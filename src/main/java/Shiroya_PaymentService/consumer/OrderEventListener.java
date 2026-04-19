package Shiroya_PaymentService.consumer;

import Shiroya_PaymentService.dto.PaymentRequest;
import Shiroya_PaymentService.paymentService.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import shiroya.orderEvent.OrderEvent;

@Component
public class OrderEventListener {

    @Autowired
    private PaymentService paymentService;

    @KafkaListener(topics = "order-created")
    public void handleOrderEvent(OrderEvent event) {

        PaymentRequest request = new PaymentRequest();
        request.setOrderId(event.getOrderId());
        request.setUserId(event.getUserId());
        request.setProductId(event.getProductId());
        request.setAmount(event.getAmount());
        request.setEmail(event.getEmail());
        request.setQuantity(event.getQuantity());
        request.setPaymentType(event.getPaymentType());

        paymentService.processPayment(request);
    }
}