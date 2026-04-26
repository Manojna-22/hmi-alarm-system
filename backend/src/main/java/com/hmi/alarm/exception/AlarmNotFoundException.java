package com.hmi.alarm.exception;

public class AlarmNotFoundException extends RuntimeException {

    public AlarmNotFoundException(Long id) {
        super("Alarm not found with ID: " + id);
    }

    public AlarmNotFoundException(String code) {
        super("Alarm not found with code: " + code);
    }
}
