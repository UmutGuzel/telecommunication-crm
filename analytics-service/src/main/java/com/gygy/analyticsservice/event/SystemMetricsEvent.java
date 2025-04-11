package com.gygy.analyticsservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Event representing system metrics from different services.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemMetricsEvent {
    private String serviceName;
    private String instanceId;
    private String metricType;
    private Double metricValue;
    private Map<String, Object> additionalMetrics;
    private LocalDateTime timestamp;
}