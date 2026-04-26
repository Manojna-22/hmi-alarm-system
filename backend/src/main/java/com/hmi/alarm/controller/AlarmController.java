package com.hmi.alarm.controller;

import com.hmi.alarm.dto.*;
import com.hmi.alarm.entity.Severity;
import com.hmi.alarm.service.AlarmService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alarms")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AlarmController {

    private final AlarmService alarmService;

    // POST /api/v1/alarms
    @PostMapping
    public ResponseEntity<ApiResponse<AlarmResponseDTO>> createAlarm(
            @Valid @RequestBody AlarmRequestDTO request) {
        AlarmResponseDTO response = alarmService.createAlarm(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Alarm created successfully"));
    }

    // GET /api/v1/alarms/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AlarmResponseDTO>> getAlarmById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(alarmService.getAlarmById(id), "Alarm fetched"));
    }

    // GET /api/v1/alarms?active=true&severity=HIGH&page=0&size=10&sort=createdAt,desc
    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponseDTO<AlarmResponseDTO>>> getAllAlarms(
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) Severity severity,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        PagedResponseDTO<AlarmResponseDTO> response = alarmService.getAllAlarms(active, severity, pageable);
        return ResponseEntity.ok(ApiResponse.success(response, "Alarms fetched"));
    }

    // GET /api/v1/alarms/active
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<AlarmResponseDTO>>> getActiveAlarms() {
        return ResponseEntity.ok(ApiResponse.success(alarmService.getActiveAlarms(), "Active alarms fetched"));
    }

    // GET /api/v1/alarms/stats
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<AlarmStatsDTO>> getStats() {
        return ResponseEntity.ok(ApiResponse.success(alarmService.getStats(), "Stats fetched"));
    }

    // PATCH /api/v1/alarms/{id}/acknowledge
    @PatchMapping("/{id}/acknowledge")
    public ResponseEntity<ApiResponse<AlarmResponseDTO>> acknowledgeAlarm(
            @PathVariable Long id,
            @Valid @RequestBody AcknowledgeRequestDTO request) {
        AlarmResponseDTO response = alarmService.acknowledgeAlarm(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Alarm acknowledged"));
    }

    // PATCH /api/v1/alarms/{id}/clear
    @PatchMapping("/{id}/clear")
    public ResponseEntity<ApiResponse<AlarmResponseDTO>> clearAlarm(@PathVariable Long id) {
        AlarmResponseDTO response = alarmService.clearAlarm(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Alarm cleared"));
    }

    // DELETE /api/v1/alarms/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAlarm(@PathVariable Long id) {
        alarmService.deleteAlarm(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Alarm deleted"));
    }
}
