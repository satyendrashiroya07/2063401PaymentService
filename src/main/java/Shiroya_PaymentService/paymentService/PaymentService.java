package Shiroya_PaymentService.paymentService;

import Shiroya_PaymentService.dto.PaymentRequest;
import Shiroya_PaymentService.entity.PaymentEntity;
import Shiroya_PaymentService.repo.PaymentRepo;
import Shiroya_PaymentService.strategyPattern.PaymentFactory;
import Shiroya_PaymentService.strategyPattern.PaymentStrategy;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import shiroya.paymentEvent.PaymentEvent;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepo repo;

    @Autowired
    private PaymentFactory factory;

    @Autowired
    private KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    @Transactional
    public String processPayment(PaymentRequest request) {

        PaymentEntity payment = new PaymentEntity();
        payment.setOrderId(request.getOrderId());
        payment.setUserId(request.getUserId());
        payment.setAmount(request.getAmount());
        payment.setPaymentType(request.getPaymentType());
        payment.setStatus("PENDING");
        payment.setCreatedAt(LocalDateTime.now());

        repo.save(payment);

        PaymentStrategy strategy = factory.getStrategy(request.getPaymentType());

        boolean success = strategy.pay(request);

        if (success) {
            PaymentEvent event = PaymentEvent.builder().
                    orderId(request.getOrderId()).
                    productId(request.getProductId()).
                    email(request.getEmail()).
                    userId(request.getUserId()).
                    quantity(request.getQuantity()).
                    status("SUCCESS").build();
            payment.setStatus("SUCCESS");

            kafkaTemplate.send("payment-success1", event);
        } else {
            PaymentEvent event = PaymentEvent.builder().
                    orderId(request.getOrderId()).
                    productId(request.getProductId()).
                    email(request.getEmail()).
                    userId(request.getUserId()).
                    quantity(request.getQuantity()).
                    status("FAILED").build();
            payment.setStatus("FAILED");
            kafkaTemplate.send("payment-failed1", event);
        }

        repo.save(payment);

        return payment.getStatus();
    }
}