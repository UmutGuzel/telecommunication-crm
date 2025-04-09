package com.gygy.paymentservice.controller;

import an.awesome.pipelinr.Pipeline;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gygy.paymentservice.application.bill.command.create.CreateBillCommand;
import com.gygy.paymentservice.application.bill.command.create.dto.CreatedBillResponse;
import com.gygy.paymentservice.application.bill.query.getbyid.GetBillByIdQuery;
import com.gygy.paymentservice.application.bill.query.getbyid.dto.BillDetailResponse;
import com.gygy.paymentservice.application.bill.query.getlist.GetCustomerBillsQuery;
import com.gygy.paymentservice.application.bill.query.getlist.dto.BillListResponse;
import com.gygy.paymentservice.domain.entity.bill.BillStatus;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest(controllers = BillController.class,  // TODO ??? çalışmıyor
//        excludeAutoConfiguration = SecurityAutoConfiguration.class)
class BillControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean // Spring contextte olmayanları mocklamamı sağlar.
    private Pipeline pipeline;

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
    void whenCreateBill_thenBillShouldBeCreated() throws Exception {
        // given
        UUID customerId = UUID.randomUUID();
        CreateBillCommand command = new CreateBillCommand();
        command.setCustomerId(customerId);
        command.setTotalAmount(new BigDecimal("100.00"));

        CreatedBillResponse response = new CreatedBillResponse();
        response.setBillId(UUID.randomUUID());
        response.setTotalAmount(new BigDecimal("100.00"));
        response.setDueDate(LocalDate.now().plusDays(30));
        response.setStatus(BillStatus.PENDING);
        response.setCreatedAt(LocalDateTime.now());

        when(pipeline.send(any(CreateBillCommand.class))).thenReturn(response);

        // when & then
        mockMvc.perform(post("/api/v1/bills")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.billId").exists())
                .andExpect(jsonPath("$.totalAmount").value("100.00"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void whenGetBillsByCustomerId_thenBillsShouldBeReturned() throws Exception {
        // given
        UUID customerId = UUID.randomUUID();
        GetCustomerBillsQuery query = new GetCustomerBillsQuery(customerId);

        BillListResponse response1 = new BillListResponse();
        response1.setBillId(UUID.randomUUID());
        response1.setCustomerId(customerId);
        response1.setTotalAmount(new BigDecimal("100.00"));
        response1.setPaidAmount(BigDecimal.ZERO);
        response1.setDueDate(LocalDate.now().plusDays(30));
        response1.setStatus(BillStatus.PENDING);

        BillListResponse response2 = new BillListResponse();
        response2.setBillId(UUID.randomUUID());
        response2.setCustomerId(customerId);
        response2.setTotalAmount(new BigDecimal("200.00"));
        response2.setPaidAmount(BigDecimal.ZERO);
        response2.setDueDate(LocalDate.now().plusDays(30));
        response2.setStatus(BillStatus.PENDING);

        List<BillListResponse> responses = Arrays.asList(response1, response2);
        when(pipeline.send(any(GetCustomerBillsQuery.class))).thenReturn(responses);

        // when & then
        mockMvc.perform(get("/api/v1/bills/customer/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].customerId").value(customerId.toString()))
                .andExpect(jsonPath("$[1].customerId").value(customerId.toString()))
                .andExpect(jsonPath("$[0].totalAmount").value("100.00"))
                .andExpect(jsonPath("$[1].totalAmount").value("200.00"));
    }

    @Test
    void whenGetBillById_thenBillShouldBeReturned() throws Exception {
        // given
        UUID billId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        BillDetailResponse response = new BillDetailResponse();
        response.setBillId(billId);
        response.setCustomerId(customerId);
        response.setTotalAmount(new BigDecimal("100.00"));
        response.setPaidAmount(BigDecimal.ZERO);
        response.setDueDate(LocalDate.now().plusDays(30));
        response.setStatus(BillStatus.PENDING);

        when(pipeline.send(any(GetBillByIdQuery.class))).thenReturn(response);

        // when & then
        mockMvc.perform(get("/api/v1/bills/{billId}", billId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.billId").value(billId.toString()))
                .andExpect(jsonPath("$.customerId").value(customerId.toString()))
                .andExpect(jsonPath("$.totalAmount").value("100.00"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void whenDeleteBill_thenBillShouldBeDeleted() throws Exception {
        // given
        UUID billId = UUID.randomUUID();

        // when & then
        mockMvc.perform(delete("/api/v1/bills/{billId}", billId))
                .andExpect(status().isOk());
    }
}