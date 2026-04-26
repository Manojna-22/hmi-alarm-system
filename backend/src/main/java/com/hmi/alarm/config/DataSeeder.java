package com.hmi.alarm.config;

import com.hmi.alarm.entity.*;
import com.hmi.alarm.repository.AlarmEventRepository;
import com.hmi.alarm.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    private final AlarmRepository alarmRepository;
    private final AlarmEventRepository alarmEventRepository;

    @Bean
    @Profile("!test")   // skip during tests
    public CommandLineRunner seedData() {
        return args -> {
            if (alarmRepository.count() > 0) {
                log.info("Database already seeded — skipping.");
                return;
            }

            log.info("Seeding sample alarms...");

            List<Alarm> alarms = List.of(
                    Alarm.builder().code("ALM-001").message("High temperature on boiler unit 3").severity(Severity.CRITICAL).active(true).acknowledged(false).build(),
                    Alarm.builder().code("ALM-002").message("Pressure sensor reading out of range").severity(Severity.HIGH).active(true).acknowledged(false).build(),
                    Alarm.builder().code("ALM-003").message("Fan speed below threshold on HVAC-2").severity(Severity.MEDIUM).active(true).acknowledged(true).acknowledgedBy("operator1").build(),
                    Alarm.builder().code("ALM-004").message("Low coolant level detected").severity(Severity.LOW).active(false).acknowledged(true).acknowledgedBy("operator2").build(),
                    Alarm.builder().code("ALM-005").message("Emergency stop triggered on conveyor").severity(Severity.CRITICAL).active(true).acknowledged(false).build()
            );

            List<Alarm> saved = alarmRepository.saveAll(alarms);

            // Seed events for each alarm
            for (Alarm alarm : saved) {
                AlarmEvent raised = AlarmEvent.builder()
                        .alarm(alarm)
                        .state(AlarmState.RAISED)
                        .remarks("System generated alarm")
                        .build();
                alarmEventRepository.save(raised);

                if (alarm.getAcknowledged()) {
                    AlarmEvent ack = AlarmEvent.builder()
                            .alarm(alarm)
                            .state(AlarmState.ACKNOWLEDGED)
                            .remarks("Acknowledged by " + alarm.getAcknowledgedBy())
                            .build();
                    alarmEventRepository.save(ack);
                }

                if (!alarm.getActive()) {
                    AlarmEvent cleared = AlarmEvent.builder()
                            .alarm(alarm)
                            .state(AlarmState.CLEARED)
                            .remarks("Alarm resolved and cleared")
                            .build();
                    alarmEventRepository.save(cleared);
                }
            }

            log.info("Seeded {} alarms.", saved.size());
        };
    }
}
