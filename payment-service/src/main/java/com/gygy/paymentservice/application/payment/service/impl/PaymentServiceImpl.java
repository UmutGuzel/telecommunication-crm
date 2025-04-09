package com.gygy.paymentservice.application.payment.service.impl;

import com.gygy.paymentservice.application.payment.service.PaymentService;
import com.gygy.paymentservice.domain.entity.payment.Payment;
import com.gygy.paymentservice.domain.entity.payment.PaymentStatus;
import com.gygy.paymentservice.domain.exception.payment.PaymentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.gygy.paymentservice.persistence.payment.PaymentRepository;

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