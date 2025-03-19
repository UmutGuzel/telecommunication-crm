package com.gygy.contractservice.repository;

import com.gygy.contractservice.entity.Commitment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CommitmentRepository extends JpaRepository<Commitment, UUID> {
    
    /**
     * Verilen isme sahip bir taahhüdün veritabanında var olup olmadığını kontrol eder.
     * @param name Kontrol edilecek taahhüt adı
     * @return İsim varsa true, yoksa false döner
     */
    boolean existsByName(String name);
    
    /**
     * Belirtilen ID'ye sahip olmayan ama aynı isimli bir taahhüdün
     * veritabanında var olup olmadığını kontrol eder. Güncelleme işlemlerinde
     * kendi kaydı dışındaki kayıtlarla çakışma olup olmadığını anlamak için kullanılır.
     * 
     * @param name Kontrol edilecek taahhüt adı
     * @param id Hariç tutulacak kayıt ID'si
     * @return Eşleşen kayıt varsa true, yoksa false döner
     */
    boolean existsByNameAndIdNot(String name, UUID id);
    
    /**
     * Belirtilen müşteri ID'sine sahip ve belirtilen durumdaki taahhütlerin
     * varlığını kontrol eder. Örneğin, bir müşterinin aktif taahhüdü olup
     * olmadığını kontrol etmek için kullanılabilir.
     * 
     * @param customerId Müşteri ID'si
     * @param status Taahhüt durumu (örn. "ACTIVE", "CANCELLED", "COMPLETED")
     * @return Eşleşen kayıt varsa true, yoksa false döner
     */
    boolean existsByContractDetail_CustomerIdAndStatus(UUID customerId, String status);
}
