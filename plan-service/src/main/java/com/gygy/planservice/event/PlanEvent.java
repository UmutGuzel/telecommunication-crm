package com.gygy.planservice.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanEvent {
    private String campaignName;
    private String description;
    private String email;
    private float price;
}
