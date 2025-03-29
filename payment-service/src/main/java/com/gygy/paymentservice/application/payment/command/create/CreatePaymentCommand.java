package com.gygy.paymentservice.application.payment.command.create;

import an.awesome.pipelinr.Command;
import com.gygy.common.constants.KafkaTopics;
import com.gygy.common.events.paymentservice.payment.PaymentFailedEvent;
import com.gygy.common.events.paymentservice.payment.PaymentSuccessEvent;
import com.gygy.common.kafka.producer.EventProducer;
import com.gygy.paymentservice.application.bill.service.BillService;
import com.gygy.paymentservice.application.payment.command.create.dto.CreatedPaymentResponse;
import com.gygy.paymentservice.application.payment.service.PaymentService;
import com.gygy.paymentservice.domain.entity.payment.Payment;
import com.gygy.paymentservice.domain.entity.payment.PaymentMethod;
import com.gygy.paymentservice.domain.entity.payment.PaymentStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentCommand implements Command<CreatedPaymentResponse> {

        @NotNull(message = "Customer ID is required")
        private UUID customerId;

        @NotNull(message = "Bill ID is required")
        private UUID billId;

        @NotNull(message = "Payment amount is required")
        @DecimalMin(value = "0.01", message = "Payment amount must be greater than 0")
        private BigDecimal paidAmount;

        @NotNull(message = "Payment method is required")
        private PaymentMethod paymentMethod;

        @Component
        @RequiredArgsConstructor
        public static class CreatePaymentCommandHandler
                        implements Handler<CreatePaymentCommand, CreatedPaymentResponse> {

                private final PaymentService paymentService;
                private final BillService billService;
                private final EventProducer eventProducer;

                @Override
                @Transactional
                public CreatedPaymentResponse handle(CreatePaymentCommand command) {
                        // Faturayı bul
                        var bill = billService.findById(command.getBillId());

                        // Yeni ödeme oluştur
                        Payment payment = new Payment();
                        payment.setBill(bill);
                        payment.setCustomerId(command.getCustomerId());
                        payment.setPaidAmount(command.getPaidAmount());
                        payment.setPaymentMethod(command.getPaymentMethod());
                        payment.setPaymentStatus(PaymentStatus.PENDING);
                        payment.setPaymentDate(LocalDateTime.now());

                        // Ödeme işlemini gerçekleştir
                        try {
                                payment.processPayment();
                                paymentService.save(payment);

                                // Başarılı ödeme event'i
                                eventProducer.sendEvent(
                                                KafkaTopics.PAYMENT_SUCCESS,
                                                new PaymentSuccessEvent(
                                                                bill.getBillId(),
                                                                command.getCustomerId(),
                                                                command.getPaidAmount(),
                                                                command.getPaymentMethod().name()));
                        } catch (Exception e) {
                                paymentService.save(payment);
                                // Başarısız ödeme event'i
                                eventProducer.sendEvent(
                                                KafkaTopics.PAYMENT_FAILED,
                                                new PaymentFailedEvent(
                                                                bill.getBillId(),
                                                                command.getCustomerId(),
                                                                command.getPaidAmount(),
                                                                e.getMessage()));
                                throw e;
                        }

                        // Fatura durumunu güncelle
                        bill.updateStatus();
                        bill.setUpdatedAt(LocalDateTime.now());

                        billService.save(bill);

                        return new CreatedPaymentResponse(
                                        payment.getPaymentId(),
                                        payment.getBill().getBillId(),
                                        payment.getPaidAmount(),
                                        payment.getPaymentMethod(),
                                        payment.getPaymentStatus(),
                                        payment.getPaymentDate());
                }
        }
}
