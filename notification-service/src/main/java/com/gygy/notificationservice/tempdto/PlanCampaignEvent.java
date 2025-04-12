package com.gygy.notificationservice.tempdto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanCampaignEvent {
    private String campaignName;
    private float price;
    private String description;
}
