package com.hmi.alarm.service.impl;

import com.hmi.alarm.dto.*;
import com.hmi.alarm.entity.*;
import com.hmi.alarm.exception.AlarmNotFoundException;
import com.hmi.alarm.exception.DuplicateAlarmCodeException;
import com.hmi.alarm.mapper.AlarmMapper;
import com.hmi.alarm.repository.AlarmEventRepository;
import com.hmi.alarm.repository.AlarmRepository;
import com.hmi.alarm.service.AlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AlarmServiceImpl implements AlarmService {

    private final AlarmRepository alarmRepository;
    private final AlarmEventRepository alarmEventRepository;
    private final AlarmMapper alarmMapper;

    @Override
    public AlarmResponseDTO createAlarm(AlarmRequestDTO request) {
        log.info("Creating alarm with code: {}", request.getCode());

        if (alarmRepository.existsByCode(request.getCode())) {
            throw new DuplicateAlarmCodeException(request.getCode());
        }

        Alarm alarm = Alarm.builder()
                .code(request.getCode())
                .message(request.getMessage())
                .severity(request.getSeverity())
                .active(true)
                .acknowledged(false)
                .build();

        Alarm saved = alarmRepository.save(alarm);

        // Record RAISED event
        AlarmEvent event = AlarmEvent.builder()
                .alarm(saved)
                .state(AlarmState.RAISED)
                .remarks("Alarm raised")
                .build();
        alarmEventRepository.save(event);

        log.info("Alarm created with ID: {}", saved.getId());
        return alarmMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AlarmResponseDTO getAlarmById(Long id) {
        Alarm alarm = alarmRepository.findById(id)
                .orElseThrow(() -> new AlarmNotFoundException(id));
        return alarmMapper.toResponseDTO(alarm);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDTO<AlarmResponseDTO> getAllAlarms(Boolean active, Severity severity, Pageable pageable) {
        Page<Alarm> page = alarmRepository.findWithFilters(active, severity, pageable);
        List<AlarmResponseDTO> content = alarmMapper.toResponseDTOList(page.getContent());
        return PagedResponseDTO.<AlarmResponseDTO>builder()
                .content(content)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlarmResponseDTO> getActiveAlarms() {
        return alarmMapper.toResponseDTOList(alarmRepository.findByActiveTrue());
    }

    @Override
    public AlarmResponseDTO acknowledgeAlarm(Long id, AcknowledgeRequestDTO request) {
        log.info("Acknowledging alarm ID: {} by: {}", id, request.getAcknowledgedBy());

        Alarm alarm = alarmRepository.findById(id)
                .orElseThrow(() -> new AlarmNotFoundException(id));

        if (!alarm.getActive()) {
            throw new IllegalStateException("Cannot acknowledge a cleared alarm.");
        }

        if (alarm.getAcknowledged()) {
            throw new IllegalStateException("Alarm is already acknowledged.");
        }

        alarm.setAcknowledged(true);
        alarm.setAcknowledgedBy(request.getAcknowledgedBy());
        alarm.setAcknowledgedAt(LocalDateTime.now());

        Alarm saved = alarmRepository.save(alarm);

        AlarmEvent event = AlarmEvent.builder()
                .alarm(saved)
                .state(AlarmState.ACKNOWLEDGED)
                .remarks(request.getRemarks() != null ? request.getRemarks() : "Acknowledged by " + request.getAcknowledgedBy())
                .build();
        alarmEventRepository.save(event);

        return alarmMapper.toResponseDTO(saved);
    }

    @Override
    public AlarmResponseDTO clearAlarm(Long id) {
        log.info("Clearing alarm ID: {}", id);

        Alarm alarm = alarmRepository.findById(id)
                .orElseThrow(() -> new AlarmNotFoundException(id));

        if (!alarm.getActive()) {
            throw new IllegalStateException("Alarm is already cleared.");
        }

        alarm.setActive(false);

        Alarm saved = alarmRepository.save(alarm);

        AlarmEvent event = AlarmEvent.builder()
                .alarm(saved)
                .state(AlarmState.CLEARED)
                .remarks("Alarm cleared")
                .build();
        alarmEventRepository.save(event);

        return alarmMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AlarmStatsDTO getStats() {
        long totalActive = alarmRepository.countByActiveTrue();
        long totalCleared = alarmRepository.countByActiveFalse();
        long totalAcknowledged = alarmRepository.countByAcknowledgedTrue();

        List<Object[]> severityData = alarmRepository.countActiveBySeverity();
        Map<String, Long> activeBySeverity = new HashMap<>();
        for (Object[] row : severityData) {
            activeBySeverity.put(((Severity) row[0]).name(), (Long) row[1]);
        }

        return AlarmStatsDTO.builder()
                .totalActive(totalActive)
                .totalCleared(totalCleared)
                .totalAcknowledged(totalAcknowledged)
                .activeBySeverity(activeBySeverity)
                .build();
    }

    @Override
    public void deleteAlarm(Long id) {
        log.info("Deleting alarm ID: {}", id);
        Alarm alarm = alarmRepository.findById(id)
                .orElseThrow(() -> new AlarmNotFoundException(id));
        alarmRepository.delete(alarm);
    }
}
