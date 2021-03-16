package com.mediscreen.risk.model;

public enum RiskEnum {
    NONE("Aucun Risque"),
    BORDERLINE("Risque limité"),
    DANGER("Danger"),
    EARLY_ONSET("Apparition précoce");

    private String text;

    RiskEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static RiskEnum fromString(String text) {
        for (RiskEnum val : RiskEnum.values()) {
            if (val.text.equalsIgnoreCase(text)) {
                return val;
            }
        }
        return null;
    }
}
