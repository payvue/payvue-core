package com.payvue.generic.alarm;

import java.util.Optional;

public interface AlarmRepository {
    Alarm save(Alarm alarm);
    Optional<Alarm> findById(AlarmId alarmId);
}
