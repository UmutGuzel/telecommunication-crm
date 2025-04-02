package com.gygy.contractservice.service.impl;

import com.gygy.contractservice.client.CustomerClient;
import com.gygy.contractservice.core.exception.type.BusinessException;
import com.gygy.contractservice.dto.contractDetail.ContractDetailListiningDto;
import com.gygy.contractservice.dto.contractDetail.CreateContractDetailDto;
import com.gygy.contractservice.dto.contractDetail.DeleteContractDetailDto;
import com.gygy.contractservice.dto.contractDetail.UpdateContractDetailDto;
import com.gygy.contractservice.entity.Contract;
import com.gygy.contractservice.entity.ContractDetail;
import com.gygy.contractservice.kafka.event.ContractDetailEvent;
import com.gygy.contractservice.kafka.producer.KafkaProducerService;
import com.gygy.contractservice.mapper.ContractDetailMapper;
import com.gygy.contractservice.repository.ContractDetailRepository;
import com.gygy.contractservice.service.ContractDetailService;
import com.gygy.contractservice.service.ContractService;
import com.gygy.customerservice.application.customer.query.GetListCustomerItemDto;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.gygy.contractservice.constant.GeneralConstant.*;

@Service
public class ContractDetailServiceImpl implements ContractDetailService {

    private final ContractDetailRepository contractDetailRepository;
    private final ContractService contractService;
    private final ContractDetailMapper contractDetailMapper;
    private static final Logger logger = LoggerFactory.getLogger(ContractDetail.class);
    private final KafkaProducerService kafkaProducerService;
    private final CustomerClient customerClient;

    public ContractDetailServiceImpl(ContractDetailRepository contractDetailRepository, ContractService contractService , ContractDetailMapper contractDetailMapper, KafkaProducerService kafkaProducerService, CustomerClient customerClient) {
        this.contractDetailRepository = contractDetailRepository;
        this.contractService = contractService;
        this.contractDetailMapper = contractDetailMapper;
        this.kafkaProducerService = kafkaProducerService;
        this.customerClient = customerClient;
    }

    @Override
    public ContractDetail findById(UUID id) {
        logger.debug("Searching for contract detail with ID: {}", id);
        ContractDetail contractDetail = contractDetailRepository.findById(id).orElseThrow(()-> new RuntimeException("Contract Detail not found"));
        if (contractDetail != null) {
            logger.info("Contract Detail found with ID: {}", id);
        } else {
            logger.warn("Contract Detail not found with ID: {}", id);
        }
        return contractDetail;
    }

    @Override
    public void add(CreateContractDetailDto createContractDetailDto) {
       Contract contract=contractService.findById(createContractDetailDto.getContractId()).orElseThrow(()-> new RuntimeException("Contract not found"));
        logger.debug("Creating contract detail for contract: {}", contract.getId());
        try {
            ContractDetail contractDetail=contractDetailMapper.createContractDetailFromCreateContractDetailDto(createContractDetailDto);
            contractDetail.setContract(contract);
            List<GetListCustomerItemDto> response= customerClient.getAllCustomers();
            contractDetail.setCustomerId(response.get(0).getId());
            contractDetail.setEmail(response.get(0).getEmail());
            contractDetail.setPhoneNumber(response.get(0).getPhoneNumber());

            contractDetailRepository.save(contractDetail);
            logger.info("Contract detail created successfully");
            kafkaProducerService
                    .sendCreatedContractDetailEvent(
                            ContractDetailEvent
                                    .builder()
                                    .discountId(contractDetail.getCustomerId())
                                    .contractName(contractDetail.getName())
                                    .signatureDate(contract.getSignatureDate())
                                    .email(contractDetail.getEmail())
                                    .customerName(contractDetail.getCustomerName())
                                    .build());
        } catch (Exception e) {
            logger.error("Error creating contract detail: {}", e.getMessage(), e);
            throw e;
        }


    }

    @Override
    public List<ContractDetailListiningDto> getAll() {

        logger.debug(FETCHING_ALL_CONTRACT_DETAIL);
        List<ContractDetail>  contractDetails = contractDetailRepository.findAll();
        List<ContractDetailListiningDto> contractDetailListiningDtos1 = contractDetails.stream()
                .map(contractDetailMapper::toContractDetailListiningDto )
                .collect(Collectors.toList());
        logger.info("Found {} billing plan", contractDetailListiningDtos1.size());
        return contractDetailListiningDtos1;

    }

    @Override
    public ContractDetail update(UpdateContractDetailDto updateContractDetailDto) {
        logger.info("Updating billing plan with ID: {}", updateContractDetailDto.getId());
        try {
            Contract contract=contractService.findById(updateContractDetailDto.getContractId()).orElseThrow(()-> new RuntimeException("Contract not found"));
            ContractDetail contractDetail=contractDetailMapper.updateContractDetailFromUpdateContractDetailDto(updateContractDetailDto);
            contractDetail.setId(contractDetail.getId());
            contractDetail.setContract(contract);
            ContractDetail updatedContractDetail=contractDetailRepository.save(contractDetail);
            logger.info("Successfully updated billing plan with ID: {}", updatedContractDetail.getId());
            return updatedContractDetail;
        } catch (Exception e) {
            logger.error("Error updating billing plan: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void delete(DeleteContractDetailDto deleteContractDetailDto) {
        logger.info("Attempting to delete contract detail  with ID: {}", deleteContractDetailDto.getId());
        try {
            ContractDetail contractDetail=contractDetailRepository.findById(deleteContractDetailDto.getId()).orElseThrow(() -> {
                        logger.error("Contract Detail not found for deletion with ID: {}", deleteContractDetailDto.getId());
                        return new BusinessException(CONTRACT_DETAIL_NOT_FOUND);
                    });
            contractDetailRepository.delete(contractDetail);
            logger.info("Successfully deleted contract detail with ID: {}", deleteContractDetailDto.getId());
        } catch (Exception e) {
            logger.error("Error deleting contract detail: {}", e.getMessage(), e);
            throw e;
        }

    }

    @Override
    public List<ContractDetailListiningDto> getContractByDate(UUID id, LocalDate startDate, LocalDate endDate) {
        return contractDetailRepository
                .findById(id)
                .stream()
                .filter(contractDetail -> (contractDetail.getStartDate().isAfter(startDate)|| contractDetail.getStartDate().isEqual(startDate))  &&
                        (contractDetail.getEndDate().isBefore(endDate) || contractDetail.getEndDate().isEqual(endDate)))
                .map(contractDetail -> new ContractDetailListiningDto(
                        contractDetail.getContractDetailType()
                        ,contractDetail.getContract().getId()
                        ,contractDetail.getStatus()
                        ,contractDetail.getEndDate()
                        ,contractDetail.getStartDate()
                        ,contractDetail.getCustomerId()
                        ,contractDetail.getServiceType())).toList();
    }

    @Override
    public List<ContractDetailListiningDto> getIndividualContractDetails() {
        return contractDetailRepository
                .findAll()
                .stream()
                .filter(contractDetail -> "INDIVIDUAL".equals(contractDetail.getContractDetailType().toString()))
                .map(contractDetail -> new ContractDetailListiningDto(
                        contractDetail.getContractDetailType()
                        ,contractDetail.getContract().getId()
                        ,contractDetail.getStatus()
                        ,contractDetail.getEndDate()
                        ,contractDetail.getStartDate()
                        ,contractDetail.getCustomerId()
                        ,contractDetail.getServiceType())).toList();
    }

    @Override
    public List<ContractDetailListiningDto> getCorporateContractDetails() {
        return contractDetailRepository
                .findAll().stream().filter(contractDetail -> "CORPORATE".equals(contractDetail.getContractDetailType().toString()))
                .map(contractDetail -> new ContractDetailListiningDto(
                        contractDetail.getContractDetailType()
                        ,contractDetail.getContract().getId()
                        ,contractDetail.getStatus()
                        ,contractDetail.getEndDate()
                        ,contractDetail.getStartDate()
                        ,contractDetail.getCustomerId()
                        ,contractDetail.getServiceType())).toList();
    }

    @Override
    public List<ContractDetailListiningDto> getContractsByCustomerId(UUID customerId) {
        return contractDetailRepository.findAll().stream().filter(contractDetail -> contractDetail.getCustomerId().equals(customerId))
                .map(contractDetail -> new ContractDetailListiningDto(
                        contractDetail.getContractDetailType()
                        ,contractDetail.getContract().getId()
                        ,contractDetail.getStatus()
                        ,contractDetail.getEndDate()
                        ,contractDetail.getStartDate()
                        ,contractDetail.getCustomerId()
                        ,contractDetail.getServiceType())).toList();
    }
    @Override
    public List<ContractDetailListiningDto> getExpiredContractsByCustomerId(UUID customerId) {
        return contractDetailRepository.findAll().stream().filter(contractDetail -> contractDetail.getEndDate().isBefore(LocalDate.now()) && contractDetail.getCustomerId().equals(customerId))
                .map(contractDetail -> new ContractDetailListiningDto(
                        contractDetail.getContractDetailType()
                        ,contractDetail.getContract().getId()
                        ,contractDetail.getStatus()
                        ,contractDetail.getEndDate()
                        ,contractDetail.getStartDate()
                        ,contractDetail.getCustomerId()
                        ,contractDetail.getServiceType())).toList();
}

    @Override
    public List<ContractDetailListiningDto> getContractsByMonthAndYear(int month, int year) {
        return contractDetailRepository.findAll().stream().filter(contractDetail -> contractDetail.getEndDate().getMonthValue() == month && contractDetail.getEndDate().getYear() == year)
                .map(contractDetail -> new ContractDetailListiningDto(
                        contractDetail.getContractDetailType()
                        ,contractDetail.getContract().getId()
                        ,contractDetail.getStatus()
                        ,contractDetail.getEndDate()
                        ,contractDetail.getStartDate()
                        ,contractDetail.getCustomerId()
                        ,contractDetail.getServiceType()))
                .toList();
    }

}
