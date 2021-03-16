package com.mediscreen.risk.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Risk {
    private Patient patient;
    private int age;
    private RiskEnum riskEnum;

    public Risk(Patient patient, int age, RiskEnum riskEnum)
    {
        this.patient=patient;
        this.age=age;
        this.riskEnum = riskEnum;
    }

    public String toString()
    {
        StringBuilder strBuilder = new StringBuilder("Patient :");
        strBuilder.append(patient.getFirstname());
        strBuilder.append(" ");
        strBuilder.append(patient.getLastname());
        strBuilder.append(" (age ");
        strBuilder.append(age);
        strBuilder.append(") diabetes assessment is:");
        strBuilder.append(riskEnum.getText());

        return strBuilder.toString();
    }
}
