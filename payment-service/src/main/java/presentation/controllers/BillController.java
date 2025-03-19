package presentation.controllers;

import an.awesome.pipelinr.Pipeline;
import application.bill.command.create.CreateBillCommand;
import application.bill.command.create.dto.CreatedBillResponse;
import application.bill.command.delete.DeleteBillCommand;
import application.bill.command.delete.dto.DeletedBillResponse;
import application.bill.command.update.UpdateBillAmountCommand;
import application.bill.command.update.UpdateBillStatusCommand;
import application.bill.command.update.dto.UpdatedBillAmountResponse;
import application.bill.command.update.dto.UpdatedBillStatusResponse;
import application.bill.query.getbyid.GetBillByIdQuery;
import application.bill.query.getbyid.dto.BillDetailResponse;
import application.bill.query.getlist.GetBillsByDateRangeQuery;
import application.bill.query.getlist.GetBillsByStatusQuery;
import application.bill.query.getlist.GetBillsQuery;
import application.bill.query.getlist.GetCustomerBillsQuery;
import application.bill.query.getlist.dto.BillListResponse;
import application.payment.command.create.CreatePaymentCommand;
import application.payment.command.create.dto.CreatedPaymentResponse;
import core.web.BaseController;
import domain.entity.bill.BillStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bills")
public class BillController extends BaseController {

    public BillController(Pipeline pipeline) {
        super(pipeline);
    }

    //yeni fatura oluşturur.
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CreatedBillResponse create(@RequestBody CreateBillCommand command){
        return pipeline.send(command); // best practice
        //return command.execute(pipeline);
    }

    //fatura id'ye göre siler
    @DeleteMapping("/{billId}")
    @ResponseStatus(code = HttpStatus.OK)
    public DeletedBillResponse delete(@PathVariable UUID billId){
        DeleteBillCommand deleteBillCommand = new DeleteBillCommand(billId);
        return pipeline.send(deleteBillCommand);
        //return deleteBillCommand.execute(pipeline);
    }

    // fatura tuturını günceller
    @PutMapping("/{billId}/amount")
    @ResponseStatus(code = HttpStatus.OK)
    public UpdatedBillAmountResponse updateAmount(@PathVariable UUID billId, @RequestBody UpdateBillAmountCommand command){
        command.setBillId(billId);
        return pipeline.send(command);
        //return command.execute(pipeline);
    }

    // fatura durumunu günceller
    @PutMapping("/{billId}/status")
    @ResponseStatus(code = HttpStatus.OK)
    public UpdatedBillStatusResponse updateStatus(@PathVariable UUID billId, @RequestBody UpdateBillStatusCommand command){
        command.setBillId(billId);
        return pipeline.send(command);
        //return command.execute(pipeline);
    }

    //fatura id'ye göre detay getirir.
    @GetMapping("/{billId}")
    @ResponseStatus(code = HttpStatus.OK)
    public BillDetailResponse getById(@PathVariable UUID billId){
        GetBillByIdQuery getBillByIdQuery = new GetBillByIdQuery(billId);
        return pipeline.send(getBillByIdQuery);
        //return getBillByIdQuery.execute(pipeline);
    }

    // tarih aralığına göre
    @GetMapping("date-range")
    @ResponseStatus(code = HttpStatus.OK)
    public List<BillListResponse> dateRange(@RequestParam LocalDate startDate, LocalDate endDate){
        GetBillsByDateRangeQuery query = new GetBillsByDateRangeQuery(startDate, endDate);
        return pipeline.send(query);
    }

    //duruma göre fatura getir
    @GetMapping("/status/{status}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<BillListResponse> getBillsByStatus(@PathVariable BillStatus status) {
        GetBillsByStatusQuery query = new GetBillsByStatusQuery(status);
        return pipeline.send(query);
    }


    // sistemdeki tüm faturalar
    @GetMapping("/all")
    @ResponseStatus(code = HttpStatus.OK)
    public List<BillListResponse> getAllBills(){
        GetBillsQuery getBillsQuery = new GetBillsQuery();
        return pipeline.send(getBillsQuery);
        //return getBillsQuery.execute(pipeline);
    }

    // müşterinin faturalarını getir
    @GetMapping("/customer/{customerId}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<BillListResponse> getCustomerBills(@PathVariable UUID customerId){
        GetCustomerBillsQuery getCustomerBillsQuery = new GetCustomerBillsQuery(customerId);
        return pipeline.send(getCustomerBillsQuery);
        //return getCustomerBillsQuery.execute(pipeline);
    }
}
