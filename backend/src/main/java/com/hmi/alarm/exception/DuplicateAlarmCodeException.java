package com.hmi.alarm.exception;

public class DuplicateAlarmCodeException extends RuntimeException {

    public DuplicateAlarmCodeException(String code) {
        super("Alarm with code '" + code + "' already exists");
    }
}
