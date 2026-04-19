package Shiroya_PaymentService.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String orderId;
    private String userId;
    private double amount;

    private String paymentType;
    private String status;

    @Column(unique = true, nullable = false, updatable = false)
    private String transactionId;

    @PrePersist
    public void generateTransactionId() {
        if (this.transactionId == null) {
            this.transactionId = UUID.randomUUID().toString();
        }
    }

    private LocalDateTime createdAt;
}
