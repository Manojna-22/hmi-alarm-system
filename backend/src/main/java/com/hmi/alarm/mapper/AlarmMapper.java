package com.hmi.alarm.mapper;

import com.hmi.alarm.dto.AlarmEventDTO;
import com.hmi.alarm.dto.AlarmResponseDTO;
import com.hmi.alarm.entity.Alarm;
import com.hmi.alarm.entity.AlarmEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AlarmMapper {

    AlarmResponseDTO toResponseDTO(Alarm alarm);

    List<AlarmResponseDTO> toResponseDTOList(List<Alarm> alarms);

    @Mapping(source = "alarm.id", target = "alarmId")
    AlarmEventDTO toEventDTO(AlarmEvent alarmEvent);

    List<AlarmEventDTO> toEventDTOList(List<AlarmEvent> events);
}
