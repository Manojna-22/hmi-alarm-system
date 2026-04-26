package com.hmi.alarm.repository;

import com.hmi.alarm.entity.Alarm;
import com.hmi.alarm.entity.Severity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    // Find all active alarms
    List<Alarm> findByActiveTrue();

    // Find by severity
    List<Alarm> findBySeverity(Severity severity);

    // Find active alarms by severity
    List<Alarm> findByActiveTrueAndSeverity(Severity severity);

    // Count active vs cleared
    long countByActiveTrue();

    long countByActiveFalse();

    long countByAcknowledgedTrue();

    // Paginated alarms filtered by active flag and optional severity
    @Query("SELECT a FROM Alarm a WHERE " +
           "(:active IS NULL OR a.active = :active) AND " +
           "(:severity IS NULL OR a.severity = :severity)")
    Page<Alarm> findWithFilters(
            @Param("active") Boolean active,
            @Param("severity") Severity severity,
            Pageable pageable
    );

    // Summary stats
    @Query("SELECT a.severity, COUNT(a) FROM Alarm a WHERE a.active = true GROUP BY a.severity")
    List<Object[]> countActiveBySeverity();

    boolean existsByCode(String code);
}
