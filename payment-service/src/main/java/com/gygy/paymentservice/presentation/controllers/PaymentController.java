package com.gygy.paymentservice.presentation.controllers;

import an.awesome.pipelinr.Pipeline;
import com.gygy.paymentservice.application.payment.command.create.CreatePaymentCommand;
import com.gygy.paymentservice.application.payment.command.create.dto.CreatedPaymentResponse;
import com.gygy.paymentservice.application.payment.command.delete.DeletePaymentCommand;
import com.gygy.paymentservice.application.payment.command.delete.dto.DeletedPaymentResponse;
import com.gygy.paymentservice.application.payment.command.update.UpdatePaymentMethodCommand;
import com.gygy.paymentservice.application.payment.command.update.UpdatePaymentStatusCommand;
import com.gygy.paymentservice.application.payment.command.update.dto.UpdatedPaymentMethodResponse;
import com.gygy.paymentservice.application.payment.command.update.dto.UpdatedPaymentStatusResponse;
import com.gygy.paymentservice.application.payment.query.getbyid.GetPaymentByIdQuery;
import com.gygy.paymentservice.application.payment.query.getbyid.dto.PaymentDetailResponse;
import com.gygy.paymentservice.core.web.BaseController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController extends BaseController {

    public PaymentController(Pipeline pipeline) {
        super(pipeline);
    }

    // ödeme oluşturur
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CreatedPaymentResponse create(@RequestBody CreatePaymentCommand command) {
        return pipeline.send(command);
    }

    @GetMapping("/{paymentId}")
    @ResponseStatus(code = HttpStatus.OK)
    public PaymentDetailResponse getPayment(@PathVariable UUID paymentId) {
        GetPaymentByIdQuery getPaymentByIdQuery = new GetPaymentByIdQuery(paymentId);
        return pipeline.send(getPaymentByIdQuery);
    }

    // ödeme silme
    @DeleteMapping
    @ResponseStatus(code = HttpStatus.OK)
    public DeletedPaymentResponse delete(@PathVariable UUID paymentId) {
        DeletePaymentCommand command = new DeletePaymentCommand(paymentId);
        return pipeline.send(command);
    }

    // Ödeme yöntemi güncelleme
    @PutMapping("/{paymentId}/method")
    @ResponseStatus(code = HttpStatus.OK)
    public UpdatedPaymentMethodResponse updateMethod(@PathVariable UUID paymentId,
            @RequestBody UpdatePaymentMethodCommand command) {
        command.setPaymentId(paymentId);
        return pipeline.send(command);
    }

    // Ödeme durumu güncelleme
    @PutMapping("/{paymentId}/status")
    @ResponseStatus(code = HttpStatus.OK)
    public UpdatedPaymentStatusResponse updateStatus(@PathVariable UUID paymentId,
            @RequestBody UpdatePaymentStatusCommand command) {
        command.setPaymentId(paymentId);
        return pipeline.send(command);
    }

}
