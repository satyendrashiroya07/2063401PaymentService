package Shiroya_PaymentService.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class OutBoxEventPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String aggregateType;
    private String aggregateId;
    private String eventType;

    @Column(columnDefinition = "TEXT")
    private String payload;

    private String status;

}
