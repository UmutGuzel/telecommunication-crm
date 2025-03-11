package domain.billing;


import domain.payment.Payment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

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
public class Bill {

    @Id
    @UuidGenerator
    private UUID id;

    private String customerId; // TODO:customer or user ??

    private BigDecimal totalAmount;

    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    private BillStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL) // cascade -> Fatura silindiğinde veya güncellendiğinde ilişkili ödemelerin de etkilenmesini sağlar
    private List<Payment> payments;
}
