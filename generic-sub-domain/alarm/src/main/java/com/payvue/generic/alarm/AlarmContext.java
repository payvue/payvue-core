package com.payvue.generic.alarm;

import java.util.HashMap;
import java.util.Map;

final class AlarmContext {
    private final ChannelRouter router;
    private final AlarmRepository repository;
    private final Map<String, AlarmChannel> channels;
    private final Map<Class<? extends Exception>, AlarmExceptionHandler<? extends Exception>> exceptionHandlers;

    private AlarmContext(
            ChannelRouter router,
            AlarmRepository repository,
            Map<String, AlarmChannel> channels,
            Map<Class<? extends Exception>, AlarmExceptionHandler<? extends Exception>> exceptionHandlers
    ) {
        this.router = router;
        this.repository = repository;
        this.channels = channels;
        this.exceptionHandlers = exceptionHandlers;
    }

    public static Builder builder() {
        return new Builder();
    }

    public AlarmRepository repository() {
        return repository;
    }

    public AlarmChannel channel(String channelKey) {
        return channels.get(channelKey);
    }

    public ChannelRouter router() {
        return router;
    }

    public void handleException(Alarm alarm, Exception exception) {
        AlarmExceptionHandler<Exception> handler = findExceptionHandler(exception);
        if (handler == null) {
            throw new IllegalStateException("no exception handler for: " + exception.getClass().getSimpleName());
        }
        handler.handle(alarm, exception, this);
    }

    @SuppressWarnings("unchecked")
    private AlarmExceptionHandler<Exception> findExceptionHandler(Exception exception) {
        for (Map.Entry<Class<? extends Exception>, AlarmExceptionHandler<? extends Exception>> entry
                : exceptionHandlers.entrySet()) {
            if (entry.getKey().isInstance(exception)) {
                return (AlarmExceptionHandler<Exception>) entry.getValue();
            }
        }
        return null;
    }

    static final class Builder {
        private ChannelRouter router;
        private AlarmRepository repository;
        private final Map<String, AlarmChannel> channels = new HashMap<>();
        private final Map<Class<? extends Exception>, AlarmExceptionHandler<? extends Exception>> exceptionHandlers =
                new HashMap<>();

        Builder router(ChannelRouter router) {
            this.router = router;
            return this;
        }

        Builder channel(String channelKey, AlarmChannel channel) {
            this.channels.put(channelKey, channel);
            if (router != null) {
                router.register(channel);
            }
            return this;
        }

        Builder repository(AlarmRepository repository) {
            this.repository = repository;
            return this;
        }

        <E extends Exception> Builder exceptionHandler(
                Class<E> exceptionType,
                AlarmExceptionHandler<E> handler
        ) {
            this.exceptionHandlers.put(exceptionType, handler);
            return this;
        }

        AlarmContext build() {
            return new AlarmContext(router, repository, channels, exceptionHandlers);
        }
    }
}
