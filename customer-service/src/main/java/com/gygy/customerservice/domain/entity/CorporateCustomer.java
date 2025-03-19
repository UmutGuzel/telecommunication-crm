//package com.gygy.customerservice.domain.entity;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//import org.hibernate.annotations.UuidGenerator;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.OneToOne;
//import jakarta.persistence.Table;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
//@Entity
//@Table(name = "corporate_customers")
//public class CorporateCustomer {
//
//    @Id
//    @UuidGenerator
//    private UUID id;
//
//    @Column(name = "tax_number")
//    private String taxNumber;
//
//    @Column(name = "company_name")
//    private String companyName;
//
//    @Column(name = "contact_person")
//    private String contactPerson;
//
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
//
//    @OneToOne
//    @JoinColumn(name = "customer_id", referencedColumnName = "id", unique = true)
//    private Customer customer;
//}