package com.gygy.notificationservice.tempdto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IndividualCustomerUpdatedEvent {
    private UUID id;
    private String email;
    private String phoneNumber;
    private String identityNumber;
    private String name;
    private String surname;
    private String fatherName;
    private String motherName;
    private String gender;
    private LocalDate birthDate;

    private List<String> changedFields;  // 🔥 Yeni alan: değişen field'lar
}
