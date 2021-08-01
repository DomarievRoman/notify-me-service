package com.domariev.emailsender.model.enums;

public enum MailingPriority {
    RED(15),
    YELLOW(10),
    GREEN(5);

    private final int value;

    MailingPriority(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
