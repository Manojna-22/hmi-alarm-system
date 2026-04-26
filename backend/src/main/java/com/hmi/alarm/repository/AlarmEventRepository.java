package com.hmi.alarm.repository;

import com.hmi.alarm.entity.AlarmEvent;
import com.hmi.alarm.entity.AlarmState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmEventRepository extends JpaRepository<AlarmEvent, Long> {

    List<AlarmEvent> findByAlarmIdOrderByTsDesc(Long alarmId);

    Page<AlarmEvent> findByAlarmId(Long alarmId, Pageable pageable);

    List<AlarmEvent> findByState(AlarmState state);
}
