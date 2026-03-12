package com.payvue.generic.alarm;

interface ChannelRouter {
    void register(AlarmChannel channel);

    void route(Alarm alarm, AlarmContext context) throws SendFailureException;
}
