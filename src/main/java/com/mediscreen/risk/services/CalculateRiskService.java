package com.mediscreen.risk.services;

import com.mediscreen.risk.exceptions.ObjectNotFoundException;
import com.mediscreen.risk.exceptions.RiskNotDefinedException;
import com.mediscreen.risk.model.FactorsConst;
import com.mediscreen.risk.model.Note;
import com.mediscreen.risk.model.Patient;
import com.mediscreen.risk.model.Risk;
import com.mediscreen.risk.model.RiskEnum;
import com.mediscreen.risk.model.SexEnum;
import com.mediscreen.risk.utils.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class CalculateRiskService {

    private static final Logger logger = LogManager.getLogger(CalculateRiskService.class);

    @Autowired
    private DateUtils dateUtils;

    @Autowired
    private PatientsProxy patientsProxy;

    @Autowired
    private NoteProxy noteProxy;

    private static final String RISK_NOT_DEFINED = "Risk is not defined";

    /**
     * get Risk for patient with patientId
     * @param patientId patientId
     * @return Risk if defined
     * @throws RiskNotDefinedException risk is not defined for patient criterias
     * @throws ObjectNotFoundException patient was not found with patientId
     */
    public Risk getRisk(int patientId) throws RiskNotDefinedException, ObjectNotFoundException {
        try {
            Patient patient = patientsProxy.getPatient(patientId);
            return evaluateRisk(patient);
        } catch (ObjectNotFoundException notFoundException) {
            logger.debug("Patient Not Found, can not calculate risk");
            throw notFoundException;
        }
    }

    /**
     * get Risk for Patient with familyName
     * @param patientFamilyName patietn FamilyName
     * @return Risk if defined
     * @throws RiskNotDefinedException risk is not defined for patient criterias
     * @throws ObjectNotFoundException patient was not found with patientId
     */
    public Risk getRisk(String patientFamilyName) throws RiskNotDefinedException, ObjectNotFoundException {
        try {
            Patient patient = patientsProxy.getPatient(patientFamilyName);
            return  evaluateRisk(patient);
        } catch (ObjectNotFoundException notFoundException) {
            logger.debug("Patient Not Found, can not calculate risk");
            throw notFoundException;
        }

    }

    /**
     * Evaluate Risk for a given Patient
     * @param patient patient
     * @return Risk if defined
     * @throws RiskNotDefinedException risk was not defined for patients's criteria
     */
    private Risk evaluateRisk(Patient patient) throws RiskNotDefinedException {
        int age = dateUtils.getAge(patient.getBirthDate());

        SexEnum sex = SexEnum.fromString(patient.getSex());

        List<Note> notesForPatient = noteProxy.getNotesForPatient(patient.getId());
        long factorsNumber = countMedicalFactors(notesForPatient);

        RiskEnum riskEnum = null;

        if (factorsNumber == 0 || factorsNumber == 1) {
            riskEnum = RiskEnum.NONE;
        } else {
            if (age > 30) {
                riskEnum = getRiskForOlderThan30(factorsNumber);
            } else {
                //TODO : on classifie les trentenaires avec les<30 ans, à valider
                riskEnum = getRiskForYoungerThan30(factorsNumber, sex);
            }
        }

        return new Risk(patient, dateUtils.getAge(patient.getBirthDate()), riskEnum);
    }

    /**
     * get Risk for patient older than 30 years
     * @param factorsNumber number of factors found relative to patient
     * @return RiskEnum if defined
     * @throws RiskNotDefinedException risk was not defined for patients's criteria
     */
    private RiskEnum getRiskForOlderThan30(long factorsNumber) throws RiskNotDefinedException {
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

        throw new RiskNotDefinedException(RISK_NOT_DEFINED);
    }

    /**
     * get Risk for patient younger than 30 years
     * @param factorsNumber number of factors found relative to patient
     * @return RiskEnum if defined
     * @throws RiskNotDefinedException risk was not defined for patients's criteria
     */
    private RiskEnum getRiskForYoungerThan30(long factorsNumber, SexEnum sex) throws RiskNotDefinedException {
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
                throw new RiskNotDefinedException(RISK_NOT_DEFINED);
        }

        throw new RiskNotDefinedException(RISK_NOT_DEFINED);
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
