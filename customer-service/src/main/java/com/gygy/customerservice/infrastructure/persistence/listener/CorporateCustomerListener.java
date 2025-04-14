package com.gygy.customerservice.infrastructure.persistence.listener;

import com.gygy.customerservice.domain.entity.CorporateCustomer;
import com.gygy.customerservice.infrastructure.security.EncryptionService;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.PostLoad;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class CorporateCustomerListener {
    private final EncryptionService encryptionService;

    @Value("${encryption.salt-key}")
    private String saltKey;

    @PrePersist
    @PreUpdate
    public void encryptTaxNumber(CorporateCustomer customer) {
        if (customer.getTaxNumber() != null && !customer.getTaxNumber().isEmpty()) {
            try {
                // Calculate hash of the tax number
                String hash = calculateHash(customer.getTaxNumber());
                customer.setTaxNumberHash(hash);

                // Encrypt the tax number
                customer.setTaxNumber(encryptionService.encrypt(customer.getTaxNumber()));
            } catch (Exception e) {
                // Log the error but don't throw exception to prevent transaction rollback
                System.err.println("Error processing tax number: " + e.getMessage());
            }
        }
    }

    @PostLoad
    public void decryptTaxNumber(CorporateCustomer corporateCustomer) {
        if (corporateCustomer.getTaxNumber() != null) {
            String decryptedTaxNumber = encryptionService.decrypt(corporateCustomer.getTaxNumber());
            corporateCustomer.setTaxNumber(decryptedTaxNumber);
        }
    }

    private String calculateHash(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String saltedInput = saltKey + input;
        byte[] hash = digest.digest(saltedInput.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }
} 