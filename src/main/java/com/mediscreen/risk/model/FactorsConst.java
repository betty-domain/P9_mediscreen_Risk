package com.mediscreen.risk.model;

import java.util.ArrayList;
import java.util.List;

public class FactorsConst {
    public static final String HEMOGLOBINE  = "Hémoglobine A1C";
    public static final String MICROALBUMINE = "Microalbumine";
    public static final String TAILLE = "Taille";
    public static final String POIDS="Poids";
    public static final String FUMEUR = "Fumeur";
    public static final String ANORMAL = "Anormal";
    public static final String CHOLESTEROL="Cholestérol";
    public static final String VERTIGE="Vertige";
    public static final String RECHUTE = "Rechute";
    public static final String REACTION="Réaction";
    public static final String ANTICORPS="Anticorps";

    public static List<String> getFactorsList()
    {
        List<String> factorsList = new ArrayList<>();
        factorsList.add(HEMOGLOBINE);
        factorsList.add(MICROALBUMINE);
        factorsList.add(TAILLE);
        factorsList.add(POIDS);
        factorsList.add(FUMEUR);
        factorsList.add(ANORMAL);
        factorsList.add(CHOLESTEROL);
        factorsList.add(VERTIGE);
        factorsList.add(RECHUTE);
        factorsList.add(REACTION);
        factorsList.add(ANTICORPS);

        return factorsList;
    }
}
