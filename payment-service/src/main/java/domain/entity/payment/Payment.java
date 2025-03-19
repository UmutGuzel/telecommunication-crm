package domain.entity.payment;

import domain.entity.bill.Bill;
import domain.exception.bill.BillAmountException;
import domain.exception.payment.PaymentValidationException;
import jakarta.persistence.*;
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
    private UUID paymentId;

    @Column(nullable = false)
    private UUID customerId; // TODO: customer or user ??

    @Column(name = "payment_amount", nullable = false)
    private BigDecimal paidAmount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private LocalDateTime paymentDate; //ödeme yapılınca belirlenecek, map ile.

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

    // ödeme işlemini yapan metot
    public void processPayment(){

        if (bill == null) {
            throw new PaymentValidationException("Payment must be linked to a bill");
        }

        try {
            bill.applyPayment(paidAmount);
            paymentStatus = PaymentStatus.COMPLETED;
            paymentDate = LocalDateTime.now();

        } catch (BillAmountException e) {
            paymentStatus = PaymentStatus.FAILED;
            failureReason = "Payment amount is incorrect: " + e.getMessage();
            throw e;
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

