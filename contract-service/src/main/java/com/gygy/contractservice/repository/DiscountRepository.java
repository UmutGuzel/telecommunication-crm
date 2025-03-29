package com.gygy.contractservice.repository;


import com.gygy.contractservice.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
//find ile isimlendir get kullanma
//RUNTİME ILLEGAL EXCEPTİON
public interface DiscountRepository extends JpaRepository<Discount, UUID> {
}
