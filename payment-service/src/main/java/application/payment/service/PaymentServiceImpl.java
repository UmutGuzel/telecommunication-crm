package application.payment.service;

import application.bill.service.BillService;
import domain.entity.bill.Bill;
import domain.entity.bill.BillStatus;
import domain.entity.payment.Payment;
import domain.entity.payment.PaymentMethod;
import domain.entity.payment.PaymentStatus;
import domain.exception.payment.PaymentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import persistence.payment.PaymentRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public void save(Payment payment) {
        paymentRepository.save(payment);
    }

    @Override
    public Payment findById(UUID id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException(id));
    }

    @Override
    public List<Payment> findByCustomerId(UUID customerId) {
        List<Payment> payments = paymentRepository.findByCustomerId(customerId);
        if (payments.isEmpty()) {
            throw new PaymentNotFoundException(customerId);
        }
        return payments;
    }

    @Override
    public List<Payment> findByCustomerIdAndPaymentStatus(UUID customerId, PaymentStatus paymentStatus) {
        List<Payment> payments = paymentRepository.findByCustomerIdAndPaymentStatus(customerId, paymentStatus);
        if (payments.isEmpty()) {
            throw new PaymentNotFoundException(customerId);
        }
        return payments;
    }

    // Payment status'e göre ödeme bulma
    @Override
    public List<Payment> findByPaymentStatus(PaymentStatus paymentStatus) {
        List<Payment> payments = paymentRepository.findByPaymentStatus(paymentStatus);
        if (payments.isEmpty()) {
            throw new PaymentNotFoundException(paymentStatus);
        }
        return payments;
    }

    @Override
    public List<Payment> findByBill_BillId(UUID billId) {
        List<Payment> payments = paymentRepository.findByBill_BillId(billId);
        if (payments.isEmpty()) {
            throw new PaymentNotFoundException(billId);
        }
        return payments;
    }

    // Faturaya göre ödemeleri bulma

    }