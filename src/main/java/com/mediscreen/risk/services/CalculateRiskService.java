package com.mediscreen.risk.services;

import com.mediscreen.risk.model.FactorsConst;
import com.mediscreen.risk.model.Note;
import com.mediscreen.risk.model.Patient;
import com.mediscreen.risk.model.Risk;
import com.mediscreen.risk.model.RiskEnum;
import com.mediscreen.risk.model.SexEnum;
import com.mediscreen.risk.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class CalculateRiskService {

    @Autowired
    private DateUtils dateUtils;

    @Autowired
    private PatientsProxy patientsProxy;

    @Autowired
    private NoteProxy noteProxy;

    private static final String RISK_NOT_DEFINED = "Risk is not defined";

    //TODO à supprimer, fonction inutile
    private SexEnum getSex(String sex) {
        return SexEnum.fromString(sex);
    }

    public Risk getRisk(int patientId) {
        try {
            Patient patient = patientsProxy.getPatient(patientId);
            RiskEnum riskEnum = evaluateRisk(patient);

            return new Risk(patient,dateUtils.getAge(patient.getBirthDate()),riskEnum);

        } catch (Exception exception) {
            //TODO trapper les notfoundException pour les feignClient

            //TODO trapper les exceptions sur RiskNotDefined si besoin
            return null;
        }
    }

    private RiskEnum evaluateRisk(Patient patient) throws Exception{
        int age = dateUtils.getAge(patient.getBirthDate());

        SexEnum sex = SexEnum.fromString(patient.getSex());

        List<Note> notesForPatient = noteProxy.getNotesForPatient(patient.getId());
        long factorsNumber = countMedicalFactors(notesForPatient);

        if (factorsNumber == 0 || factorsNumber == 1) {
            return RiskEnum.NONE;
        } else {
            if (age > 30) {
                return getRiskForOlderThan30(factorsNumber);
            } else {
                return getRiskForYoungerThan30(factorsNumber, sex);
            }
        }

    }

    private RiskEnum getRiskForOlderThan30(long factorsNumber) throws Exception {
        if (factorsNumber == 2) {
            return RiskEnum.BORDERLINE;
        } else {
            if (factorsNumber == 6) {
                return RiskEnum.DANGER;
            } else {
                if (factorsNumber >= 8) {
                    return RiskEnum.EARLY_ONSET;
                } else {
                    //TODO : to define
                }
            }
        }

        throw new Exception(RISK_NOT_DEFINED);
    }

    private RiskEnum getRiskForYoungerThan30(long factorsNumber, SexEnum sex) throws Exception {
        switch (sex) {
            case MEN:
                if (factorsNumber == 3) {
                    return RiskEnum.DANGER;
                } else {
                    if (factorsNumber >= 5) {
                        return RiskEnum.EARLY_ONSET;
                    } else {
                        //TODO to define
                    }
                }
                break;
            case WOMEN:
                if (factorsNumber == 4) {
                    return RiskEnum.DANGER;
                } else {
                    if (factorsNumber >= 7) {
                        return RiskEnum.EARLY_ONSET;
                    } else {
                        //TODO to define
                    }
                }
                break;
            default:
                throw new Exception(RISK_NOT_DEFINED);
        }

        throw new Exception(RISK_NOT_DEFINED);
    }

    /**
     * Count medical factors in note list
     *
     * @param noteList note list
     * @return number of medical factors
     */
    private long countMedicalFactors(List<Note> noteList) {
        List<String> factorsList = FactorsConst.getFactorsList();

        long factorCounter = 0;

        for (String factorString : factorsList) {
            factorCounter = factorCounter + noteList.stream().filter(note -> note.getNote().toUpperCase(Locale.ROOT).contains(factorString.toUpperCase(Locale.ROOT))).count();
        }

        return factorCounter;
    }

}
