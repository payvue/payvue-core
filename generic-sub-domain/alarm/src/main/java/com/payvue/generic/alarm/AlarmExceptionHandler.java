package com.payvue.generic.alarm;

interface AlarmExceptionHandler<E extends Exception> {
    void handle(Alarm alarm, E exception, AlarmContext context);
}
