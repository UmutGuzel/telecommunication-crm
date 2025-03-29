package com.gygy.paymentservice.domain.entity.payment;

import com.gygy.paymentservice.domain.entity.bill.Bill;
import com.gygy.paymentservice.domain.exception.bill.BillAmountException;
import com.gygy.paymentservice.domain.exception.payment.PaymentValidationException;
import com.gygy.paymentservice.domain.exception.payment.PaymentAmountException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID paymentId;

    @Column(nullable = false)
    @NotNull(message = "Customer ID cannot be null")
    private UUID customerId;

    @Column(name = "payment_amount", nullable = false)
    @NotNull(message = "Payment amount cannot be null")
    @Positive(message = "Payment amount must be greater than 0")
    private BigDecimal paidAmount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Payment method cannot be null")
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Payment status cannot be null")
    private PaymentStatus paymentStatus;

    private LocalDateTime paymentDate;

    @Column
    private String failureReason;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime deletedAt;

    @ManyToOne
    @JoinColumn(name = "bill_id", nullable = false)
    @NotNull(message = "Bill cannot be null")
    private Bill bill;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void setCustomerId(UUID customerId) {
        if (customerId == null) {
            throw new PaymentValidationException("Customer ID cannot be null");
        }
        this.customerId = customerId;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        if (paidAmount == null) {
            throw new PaymentValidationException("Payment amount cannot be null");
        }
        if (paidAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new PaymentAmountException("Payment amount must be greater than 0");
        }
        this.paidAmount = paidAmount;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            throw new PaymentValidationException("Payment method cannot be null");
        }
        this.paymentMethod = paymentMethod;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        if (paymentStatus == null) {
            throw new PaymentValidationException("Payment status cannot be null");
        }
        this.paymentStatus = paymentStatus;
    }

    public void setBill(Bill bill) {
        if (bill == null) {
            throw new PaymentValidationException("Bill cannot be null");
        }
        this.bill = bill;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        if (paymentDate != null && paymentDate.isAfter(LocalDateTime.now())) {
            throw new PaymentValidationException("Payment date cannot be in the future");
        }
        this.paymentDate = paymentDate;
    }

    public void processPayment() {
        if (bill == null) {
            paymentStatus = PaymentStatus.FAILED;
            failureReason = "Payment verification error: Payment must be linked to a bill";
            throw new PaymentValidationException("Payment must be linked to a bill");
        }

        try {
            bill.applyPayment(paidAmount);
            paymentStatus = PaymentStatus.COMPLETED;
            paymentDate = LocalDateTime.now();
        } catch (BillAmountException e) {
            paymentStatus = PaymentStatus.FAILED;
            failureReason = "Payment amount is incorrect: The payment amount cannot exceed the bill total";
            throw new PaymentAmountException(e.getMessage(), e);
        } catch (PaymentValidationException e) {
            paymentStatus = PaymentStatus.FAILED;
            failureReason = "Payment verification error: " + e.getMessage();
            throw e;
        } catch (Exception e) {
            paymentStatus = PaymentStatus.FAILED;
            failureReason = "An unknown error occurred.";
            throw e;
        }
    }

    public boolean isCompleted() {
        return paymentStatus == PaymentStatus.COMPLETED;
    }

    public boolean isFailed() {
        return paymentStatus == PaymentStatus.FAILED;
    }
}
