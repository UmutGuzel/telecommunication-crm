package presentation.controllers;

import an.awesome.pipelinr.Pipeline;
import application.bill.command.delete.DeleteBillCommand;
import application.payment.command.create.CreatePaymentCommand;
import application.payment.command.create.dto.CreatedPaymentResponse;
import application.payment.command.delete.DeletePaymentCommand;
import application.payment.command.delete.dto.DeletedPaymentResponse;
import application.payment.command.update.UpdatePaymentMethodCommand;
import application.payment.command.update.UpdatePaymentStatusCommand;
import application.payment.command.update.dto.UpdatedPaymentMethodResponse;
import application.payment.command.update.dto.UpdatedPaymentStatusResponse;
import core.web.BaseController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController extends BaseController {

    public PaymentController(Pipeline pipeline) {
        super(pipeline);
    }

    //ödeme oluşturur
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CreatedPaymentResponse create(@RequestBody CreatePaymentCommand command){
        return pipeline.send(command);
    }

    //ödeme silme
    @DeleteMapping
    @ResponseStatus(code = HttpStatus.OK)
    public DeletedPaymentResponse delete(@PathVariable UUID paymentId){
        DeletePaymentCommand command = new DeletePaymentCommand(paymentId);
        return pipeline.send(command);
    }

    // Ödeme yöntemi güncelleme
    @PutMapping("/{paymentId}/method")
    @ResponseStatus(code = HttpStatus.OK)
    public UpdatedPaymentMethodResponse updateMethod(@PathVariable UUID paymentId, @RequestBody UpdatePaymentMethodCommand command){
        command.setPaymentId(paymentId);
        return pipeline.send(command);
    }

    // Ödeme durumu güncelleme
    @PutMapping("/{paymentId}/status")
    @ResponseStatus(code = HttpStatus.OK)
    public UpdatedPaymentStatusResponse updateStatus(@PathVariable UUID paymentId, @RequestBody UpdatePaymentStatusCommand command){
       command.setPaymentId(paymentId);
       return pipeline.send(command);
    }









}
