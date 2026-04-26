package com.hmi.alarm.dto;

import com.hmi.alarm.entity.AlarmState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmEventDTO {

    private Long id;
    private Long alarmId;
    private LocalDateTime ts;
    private AlarmState state;
    private String remarks;
}
