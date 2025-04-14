package com.gygy.customerservice.infrastructure.persistence.listener;

import com.gygy.customerservice.domain.entity.IndividualCustomer;
import com.gygy.customerservice.infrastructure.security.EncryptionService;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class IndividualCustomerListener {
    private final EncryptionService encryptionService;

    @Value("${encryption.salt-key}")
    private String saltKey;

    @PrePersist
    @PreUpdate
    public void encryptIdentityNumber(IndividualCustomer customer) {
        if (customer.getIdentityNumber() != null && !customer.getIdentityNumber().isEmpty()) {
            try {
                // Calculate hash of the identity number
                String hash = calculateHash(customer.getIdentityNumber());
                customer.setIdentityNumberHash(hash);
                
                // Encrypt the identity number
                customer.setIdentityNumber(encryptionService.encrypt(customer.getIdentityNumber()));
            } catch (Exception e) {
                // Log the error but don't throw exception to prevent transaction rollback
                System.err.println("Error processing identity number: " + e.getMessage());
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

    private String calculateHash(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String saltedInput = saltKey + input;
        byte[] hash = digest.digest(saltedInput.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }
} 