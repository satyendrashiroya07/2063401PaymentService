package Shiroya_PaymentService.repo;

import Shiroya_PaymentService.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepo extends JpaRepository<PaymentEntity,Long> {
}
