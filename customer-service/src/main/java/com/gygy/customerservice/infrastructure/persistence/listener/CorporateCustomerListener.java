package com.gygy.customerservice.infrastructure.persistence.listener;

import com.gygy.customerservice.domain.entity.CorporateCustomer;
import com.gygy.customerservice.infrastructure.security.EncryptionService;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.PostLoad;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CorporateCustomerListener {
    private final EncryptionService encryptionService;

    @PrePersist
    @PreUpdate
    public void encryptTaxNumber(CorporateCustomer corporateCustomer) {
        if (corporateCustomer.getTaxNumber() != null) {
            String encryptedTaxNumber = encryptionService.encrypt(corporateCustomer.getTaxNumber());
            corporateCustomer.setTaxNumber(encryptedTaxNumber);
        }
    }

    @PostLoad
    public void decryptTaxNumber(CorporateCustomer corporateCustomer) {
        if (corporateCustomer.getTaxNumber() != null) {
            String decryptedTaxNumber = encryptionService.decrypt(corporateCustomer.getTaxNumber());
            corporateCustomer.setTaxNumber(decryptedTaxNumber);
        }
    }
} 