package com.hmi.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmStatsDTO {

    private long totalActive;
    private long totalCleared;
    private long totalAcknowledged;
    private Map<String, Long> activeBySeverity;
}
