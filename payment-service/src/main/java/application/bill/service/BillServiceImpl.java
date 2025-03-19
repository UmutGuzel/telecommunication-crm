package application.bill.service;

import domain.entity.bill.Bill;
import domain.entity.bill.BillStatus;
import domain.exception.bill.BillNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import persistence.bill.BillRepository;
import persistence.payment.PaymentRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;

    @Override
    public void save(Bill bill) {
        billRepository.save(bill);
    }

    @Override
    public void saveAll(List<Bill> bills) {
        billRepository.saveAll(bills);
    }


    @Override
    public Bill findById(UUID billId) {
        return billRepository.findById(billId)
                .orElseThrow(() -> new BillNotFoundException(billId));
    }

    @Override
    public List<Bill> findByCustomerId(UUID customerId) {
        List<Bill> bills = billRepository.findByCustomerId(customerId);
        if (bills.isEmpty()) {
            throw new BillNotFoundException(customerId);
        }
        return bills;
    }

    @Override
    public List<Bill> findAllBills() {
        return billRepository.findAll();
    }

    @Override
    public List<Bill> findByStatus(BillStatus status) {
        return billRepository.findByStatus(status);
    }

    @Override
    public List<Bill> findBillsByDateRange(LocalDate startDate, LocalDate endDate) {
        return billRepository.findByDueDateBetween(startDate, endDate);
    }


    @Override
    public List<Bill> findPendingBillsDueBefore(LocalDate date) {
        return billRepository.findByDueDateBeforeAndStatus(date, BillStatus.PENDING);
    }

//    @Override
//    public List<Bill> findByCustomerIdAndStatus(UUID customerId, BillStatus status) {
//        List<Bill> bills = billRepository.findByCustomerIdAndStatus(customerId, status);
//        if (bills.isEmpty()) {
//            throw new BillNotFoundException(customerId);}
//
//        return bills;
//    }

}
