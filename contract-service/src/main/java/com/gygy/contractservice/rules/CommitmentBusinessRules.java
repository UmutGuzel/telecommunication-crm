package com.gygy.contractservice.rules;

import com.gygy.contractservice.core.exception.type.BusinessException;
import com.gygy.contractservice.repository.CommitmentRepository;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Component
public class CommitmentBusinessRules {
    
    private final CommitmentRepository commitmentRepository;
    
    public CommitmentBusinessRules(CommitmentRepository commitmentRepository) {
        this.commitmentRepository = commitmentRepository;
    }
    

    public void checkIfCommitmentNameExists(String name) {
        if (commitmentRepository.existsByName(name)) {
            throw new BusinessException("A commitment with name '" + name + "' already exists");
        }
    }

    public void checkIfCommitmentNameExistsForUpdate(UUID id, String name) {
        if (commitmentRepository.existsByNameAndIdNot(name, id)) {
            throw new BusinessException("A commitment with name '" + name + "' already exists");
        }
    }

    public void checkIfCommitmentPeriodAndTypeAreConsistent(String commitmentType, int periodInMonths) {
        //Premium taahhütler en az 6 ay olmalıdır
        if (commitmentType.equals("PREMIUM") && periodInMonths < 6) {
            throw new BusinessException("Premium commitments must be at least 6 months");
        }
        
        //Kurumsal taahhütler en az 12 ay olmalıdır
        if (commitmentType.equals("CORPORATE") && periodInMonths < 12) {
            throw new BusinessException("Corporate commitments must be at least 12 months");
        }
        
        //Hiçbir taahhüt 36 aydan uzun olamaz
        if (periodInMonths > 36) {
            throw new BusinessException("Commitment period cannot exceed 36 months");
        }
    }
    

    public void checkIfCommitmentDatesAreValid(LocalDate startDate, LocalDate endDate) {

        // Başlangıç tarihi bugünden önce olamaz
        if (startDate.isBefore(LocalDate.now())) {
            throw new BusinessException("Commitment start date cannot be in the past");
        }
        
        // Başlangıç tarihi bitiş tarihinden sonra olamaz
        if (startDate.isAfter(endDate)) {
            throw new BusinessException("Start date cannot be after end date");
        }
        
        // Taahhüt süresi ile bitiş tarihi tutarlı olmalı
        long calculatedMonths = ChronoUnit.MONTHS.between(startDate, endDate);
        if (calculatedMonths < 1) {
            throw new BusinessException("Commitment period must be at least 1 month");
        }
    }
    public void checkIfProductCombinationIsValid(String mainProduct, String[] additionalProducts) {
        // Ana ürün olmadan ek ürün taahhüt edilemez
        if (mainProduct == null && (additionalProducts != null && additionalProducts.length > 0)) {
            throw new BusinessException("Cannot commit to additional products without a main product");
        }
        
        // Kombinasyon sayısı kontrolü - maksimum 3 ek ürün olabilir
        if (additionalProducts != null && additionalProducts.length > 3) {
            throw new BusinessException("Maximum 3 additional products are allowed per commitment");
        }
        
        if (mainProduct != null && mainProduct.equals("INTERNET")) {
            if (additionalProducts != null) {
                boolean hasTv = false;
                boolean hasVoip = false;
                
                for (String product : additionalProducts) {
                    // TV ürünü kontrolü
                    if (product.equals("TV")) {
                        hasTv = true;
                    }
                    
                    // VoIP ürünü kontrolü
                    if (product.equals("VOIP")) {
                        hasVoip = true;
                    }
                    
                    // Fiber Internet olmadan Fiber TV alınamaz
                    if (product.equals("FIBER_TV") && !mainProduct.equals("FIBER_INTERNET")) {
                        throw new BusinessException("Fiber TV requires Fiber Internet subscription");
                    }
                }
                
                // Triple Play (Internet + TV + VoIP) tespiti - özel indirim uygulanmalı
                if (hasTv && hasVoip) {
                    // Triple Play paketi için fiyatlandırma servisi bilgilendirilmeli
                    // Bu örnekte sadece not olarak kalıyor, gerçek uygulamada ilgili servise bilgi gönderilebilir
                    // throw new BusinessException("Triple Play discount must be applied");
                }
            }
        }
        
        // Ana ürün MOBILE ise yapılacak kontroller
        if (mainProduct != null && mainProduct.equals("MOBILE")) {
            if (additionalProducts != null) {
                int mobileLineCount = 0;
                
                for (String product : additionalProducts) {
                    // Ek mobil hatlar kontrolü
                    if (product.startsWith("MOBILE_LINE")) {
                        mobileLineCount++;
                    }
                    
                    // Mobil hat olmadan tablet internet alınamaz
                    if (product.equals("TABLET_INTERNET") && mobileLineCount == 0) {
                        throw new BusinessException("Tablet internet requires at least one mobile line");
                    }
                    
                    // İş hatları (Corporate) ve bireysel hatlar (Individual) karıştırılamaz
                    if (mainProduct.equals("MOBILE_INDIVIDUAL") && product.equals("MOBILE_CORPORATE")) {
                        throw new BusinessException("Cannot mix individual and corporate mobile lines");
                    }
                }
                
                // Aile paketi kontrolü - 3 veya daha fazla hat varsa otomatik aile paketi uygulanmalı
                if (mobileLineCount >= 3) {
                    // Bu örnekte sadece not olarak kalıyor, gerçek uygulamada ilgili servise bilgi gönderilebilir
                    // throw new BusinessException("Family package discount must be applied");
                }
            }
        }
        
        // Kurumsal ve bireysel ürün karışımı kontrolü
        boolean hasCorporateProduct = (mainProduct != null && mainProduct.contains("CORPORATE"));
        boolean hasIndividualProduct = (mainProduct != null && mainProduct.contains("INDIVIDUAL"));
        
        if (additionalProducts != null) {
            for (String product : additionalProducts) {
                if (product.contains("CORPORATE")) {
                    hasCorporateProduct = true;
                }
                if (product.contains("INDIVIDUAL")) {
                    hasIndividualProduct = true;
                }
            }
        }
        
        // Kurumsal ve bireysel ürünler aynı taahhütte birleştirilemez
        if (hasCorporateProduct && hasIndividualProduct) {
            throw new BusinessException("Cannot mix corporate and individual products in the same commitment");
        }
        
        // Emekli tarifesi kontrolü (örnek bir iş kuralı)
        boolean hasRetiredTariff = (mainProduct != null && mainProduct.equals("RETIRED_TARIFF"));
        
        if (hasRetiredTariff) {
            // Emekli tarifesi başka tarifelerle birleştirilemez
            if (additionalProducts != null && additionalProducts.length > 0) {
                for (String product : additionalProducts) {
                    if (!product.startsWith("RETIRED_")) {
                        throw new BusinessException("Retired tariff can only be combined with other retired products");
                    }
                }
            }
        }
    }
    public void checkDeviceCommitmentRules(UUID commitmentId, UUID deviceId, int installmentCount) {
        // Taahhüt süresi, cihaz taksit sayısından az olamaz
        if (installmentCount > 24) {
            throw new BusinessException("Maximum installment count is 24 months");
        }

    }
    
    /**
     * Fatura döngüsü tipi ile faturalama gününün tutarlı olup olmadığını kontrol eder.
     * Örneğin, aylık faturalama için gün 1-28 arasında olmalıdır,
     * yıllık faturalama için özel kontroller yapılmalıdır.
     * 
     * @param cycleType Faturalama döngüsü tipi (MONTHLY, QUARTERLY, YEARLY, vb.)
     * @param billingDay Faturalama günü
     * @throws BusinessException Tutarsızlık durumunda
     */
    public void checkIfCycleTypeAndBillingDayAreConsistent(String cycleType, int billingDay) {
        if (cycleType == null) {
            throw new BusinessException("Cycle type cannot be null");
        }
        
        switch (cycleType) {
            case "MONTHLY":
                // Aylık döngüde, faturalama günü 1-28 arasında olmalıdır
                // (Her ay 28 gün olduğundan, 29-31 günleri sorun çıkarabilir)
                if (billingDay < 1 || billingDay > 28) {
                    throw new BusinessException("For monthly billing cycle, billing day must be between 1 and 28");
                }
                break;
                
            case "QUARTERLY":
                // 3 aylık döngüde, faturalama günü 1-28 arasında olmalıdır
                if (billingDay < 1 || billingDay > 28) {
                    throw new BusinessException("For quarterly billing cycle, billing day must be between 1 and 28");
                }
                break;
                
            case "BIANNUAL":
                // 6 aylık döngüde, faturalama günü 1-28 arasında olmalıdır
                if (billingDay < 1 || billingDay > 28) {
                    throw new BusinessException("For biannual billing cycle, billing day must be between 1 and 28");
                }
                break;
                
            case "YEARLY":
                // Yıllık döngüde, faturalama günü 1-28 arasında olmalıdır
                if (billingDay < 1 || billingDay > 28) {
                    throw new BusinessException("For yearly billing cycle, billing day must be between 1 and 28");
                }
                break;
                
            case "WEEKLY":
                // Haftalık döngüde, gün 1-7 arasında olmalıdır (haftanın günleri)
                if (billingDay < 1 || billingDay > 7) {
                    throw new BusinessException("For weekly billing cycle, billing day must be between 1 and 7");
                }
                break;
                
            case "DAILY":
                // Günlük faturalama için billingDay her zaman 1 olmalıdır
                if (billingDay != 1) {
                    throw new BusinessException("For daily billing cycle, billing day must be 1");
                }
                break;
                
            default:
                throw new BusinessException("Unknown cycle type: " + cycleType);
        }
        
        // Ek kontroller
        // Örneğin, özel günler için kısıtlamalar eklenebilir
        // (Örn: Haftasonu faturalama yapılmaz vb.)
    }
    
    /**
     * Müşterinin mevcut durumuna göre yeni bir taahhüt alıp alamayacağını kontrol eder.
     * Örneğin, müşterinin aktif bir taahhüdü varsa veya ödenmemiş faturaları bulunuyorsa.
     * 
     * @param customerId Müşteri ID'si
     * @throws BusinessException Eğer müşteri yeni taahhüt alamıyorsa
     */
    public void checkIfCustomerCanMakeNewCommitment(UUID customerId) {
        if (commitmentRepository.existsByContractDetail_CustomerIdAndStatus(customerId, "ACTIVE")) {
            throw new BusinessException("Customer already has an active commitment");
        }
        
        // Diğer özel iş kuralları buraya eklenebilir
        // Örnek: Müşterinin ödenmemiş faturası var mı?
        // Örnek: Müşteri kara listede mi?
    }
    
    /**
     * Bir paketin başka bir pakete yükseltme (upgrade) yolunun geçerli olup olmadığını kontrol eder.
     * Bu metod, hangi paketlerden hangi paketlere yükseltme yapılabileceğini belirtir.
     * 
     * @param currentPackage Mevcut paket
     * @param targetPackage Hedef paket
     * @return Yükseltme yolu geçerli ise true, değilse false
     */
    public boolean isValidUpgradePath(String currentPackage, String targetPackage) {
        // Paket hiyerarşisini harita olarak tanımlayalım
        // Her paket için, hangi paketlere yükseltilebileceğini belirtelim
        java.util.Map<String, java.util.List<String>> upgradePathMap = new java.util.HashMap<>();
        
        // INTERNET paketleri için yükseltme yolları
        upgradePathMap.put("INTERNET_BASIC", java.util.Arrays.asList("INTERNET_STANDARD", "INTERNET_PREMIUM", "INTERNET_ULTIMATE"));
        upgradePathMap.put("INTERNET_STANDARD", java.util.Arrays.asList("INTERNET_PREMIUM", "INTERNET_ULTIMATE"));
        upgradePathMap.put("INTERNET_PREMIUM", java.util.Arrays.asList("INTERNET_ULTIMATE"));
        
        // MOBILE paketleri için yükseltme yolları
        upgradePathMap.put("MOBILE_BASIC", java.util.Arrays.asList("MOBILE_STANDARD", "MOBILE_PREMIUM", "MOBILE_UNLIMITED"));
        upgradePathMap.put("MOBILE_STANDARD", java.util.Arrays.asList("MOBILE_PREMIUM", "MOBILE_UNLIMITED"));
        upgradePathMap.put("MOBILE_PREMIUM", java.util.Arrays.asList("MOBILE_UNLIMITED"));
        
        // TV paketleri için yükseltme yolları
        upgradePathMap.put("TV_BASIC", java.util.Arrays.asList("TV_STANDARD", "TV_PREMIUM", "TV_ULTIMATE"));
        upgradePathMap.put("TV_STANDARD", java.util.Arrays.asList("TV_PREMIUM", "TV_ULTIMATE"));
        upgradePathMap.put("TV_PREMIUM", java.util.Arrays.asList("TV_ULTIMATE"));
        
        // Mevcut paket için yükseltme yollarını kontrol et
        if (upgradePathMap.containsKey(currentPackage)) {
            return upgradePathMap.get(currentPackage).contains(targetPackage);
        }
        
        // Tanımlanmamış paketler için varsayılan olarak false döndür
        return false;
    }
    
    /**
     * Bir paketin başka bir pakete düşürme (downgrade) yolunun geçerli olup olmadığını kontrol eder.
     * Bu metod, hangi paketlerden hangi paketlere düşürme yapılabileceğini belirtir.
     * 
     * @param currentPackage Mevcut paket
     * @param targetPackage Hedef paket
     * @return Düşürme yolu geçerli ise true, değilse false
     */
    public boolean isValidDowngradePath(String currentPackage, String targetPackage) {
        // Basit bir yaklaşım olarak, düşürme yolları yükseltme yollarının tersidir
        // Ancak bazı paketler arasında doğrudan düşürme mümkün olmayabilir
        
        // Paket hiyerarşisini harita olarak tanımlayalım
        // Her paket için, hangi paketlere düşürülebileceğini belirtelim
        java.util.Map<String, java.util.List<String>> downgradePathMap = new java.util.HashMap<>();
        
        // INTERNET paketleri için düşürme yolları
        downgradePathMap.put("INTERNET_ULTIMATE", java.util.Arrays.asList("INTERNET_PREMIUM", "INTERNET_STANDARD", "INTERNET_BASIC"));
        downgradePathMap.put("INTERNET_PREMIUM", java.util.Arrays.asList("INTERNET_STANDARD", "INTERNET_BASIC"));
        downgradePathMap.put("INTERNET_STANDARD", java.util.Arrays.asList("INTERNET_BASIC"));
        
        // MOBILE paketleri için düşürme yolları
        downgradePathMap.put("MOBILE_UNLIMITED", java.util.Arrays.asList("MOBILE_PREMIUM", "MOBILE_STANDARD", "MOBILE_BASIC"));
        downgradePathMap.put("MOBILE_PREMIUM", java.util.Arrays.asList("MOBILE_STANDARD", "MOBILE_BASIC"));
        downgradePathMap.put("MOBILE_STANDARD", java.util.Arrays.asList("MOBILE_BASIC"));
        
        // TV paketleri için düşürme yolları
        downgradePathMap.put("TV_ULTIMATE", java.util.Arrays.asList("TV_PREMIUM", "TV_STANDARD", "TV_BASIC"));
        downgradePathMap.put("TV_PREMIUM", java.util.Arrays.asList("TV_STANDARD", "TV_BASIC"));
        downgradePathMap.put("TV_STANDARD", java.util.Arrays.asList("TV_BASIC"));
        
        // Mevcut paket için düşürme yollarını kontrol et
        if (downgradePathMap.containsKey(currentPackage)) {
            return downgradePathMap.get(currentPackage).contains(targetPackage);
        }
        
        // Tanımlanmamış paketler için varsayılan olarak false döndür
        return false;
    }

}
