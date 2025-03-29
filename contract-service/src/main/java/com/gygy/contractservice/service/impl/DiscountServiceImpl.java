package com.gygy.contractservice.service.impl;

import com.gygy.contractservice.dto.discount.CreateDiscountDto;
import com.gygy.contractservice.dto.discount.DeleteDiscountDto;
import com.gygy.contractservice.dto.discount.DiscountListiningDto;
import com.gygy.contractservice.dto.discount.UpdateDiscountDto;
import com.gygy.contractservice.entity.BillingPlan;
import com.gygy.contractservice.entity.ContractDetail;
import com.gygy.contractservice.entity.Discount;
import com.gygy.contractservice.event.DiscountEvent;
import com.gygy.contractservice.event.NotificationEvent;
import com.gygy.contractservice.mapper.DiscountMapper;
import com.gygy.contractservice.model.enums.DiscountType;
import com.gygy.contractservice.model.enums.Status;
import com.gygy.contractservice.repository.DiscountRepository;
import com.gygy.contractservice.service.BillingPlanService;
import com.gygy.contractservice.service.ContractDetailService;
import com.gygy.contractservice.service.DiscountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.gygy.contractservice.constant.GeneralConstant.SEND_DISCOUNT_ERROR;
import static com.gygy.contractservice.model.enums.EventType.DISCOUNT_APPLIED;

@Service
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository discountRepository;
    private final BillingPlanService billingPlanService;
    private final ContractDetailService contractDetailService;
    private final StreamBridge streamBridge;
    private static final Logger log = LoggerFactory.getLogger(DiscountServiceImpl.class);
    private final DiscountMapper discountMapper;

    public DiscountServiceImpl(DiscountRepository discountRepository, BillingPlanService billingPlanService,
                               ContractDetailService contractDetailService, StreamBridge streamBridge, DiscountMapper discountMapper) {
        this.discountRepository = discountRepository;
        this.billingPlanService = billingPlanService;
        this.contractDetailService = contractDetailService;
        this.streamBridge = streamBridge;
        this.discountMapper = discountMapper;
    }

    @Override
    public List<Discount> findAllById(List<UUID> ids) {
        return discountRepository.findAllById(ids);
    }

    @Override
    public Discount findById(UUID id) {
        return discountRepository.findById(id).orElse(null);
    }

    @Override
    public void add(CreateDiscountDto createDiscountDto) {
        List<BillingPlan> billingPlan = billingPlanService.findAll(createDiscountDto.getBillingPlanId());
        ContractDetail contractDetail = contractDetailService.findById(createDiscountDto.getContractDetailId());
        /*
        Discount discount = new Discount();
        discount.setDiscountType(createDiscountDto.getDiscountType());
        discount.setAmount(createDiscountDto.getAmount());
        discount.setBillingPlans(billingPlan);
        discount.setPercentage(createDiscountDto.getPercentage());
        discount.setStartDate(createDiscountDto.getStartDate());
        discount.setEndDate(createDiscountDto.getEndDate());
        discount.setContractDetail(contractDetail);
        discount.setStatus(createDiscountDto.getStatus());
        discount.setCustomerId(createDiscountDto.getCustomerId());

         */
        Discount discount=discountMapper.createDiscountFromCreateDiscountDto(createDiscountDto);
        discount.setBillingPlans(billingPlan);
        discount.setContractDetail(contractDetail);
        discountRepository.save(discount);


        // Send discount event to Kafka
        DiscountEvent event = new DiscountEvent();
        event.setDiscountId(discount.getId());
        event.setContractDetailId(createDiscountDto.getContractDetailId());
        event.setCustomerId(createDiscountDto.getCustomerId());
        //event.setEventType(DISCOUNT_CREATED);
        event.setEventDate(LocalDate.now());
        event.setDiscountData(discount);

        try {
            streamBridge.send("discount-events-out-0", event);
        } catch (Exception e) {
            log.error("Failed to send discount event: {}", e.getMessage());
            // Continue execution even if Kafka is unavailable
        }

        // Send notification event to Kafka
        NotificationEvent notification = new NotificationEvent();
        notification.setCustomerId(createDiscountDto.getCustomerId());
        notification.setTitle("Discount Created");
        notification.setMessage("A new discount has been created for your contract.");
        notification.setType("DISCOUNT");
        notification.setStatus("SUCCESS");
        notification.setEventType("NOTIFICATION");
        notification.setEventDate(LocalDate.now().toString());

        try {
            streamBridge.send("notification-events-out-0", notification);
        } catch (Exception e) {
            log.error("Failed to send notification event: {}", e.getMessage());
            // Continue execution even if Kafka is unavailable
        }
    }

    @Override
    public List<DiscountListiningDto> getAll() {
        List<DiscountListiningDto> discountListiningDtos = discountRepository
                .findAll()
                .stream()
                .map(discount -> new DiscountListiningDto(discount.getDiscountType(), discount.getAmount(),
                        discount.getPercentage(), discount.getContractDetail(), discount.getEndDate(),
                        discount.getStartDate(), discount.getBillingPlans(), discount.getCreatedAt(),
                        discount.getUpdatedAt()))
                .toList();
        return discountListiningDtos;
    }

    @Override
    public Discount update(UpdateDiscountDto updateDiscountDto) {
        ContractDetail contractDetail = contractDetailService.findById(updateDiscountDto.getContractDetailId());
        List<BillingPlan> billingPlan = billingPlanService.findAll(updateDiscountDto.getBillingPlanId());

        /*
        discount.setDiscountType(updateDiscountDto.getDiscountType());
        discount.setAmount(updateDiscountDto.getAmount());
        discount.setPercentage(updateDiscountDto.getPercentage());
        discount.setStartDate(updateDiscountDto.getStartDate());
        discount.setEndDate(updateDiscountDto.getEndDate());
        discount.setAmount(updateDiscountDto.getAmount());
        discount.setContractDetail(contractDetail);

         */
        Discount discount=discountMapper.updateDiscountFromUpdateDiscountDto(updateDiscountDto);
        discount.setId(updateDiscountDto.getId());
        discount.setContractDetail(contractDetail);
        discount.setBillingPlans(billingPlan);
        return discountRepository.save(discount);
    }

    @Override
    public void delete(DeleteDiscountDto deleteDiscountDto) {
        Discount discount = discountRepository.findById(deleteDiscountDto.getId())
                .orElseThrow(() -> new RuntimeException("Discount not found"));
        discountRepository.delete(discount);
    }

    @Override
    public List<DiscountListiningDto> getActiveDiscounts() {
        return discountRepository.findAll().stream()
                .filter(discounts -> "ACTIVE".equals(discounts.getStatus().toString()))
                .map(discount -> new DiscountListiningDto(discount.getDiscountType(), discount.getAmount(),
                        discount.getPercentage(), discount.getContractDetail(), discount.getEndDate(),
                        discount.getStartDate(), discount.getBillingPlans(), discount.getCreatedAt(),
                        discount.getUpdatedAt()))
                .toList();
    }

    @Override
    public List<DiscountListiningDto> getActiveDiscountsByCustomerId(UUID customerId) {
        return discountRepository.findAll().stream()
                .filter(discount -> "ACTIVE".equals(discount.getStatus().toString())
                        && discount.getCustomerId().equals(customerId))
                .map(discount -> new DiscountListiningDto(discount.getDiscountType(), discount.getAmount(),
                        discount.getPercentage(), discount.getContractDetail(), discount.getEndDate(),
                        discount.getStartDate(), discount.getBillingPlans(), discount.getCreatedAt(),
                        discount.getUpdatedAt()))
                .toList();
    }

    @Override
    public List<DiscountListiningDto> getDiscountsByContractId(UUID contractId) {
        return discountRepository.findAll().stream()
                .filter(discount -> discount.getContractDetail().getId().equals(contractId))
                .map(discount -> new DiscountListiningDto(discount.getDiscountType(), discount.getAmount(),
                        discount.getPercentage(), discount.getContractDetail(), discount.getEndDate(),
                        discount.getStartDate(), discount.getBillingPlans(), discount.getCreatedAt(),
                        discount.getUpdatedAt()))
                .toList();
    }

    @Override
    public Discount applyDiscountForAnnualPackage(CreateDiscountDto createDiscountDto) {
        if ("ANNUAL".equals(createDiscountDto.getBillingCycleType().toString())) {
            // Create and save discount
            Discount discount = new Discount();
            discount.setDiscountType(DiscountType.YEARLY_SUBSCRIPTION);
            discount.setPercentage(5.0);
            discount.setStartDate(LocalDate.now());
            discount.setEndDate(createDiscountDto.getEndDate());
            discount.setCreatedAt(LocalDate.now());
            discount.setUpdatedAt(LocalDate.now());
            discount.setStatus(Status.ACTIVE);
            discount.setCustomerId(createDiscountDto.getCustomerId());
            discount.setAmount(0.1); // Geçici bir değer veya hesaplanmış değer atan

            Discount savedDiscount = discountRepository.save(discount);

            DiscountEvent event = new DiscountEvent();
            event.setDiscountId(savedDiscount.getId());
            event.setContractDetailId(createDiscountDto.getContractDetailId());
            event.setCustomerId(createDiscountDto.getCustomerId());
            event.setEventType(DISCOUNT_APPLIED); //STRİNG DEĞİL ENUM OLARAK VERİLİCEK
            event.setEventDate(LocalDate.now());
            event.setDiscountData(savedDiscount);


            try {
                streamBridge.send("discount-events-out-0", event);
            } catch (Exception e) {
                log.error(SEND_DISCOUNT_ERROR, e.getMessage()); //ENUM VERİLSİN FAİL MESSAGE OLABİLİR
                // Continue execution even if Kafka is unavailable
            }

            NotificationEvent notification = new NotificationEvent();
            notification.setCustomerId(createDiscountDto.getCustomerId());
            notification.setTitle("Discount Applied");
            notification.setMessage("A 5% discount has been applied to your annual subscription.");
            notification.setType("DISCOUNT");
            notification.setStatus("SUCCESS");
            notification.setEventType("NOTIFICATION");
            notification.setEventDate(LocalDate.now().toString());

            try {
                streamBridge.send("notification-events-out-0", notification);
            } catch (Exception e) {
                log.error("Failed to send notification event: {}", e.getMessage());
                // Continue execution even if Kafka is unavailable
            }

            return savedDiscount;
        }
        throw new IllegalArgumentException("Invalid billing cycle type: " + createDiscountDto.getBillingCycleType());
    }

    private double calculateDiscountedPrice(double originalPrice, double discountPercentage) {
        double discount = originalPrice * (discountPercentage / 100);
        return originalPrice - discount;
    }
}
