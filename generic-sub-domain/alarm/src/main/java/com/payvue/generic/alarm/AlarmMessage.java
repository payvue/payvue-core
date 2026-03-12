package com.payvue.generic.alarm;

final class AlarmMessage {
    private final String title;
    private final String message;
    private final String description;

    private AlarmMessage(String title, String message, String description) {
        this.title = title;
        this.message = message;
        this.description = description;
    }

    static AlarmMessage of(String title, String message, String description) {
        return new AlarmMessage(title, message, description);
    }

    String title() {
        return title;
    }

    String message() {
        return message;
    }

    String description() {
        return description;
    }
}
