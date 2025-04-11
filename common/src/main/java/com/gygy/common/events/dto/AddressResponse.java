package com.gygy.common.events.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressResponse {
    private String street;
    private String district;
    private String apartmentNumber;
    private String city;
    private String country;
}