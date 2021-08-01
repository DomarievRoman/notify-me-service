package com.domariev.emailsender.model.enums;

public enum NewsLocation {
    GB("en"),
    US("en"),
    UA("uk"),
    RU("ru"),
    FR("fr"),
    DE("de");

    private final String code;

    NewsLocation(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
