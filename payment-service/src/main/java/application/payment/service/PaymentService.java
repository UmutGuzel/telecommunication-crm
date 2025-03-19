package application.payment.service;

import domain.entity.payment.Payment;
import domain.entity.payment.PaymentMethod;
import domain.entity.payment.PaymentStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface PaymentService {

    void save(Payment payment);

    Payment findById(UUID id);

    List<Payment> findByCustomerId(UUID customerId);

    List<Payment> findByCustomerIdAndPaymentStatus(UUID customerId, PaymentStatus paymentStatus);

    // Payment status'e göre ödeme bulma
    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);

    // Faturaya göre ödemeleri bulma
    List<Payment> findByBill_BillId(UUID billId);

}