package com.payvue.generic.alarm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AlarmContextTest {

    @Test
    @DisplayName("AlarmContext는 등록된 예외 핸들러를 사용한다")
    void handleExceptionUsesRegisteredHandler() {
        TestExceptionHandler handler = new TestExceptionHandler();
        AlarmContext context = AlarmContext.builder()
                .router(new CapturingRouter())
                .repository(new InMemoryRepository())
                .exceptionHandler(TestException.class, handler)
                .build();

        Alarm alarm = new TestAlarm();
        context.handleException(alarm, new TestException("boom"));

        assertThat(handler.handledAlarm).isSameAs(alarm);
        assertThat(handler.handledException).isInstanceOf(TestException.class);
    }

    @Test
    @DisplayName("AlarmContext는 예외 핸들러가 없으면 IllegalStateException을 던진다")
    void handleExceptionThrowsWhenMissingHandler() {
        AlarmContext context = AlarmContext.builder()
                .router(new CapturingRouter())
                .repository(new InMemoryRepository())
                .build();

        assertThatThrownBy(() -> context.handleException(new TestAlarm(), new TestException("boom")))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("AlarmContext 빌더는 채널을 등록하면 라우터에도 등록한다")
    void builderRegistersChannelWithRouter() {
        CapturingRouter router = new CapturingRouter();
        AlarmChannel channel = new TestChannel();

        AlarmContext.builder()
                .router(router)
                .channel("test", channel)
                .repository(new InMemoryRepository())
                .build();

        assertThat(router.registered).containsExactly(channel);
    }

    @Test
    @DisplayName("AlarmContext는 채널 키로 채널을 조회한다")
    void channelLookupReturnsRegisteredChannel() {
        AlarmChannel channel = new TestChannel();
        AlarmContext context = AlarmContext.builder()
                .router(new CapturingRouter())
                .channel("alarm", channel)
                .repository(new InMemoryRepository())
                .build();

        assertThat(context.channel("alarm")).isSameAs(channel);
    }

    private static class TestAlarm extends Alarm {
        TestAlarm() {
            super(AlarmId.generate(), Recipient.ofUser("user"), "title", "message", "desc", () -> "op");
        }

        @Override
        protected String channelKey() {
            return "test";
        }
    }

    private static class TestChannel implements AlarmChannel {
        @Override
        public void send(Alarm alarm) {
        }

        @Override
        public boolean supports(String channelKey) {
            return false;
        }
    }

    private static class TestException extends Exception {
        TestException(String message) {
            super(message);
        }
    }

    private static class TestExceptionHandler implements AlarmExceptionHandler<TestException> {
        private Alarm handledAlarm;
        private Exception handledException;

        @Override
        public void handle(Alarm alarm, TestException exception, AlarmContext context) {
            this.handledAlarm = alarm;
            this.handledException = exception;
        }
    }

    private static class CapturingRouter implements ChannelRouter {
        private final List<AlarmChannel> registered = new ArrayList<>();

        @Override
        public void register(AlarmChannel channel) {
            registered.add(channel);
        }

        @Override
        public void route(Alarm alarm, AlarmContext context) {
        }
    }

    private static class InMemoryRepository implements AlarmRepository {
        @Override
        public Alarm save(Alarm alarm) {
            return alarm;
        }

        @Override
        public java.util.Optional<Alarm> findById(AlarmId alarmId) {
            return java.util.Optional.empty();
        }
    }
}
