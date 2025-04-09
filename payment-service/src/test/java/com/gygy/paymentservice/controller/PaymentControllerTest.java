package com.gygy.paymentservice.controller;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gygy.paymentservice.application.bill.service.BillService;
import com.gygy.paymentservice.application.payment.command.create.CreatePaymentCommand;
import com.gygy.paymentservice.domain.entity.bill.Bill;
import com.gygy.paymentservice.domain.entity.bill.BillStatus;
import com.gygy.paymentservice.domain.entity.payment.Payment;
import com.gygy.paymentservice.domain.entity.payment.PaymentMethod;
import com.gygy.paymentservice.domain.entity.payment.PaymentStatus;
import com.gygy.paymentservice.application.payment.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BillService billService;

    @BeforeEach
    void setUp() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(BigDecimal.class, new JsonSerializer<BigDecimal>() {
            @Override
            public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                gen.writeString(value.setScale(2).toString());
            }
        });

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(module);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);
    }

    @Test
    void whenCreatePayment_thenStatus201() throws Exception {
        // given
        UUID customerId = UUID.randomUUID();
        UUID billId = UUID.randomUUID();

        // CreatePaymentCommand nesnesini oluşturuyoruz
        CreatePaymentCommand createPaymentCommand = new CreatePaymentCommand();
        createPaymentCommand.setCustomerId(customerId);
        createPaymentCommand.setPaidAmount(new BigDecimal("100.00"));
        createPaymentCommand.setBillId(billId);
        createPaymentCommand.setPaymentMethod(PaymentMethod.CREDIT_CARD);

        // Fatura nesnesini mock'lıyoruz
        Bill mockBill = new Bill();
        mockBill.setBillId(billId);
        mockBill.setTotalAmount(new BigDecimal("100.00"));
        mockBill.setPaidAmount(BigDecimal.ZERO);
        mockBill.setDueDate(LocalDate.now().plusDays(10)); // Ödeme tarihi ileri olmalı
        mockBill.setStatus(BillStatus.PENDING);

        // Mock: Fatura bulunamadığında exception fırlatıyor
        when(billService.findById(billId)).thenReturn(mockBill);  // Burada billService mocklanacak

        // Mock'lama: paymentService save metodu çağrıldığında hiçbir şey yapmıyor
        doNothing().when(paymentService).save(any(Payment.class));

        // when/then
        mockMvc.perform(post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPaymentCommand)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.paidAmount").value("100.00"));
    }
    
    @Test
    void whenGetPayment_thenStatus200() throws Exception {
        UUID paymentId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        // Bill nesnesini oluşturuyoruz
        Bill mockBill = new Bill();
        mockBill.setBillId(UUID.randomUUID());
        mockBill.setTotalAmount(new BigDecimal("150.00"));
        mockBill.setPaidAmount(BigDecimal.ZERO);
        mockBill.setDueDate(LocalDate.now().plusDays(5));
        mockBill.setStatus(BillStatus.PENDING);

        // Payment nesnesini oluşturuyoruz
        Payment payment = new Payment();
        payment.setPaymentId(paymentId);
        payment.setCustomerId(customerId);
        payment.setPaidAmount(new BigDecimal("100.00"));
        payment.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setBill(mockBill);

        // Mock tanımlamaları
        when(paymentService.findById(paymentId)).thenReturn(payment);

        // Testin çalıştırılması
        mockMvc.perform(get("/api/v1/payments/{id}", paymentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId").value(paymentId.toString()))
                .andExpect(jsonPath("$.customerId").value(customerId.toString()))
                .andExpect(jsonPath("$.paidAmount").value("100.00"))
                .andExpect(jsonPath("$.paymentStatus").value("PENDING"));
    }




//    @Test
//    void whenProcessPayment_thenStatus200() throws Exception {
//        // given
//        UUID paymentId = UUID.randomUUID();
//        UUID customerId = UUID.randomUUID();
//        Payment payment = new Payment();
//        payment.setPaymentId(paymentId);
//        payment.setPaidAmount(new BigDecimal("100.00"));
//        payment.setCustomerId(customerId);
//        payment.setPaymentDate(LocalDateTime.now());
//        payment.setPaymentStatus(PaymentStatus.PENDING);
//
//        when(paymentService.findById(paymentId)).thenReturn(payment);
//
//        // when/then
//        mockMvc.perform(post("/api/v1/payments/" + paymentId + "/process"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.paymentStatus").value("COMPLETED"));
//    }
}