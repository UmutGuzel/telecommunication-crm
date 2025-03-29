package com.gygy.paymentservice.domain.entity.bill;

import com.gygy.paymentservice.domain.entity.payment.Payment;
import com.gygy.paymentservice.domain.exception.bill.BillAmountException;
import com.gygy.paymentservice.domain.exception.bill.BillValidationException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "bills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted = false")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID billId;

    @Column(nullable = false)
    @NotNull(message = "Customer ID cannot be null")
    private UUID customerId;

    @Column(nullable = false)
    @NotNull(message = "Total amount cannot be null")
    @Positive(message = "Total amount must be greater than 0")
    private BigDecimal totalAmount;

    @Column(nullable = false)
    @NotNull(message = "Paid amount cannot be null")
    private BigDecimal paidAmount;

    @Column(nullable = false)
    @NotNull(message = "Due date cannot be null")
    private LocalDate dueDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status cannot be null")
    private BillStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    private boolean deleted = false;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
    private List<Payment> payments;

    public void setCustomerId(UUID customerId) {
        if (customerId == null) {
            throw new BillValidationException("Customer ID cannot be null");
        }
        this.customerId = customerId;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        if (totalAmount == null) {
            throw new BillValidationException("Total amount cannot be null");
        }
        if (totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BillAmountException("Total amount must be greater than 0");
        }
        this.totalAmount = totalAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        if (paidAmount == null) {
            throw new BillValidationException("Paid amount cannot be null");
        }
        if (paidAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BillAmountException("Paid amount cannot be negative");
        }
        if (paidAmount.compareTo(totalAmount) > 0) {
            throw new BillAmountException("Paid amount cannot exceed total amount");
        }
        this.paidAmount = paidAmount;
    }

    public void setDueDate(LocalDate dueDate) {
        if (dueDate == null) {
            throw new BillValidationException("Due date cannot be null");
        }
        if (dueDate.isBefore(LocalDate.now())) {
            throw new BillValidationException("Due date cannot be in the past");
        }
        this.dueDate = dueDate;
    }

    public void setStatus(BillStatus status) {
        if (status == null) {
            throw new BillValidationException("Status cannot be null");
        }
        if (!isValidStatusTransition(this.status, status)) {
            throw new BillValidationException(this.status, status);
        }
        this.status = status;
    }

    public void applyPayment(BigDecimal paymentAmount) {
        if (paymentAmount == null) {
            throw new BillValidationException("Payment amount cannot be null");
        }
        if (paymentAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BillAmountException("Payment amount must be greater than 0");
        }

        if (status == BillStatus.PAID) {
            throw new BillValidationException("This bill has already been paid");
        }

        if (status == BillStatus.DELETED) {
            throw new BillValidationException("Payment cannot be made for deleted bill");
        }

        if (this.paidAmount.add(paymentAmount).compareTo(this.totalAmount) > 0) {
            throw new BillAmountException("The payment amount cannot exceed the bill total");
        }

        paidAmount = paidAmount.add(paymentAmount);
        updateStatus();
    }

    public void updateStatus() {
        if (isFullyPaid()) {
            this.status = BillStatus.PAID;
        } else if (isPartiallyPaid()) {
            this.status = BillStatus.PARTIALLY_PAID;
        } else if (isOverdue()) {
            this.status = BillStatus.OVERDUE;
        } else {
            this.status = BillStatus.PENDING;
        }
    }

    public boolean isFullyPaid() {
        return this.paidAmount.compareTo(this.totalAmount) >= 0;
    }

    public boolean isPartiallyPaid() {
        return this.paidAmount.compareTo(BigDecimal.ZERO) > 0 && !isFullyPaid();
    }

    public boolean isOverdue() {
        return !isFullyPaid() && LocalDate.now().isAfter(dueDate);
    }

    private boolean isValidStatusTransition(BillStatus currentStatus, BillStatus newStatus) {
        if (currentStatus == null) {
            return newStatus == BillStatus.PENDING;
        }

        return switch (currentStatus) {
            case PENDING -> newStatus == BillStatus.PAID || newStatus == BillStatus.PARTIALLY_PAID
                    || newStatus == BillStatus.OVERDUE || newStatus == BillStatus.CANCELLED
                    || newStatus == BillStatus.DELETED;

            case OVERDUE -> newStatus == BillStatus.PAID || newStatus == BillStatus.PARTIALLY_PAID
                    || newStatus == BillStatus.CANCELLED || newStatus == BillStatus.DELETED;

            case PAID -> newStatus == BillStatus.DELETED;

            case PARTIALLY_PAID -> newStatus == BillStatus.PAID || newStatus == BillStatus.OVERDUE
                    || newStatus == BillStatus.CANCELLED || newStatus == BillStatus.DELETED;

            case DELETED -> false;

            case CANCELLED -> false;
        };
    }
}
