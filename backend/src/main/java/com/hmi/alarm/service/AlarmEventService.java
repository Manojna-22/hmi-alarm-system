package com.hmi.alarm.service;

import com.hmi.alarm.dto.AlarmEventDTO;

import java.util.List;

public interface AlarmEventService {

    List<AlarmEventDTO> getEventsForAlarm(Long alarmId);
}
