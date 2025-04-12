package com.gygy.customerservice.infrastructure.persistence.listener;

import com.gygy.customerservice.domain.entity.IndividualCustomer;
import com.gygy.customerservice.infrastructure.security.EncryptionService;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IndividualCustomerListener {
    private final EncryptionService encryptionService;

    @PrePersist
    @PreUpdate
    public void encryptIdentityNumber(IndividualCustomer customer) {
        if (customer.getIdentityNumber() != null && !customer.getIdentityNumber().isEmpty()) {
            try {
                customer.setIdentityNumber(encryptionService.encrypt(customer.getIdentityNumber()));
            } catch (Exception e) {
                // Log the error but don't throw exception to prevent transaction rollback
                System.err.println("Error encrypting identity number: " + e.getMessage());
            }
        }
    }

    @PostLoad
    @PostPersist
    @PostUpdate
    public void decryptIdentityNumber(IndividualCustomer customer) {
        if (customer.getIdentityNumber() != null && !customer.getIdentityNumber().isEmpty()) {
            try {
                // Check if the value is already decrypted (not encrypted)
                if (!customer.getIdentityNumber().matches("^[A-Za-z0-9+/=]+$")) {
                    return;
                }
                customer.setIdentityNumber(encryptionService.decrypt(customer.getIdentityNumber()));
            } catch (Exception e) {
                // Log the error but don't throw exception to prevent transaction rollback
                System.err.println("Error decrypting identity number: " + e.getMessage());
            }
        }
    }
} 