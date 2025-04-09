package com.gygy.contractservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.UUID;

@FeignClient(name = "customer-service",url = "http://localhost:9020")
public interface CustomerClient {

   @GetMapping("/api/v1/customers")
   List<CustomerDto> getAllCustomers();
}

// CustomerDto sınıfını burada tanımlıyoruz
class CustomerDto {
    private UUID id;
    private String name;
    private String email;
    private String phoneNumber;
    
    // Getter ve Setter metodları
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}



