package com.hmi.alarm.controller;

import com.hmi.alarm.dto.AlarmEventDTO;
import com.hmi.alarm.dto.ApiResponse;
import com.hmi.alarm.service.AlarmEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alarms/{alarmId}/events")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AlarmEventController {

    private final AlarmEventService alarmEventService;

    // GET /api/v1/alarms/{alarmId}/events
    @GetMapping
    public ResponseEntity<ApiResponse<List<AlarmEventDTO>>> getEvents(@PathVariable Long alarmId) {
        List<AlarmEventDTO> events = alarmEventService.getEventsForAlarm(alarmId);
        return ResponseEntity.ok(ApiResponse.success(events, "Events fetched"));
    }
}
