package Shiroya_PaymentService.consumer;

import Shiroya_PaymentService.dto.PaymentRequest;
import Shiroya_PaymentService.paymentService.PaymentService;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import shiroya.orderEvent.OrderEvent;

@Slf4j
@Component
public class OrderEventListener {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private Tracer tracer;

    @KafkaListener(topics = "order-created")
    public void handleOrderEvent(ConsumerRecord<String, OrderEvent> record) {

        String traceId = null;
        OrderEvent event = record.value();
        // Create new span with existing traceId
        Span newSpan = tracer.nextSpan().name("kafka-consume");

        try {
            PaymentRequest request = new PaymentRequest();
            request.setOrderId(event.getOrderId());
            request.setUserId(event.getUserId());
            request.setProductId(event.getProductId());
            request.setAmount(event.getAmount());
            request.setEmail(event.getEmail());
            request.setQuantity(event.getQuantity());
            request.setPaymentType(event.getPaymentType());

            if (record.headers().lastHeader("traceId") != null) {
                traceId = new String(record.headers().lastHeader("traceId").value());
            }
            log.info("Received traceId: {}", traceId);

            paymentService.processPayment(request);
        }
        finally {
            newSpan.end();
        }
    }
}