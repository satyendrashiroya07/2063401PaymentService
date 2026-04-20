package Shiroya_PaymentService.repo;

import Shiroya_PaymentService.entity.OutBoxEventPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutBoxEventPaymentRepo extends JpaRepository<OutBoxEventPayment, Long> {
    List<OutBoxEventPayment> findByStatus(String aNew);
}
