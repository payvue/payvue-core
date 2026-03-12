package com.payvue.generic.alarm;

public interface AlarmChannel {
    void send(Alarm alarm) throws SendFailureException;
    boolean supports(String channelKey);
}
