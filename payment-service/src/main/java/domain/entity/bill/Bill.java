package domain.entity.bill;

import domain.entity.payment.Payment;
import domain.exception.bill.BillAmountException;
import domain.exception.bill.BillStatusException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "bills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID billId;

    @Column(nullable = false)
    private UUID customerId; // TODO:customer or user ??

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private BigDecimal paidAmount;

    @Column(nullable = false)
    private LocalDate dueDate; // TODO: eğer saat ve dakika kullaanacksak localdatetime

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BillStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    private boolean deleted = false; // Soft delete

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
    private List<Payment> payments;

//    public BigDecimal getRemainingAmount() {
//        return totalAmount.subtract(paidAmount);
//    }

    // Ödeme alındığında çağrılacak metod
    public void applyPayment(BigDecimal paymentAmount) {
        if (paymentAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BillAmountException("Payment amount must be greater than 0");
        }

        if (status == BillStatus.PAID) {
            throw new BillStatusException("This bill has already been paid");
        }

        if (status == BillStatus.DELETED) {
            throw new BillStatusException("Payment cannot be made for deleted bill");
        }

        if (this.paidAmount.add(paymentAmount).compareTo(this.totalAmount) > 0) {
            throw new BillAmountException("The payment amount cannot exceed the bill total");
        }

        // Ödenen tutarı güncelle
        paidAmount = paidAmount.add(paymentAmount);

        // Fatura durumunu güncelle
        updateStatus();
    }

    // Durum güncelleme metodu
    public void updateStatus() {
        if (isFullyPaid()) {
            this.status = BillStatus.PAID;
        } else if (isPartiallyPaid()) {
            this.status = BillStatus.PARTIALLY_PAID;
        } else if (isOverdue()) {
            this.status = BillStatus.OVERDUE; //
        } else {
            this.status = BillStatus.PENDING;
        }
    }

    // Faturanın tam ödenip ödenmediğini kontrol eder
    public boolean isFullyPaid() {
        return this.paidAmount.compareTo(this.totalAmount) >= 0;
    }

    // Faturanın kısmi olarak ödenip ödenmediğini kontrol eder
    public boolean isPartiallyPaid() {
        return this.paidAmount.compareTo(BigDecimal.ZERO) > 0 && !isFullyPaid();
    }

    // Faturanın tarihini kontrol eder
    public boolean isOverdue() {

        return !isFullyPaid() && LocalDate.now().isAfter(dueDate);
    }

    // Durum geçişlerini kontrol eden metod
    public void setStatus(BillStatus newStatus) {
        if (!isValidStatusTransition(this.status, newStatus)) {
            throw new BillStatusException(this.status, newStatus);
        }
        this.status = newStatus;
    }

    private boolean isValidStatusTransition(BillStatus currentStatus, BillStatus newStatus) {

        // Eğer mevcut durum null ise, sadece PENDING'e geçişe izin ver
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
