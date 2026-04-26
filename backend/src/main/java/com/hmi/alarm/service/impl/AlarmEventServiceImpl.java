package com.hmi.alarm.service.impl;

import com.hmi.alarm.dto.AlarmEventDTO;
import com.hmi.alarm.exception.AlarmNotFoundException;
import com.hmi.alarm.mapper.AlarmMapper;
import com.hmi.alarm.repository.AlarmEventRepository;
import com.hmi.alarm.repository.AlarmRepository;
import com.hmi.alarm.service.AlarmEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AlarmEventServiceImpl implements AlarmEventService {

    private final AlarmEventRepository alarmEventRepository;
    private final AlarmRepository alarmRepository;
    private final AlarmMapper alarmMapper;

    @Override
    public List<AlarmEventDTO> getEventsForAlarm(Long alarmId) {
        if (!alarmRepository.existsById(alarmId)) {
            throw new AlarmNotFoundException(alarmId);
        }
        return alarmMapper.toEventDTOList(
                alarmEventRepository.findByAlarmIdOrderByTsDesc(alarmId)
        );
    }
}
