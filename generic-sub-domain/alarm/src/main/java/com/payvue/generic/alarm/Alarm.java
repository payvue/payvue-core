package com.payvue.generic.alarm;

public abstract class Alarm {

    private final AlarmId id;
    private final Recipient recipient;
    private final String title;
    private final String message;
    private final String description;
    private final AlarmContent content;

    protected Alarm(
            AlarmId id,
            Recipient recipient,
            String title,
            String message,
            String description,
            AlarmContent content
    ) {
        this.id = id;
        this.recipient = recipient;
        this.title = title;
        this.message = message;
        this.description = description;
        this.content = content;
    }

    public AlarmId getId() {
        return id;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

    public AlarmContent getContent() {
        return content;
    }

    public void markAsSent() {
    }

    public void markAsFailed(String reason) {
    }

    protected abstract String channelKey();
}
