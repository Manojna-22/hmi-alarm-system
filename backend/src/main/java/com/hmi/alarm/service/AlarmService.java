package com.hmi.alarm.service;

import com.hmi.alarm.dto.*;
import com.hmi.alarm.entity.Severity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AlarmService {

    AlarmResponseDTO createAlarm(AlarmRequestDTO request);

    AlarmResponseDTO getAlarmById(Long id);

    PagedResponseDTO<AlarmResponseDTO> getAllAlarms(Boolean active, Severity severity, Pageable pageable);

    List<AlarmResponseDTO> getActiveAlarms();

    AlarmResponseDTO acknowledgeAlarm(Long id, AcknowledgeRequestDTO request);

    AlarmResponseDTO clearAlarm(Long id);

    AlarmStatsDTO getStats();

    void deleteAlarm(Long id);
}
