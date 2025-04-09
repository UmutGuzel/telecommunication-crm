package com.gygy.paymentservice.application.bill.service.impl;

import com.gygy.paymentservice.application.bill.service.BillService;
import com.gygy.paymentservice.domain.entity.bill.Bill;
import com.gygy.paymentservice.domain.entity.bill.BillStatus;
import com.gygy.paymentservice.domain.exception.bill.BillNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.gygy.paymentservice.persistence.bill.BillRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
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

    @Override
    public List<Bill> findUnpaidBills() {
        return billRepository.findByStatusIn(Arrays.asList(BillStatus.PENDING, BillStatus.PARTIALLY_PAID));
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
