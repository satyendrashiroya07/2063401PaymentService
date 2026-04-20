package Shiroya_PaymentService.paymentService;

import Shiroya_PaymentService.dto.PaymentRequest;
import Shiroya_PaymentService.entity.OutBoxEventPayment;
import Shiroya_PaymentService.entity.PaymentEntity;
import Shiroya_PaymentService.repo.OutBoxEventPaymentRepo;
import Shiroya_PaymentService.repo.PaymentRepo;
import Shiroya_PaymentService.strategyPattern.PaymentFactory;
import Shiroya_PaymentService.strategyPattern.PaymentStrategy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import shiroya.orderEvent.OrderEvent;
import shiroya.paymentEvent.PaymentEvent;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class PaymentService {

    @Autowired
    private PaymentRepo repo;

    @Autowired
    private PaymentFactory factory;

    @Autowired
    private KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    @Autowired
    private  ObjectMapper objectMapper;

    @Autowired
    private OutBoxEventPaymentRepo outBoxEventPaymentRepo;

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

            OutBoxEventPayment kafkaDbEvent = new OutBoxEventPayment();
            kafkaDbEvent.setAggregateType("ORDER");
            kafkaDbEvent.setAggregateId(request.getOrderId());
            kafkaDbEvent.setEventType("payment-success1");
            kafkaDbEvent.setPayload(convertToJson(event));
            kafkaDbEvent.setStatus("NEW");

            outBoxEventPaymentRepo.save(kafkaDbEvent);

        } else {
            PaymentEvent event = PaymentEvent.builder().
                    orderId(request.getOrderId()).
                    productId(request.getProductId()).
                    email(request.getEmail()).
                    userId(request.getUserId()).
                    quantity(request.getQuantity()).
                    status("FAILED").build();
            payment.setStatus("FAILED");

            OutBoxEventPayment kafkaDbEvent = new OutBoxEventPayment();
            kafkaDbEvent.setAggregateType("ORDER");
            kafkaDbEvent.setAggregateId(request.getOrderId());
            kafkaDbEvent.setEventType("payment-failed1");
            kafkaDbEvent.setPayload(convertToJson(event));
            kafkaDbEvent.setStatus("NEW");

            outBoxEventPaymentRepo.save(kafkaDbEvent);

        }

        repo.save(payment);

        return payment.getStatus();
    }


    @Scheduled(fixedDelay = 5000)
    public void publishOutboxEvents() {

        List<OutBoxEventPayment> events = outBoxEventPaymentRepo.findByStatus("NEW");

        for (OutBoxEventPayment event : events) {
            try {

                PaymentEvent paymentEvent =
                        objectMapper.readValue(event.getPayload(), PaymentEvent.class);

                System.out.println("Event Topic: " + event.getEventType());

                kafkaTemplate.send(event.getEventType(), paymentEvent);

                System.out.println("Reached Here");

                event.setStatus("SENT");

                outBoxEventPaymentRepo.save(event);

            } catch (Exception e) {
                log.error("Failed to publish event {}", event.getId(), e);
            }
        }
    }


    private String convertToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting to JSON", e);
        }
    }
}