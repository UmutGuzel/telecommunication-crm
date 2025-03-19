package persistence.payment;

import domain.entity.payment.Payment;
import domain.entity.payment.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findByCustomerId(UUID customerId);

    List<Payment> findByBill_BillId(UUID billId);

    List<Payment> findByPaymentStatus(PaymentStatus status);

    List<Payment> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Payment> findByCustomerIdAndPaymentStatus(UUID customerId, PaymentStatus status);
}
