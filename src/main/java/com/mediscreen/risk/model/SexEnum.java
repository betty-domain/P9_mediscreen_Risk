package com.mediscreen.risk.model;

public enum SexEnum {
    WOMEN("F"),
    MEN("M"),
    UNDEFINED("");

    private String text;

    SexEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static SexEnum fromString(String text) {
        for (SexEnum val : SexEnum.values()) {
            if (val.text.equalsIgnoreCase(text)) {
                return val;
            }
        }
        return UNDEFINED;
    }
}
