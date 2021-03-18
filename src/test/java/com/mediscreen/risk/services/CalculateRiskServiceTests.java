package com.mediscreen.risk.services;

import com.mediscreen.risk.model.Note;
import com.mediscreen.risk.model.Patient;
import com.mediscreen.risk.model.Risk;
import com.mediscreen.risk.model.RiskEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CalculateRiskServiceTests {


    @MockBean
    private NoteProxy noteProxyMock;

    @MockBean
    private PatientsProxy patientsProxyMock;

    @Autowired
    private CalculateRiskService calculateRiskService;

    @Test
    void defineRiskForTestNone()
    {
        Patient patient = new Patient(1,"Test","TestNone","F", LocalDate.of(1966,12,31),"1 Brookside St","100-222-3333");
        List<Note> noteList = new ArrayList<>();
        Note note = new Note(patient.getId(),"Le patient déclare qu'il `` se sent bien '' Poids au niveau ou en dessous du niveau recommandé", LocalDate.now());
        noteList.add(note);

        when(patientsProxyMock.getPatient(patient.getId())).thenReturn(patient);
        when(noteProxyMock.getNotesForPatient(patient.getId())).thenReturn(noteList);


        Risk risk = calculateRiskService.getRisk(patient.getId());
        assertThat(risk).isNotNull();
        assertThat(risk.getRiskEnum()).isEqualTo(RiskEnum.NONE);
    }

    @Test
    void defineRiskForTestNone_ByFamilyName()
    {
        Patient patient = new Patient(1,"Test","TestNone","F", LocalDate.of(1966,12,31),"1 Brookside St","100-222-3333");
        List<Note> noteList = new ArrayList<>();
        Note note = new Note(patient.getId(),"Le patient déclare qu'il `` se sent bien '' Poids au niveau ou en dessous du niveau recommandé", LocalDate.now());
        noteList.add(note);

        when(patientsProxyMock.getPatient(patient.getLastname())).thenReturn(patient);
        when(noteProxyMock.getNotesForPatient(patient.getId())).thenReturn(noteList);


        Risk risk = calculateRiskService.getRisk(patient.getLastname());
        assertThat(risk).isNotNull();
        assertThat(risk.getRiskEnum()).isEqualTo(RiskEnum.NONE);
    }

    @Test
    void defineRiskForTestBorderLine()
    {
        Patient patient = new Patient(2,"Test","TestBorderline","M", LocalDate.of(1945,06,24),"2 High St","200-333-4444");
        List<Note> noteList = new ArrayList<>();

        noteList.add(new Note(patient.getId(),"Le patient déclare ressentir beaucoup de stress au travail Le patient se plaint également que son audition semble anormale ces derniers temps", LocalDate.now()));
        noteList.add(new Note(patient.getId(),"Le patient déclare avoir eu une réaction à un médicament au cours des 3 derniers mois Le patient se plaint également que son audition continue d'être problématique", LocalDate.now()));

        when(patientsProxyMock.getPatient(patient.getId())).thenReturn(patient);
        when(noteProxyMock.getNotesForPatient(patient.getId())).thenReturn(noteList);

        Risk risk = calculateRiskService.getRisk(patient.getId());
        assertThat(risk).isNotNull();
        assertThat(risk.getRiskEnum()).isEqualTo(RiskEnum.BORDERLINE);

    }

    @Test
    void defineRiskForTestBorderLine_ByFamilyName()
    {
        Patient patient = new Patient(2,"Test","TestBorderline","M", LocalDate.of(1945,06,24),"2 High St","200-333-4444");
        List<Note> noteList = new ArrayList<>();

        noteList.add(new Note(patient.getId(),"Le patient déclare ressentir beaucoup de stress au travail Le patient se plaint également que son audition semble anormale ces derniers temps", LocalDate.now()));
        noteList.add(new Note(patient.getId(),"Le patient déclare avoir eu une réaction à un médicament au cours des 3 derniers mois Le patient se plaint également que son audition continue d'être problématique", LocalDate.now()));

        when(patientsProxyMock.getPatient(patient.getLastname())).thenReturn(patient);
        when(noteProxyMock.getNotesForPatient(patient.getId())).thenReturn(noteList);

        Risk risk = calculateRiskService.getRisk(patient.getLastname());
        assertThat(risk).isNotNull();
        assertThat(risk.getRiskEnum()).isEqualTo(RiskEnum.BORDERLINE);

    }

    @Test
    void defineRiskForTestInDanger()
    {
        Patient patient = new Patient(3,"Test","TestInDanger","M", LocalDate.of(2004,6,18),"3 Club Road","300-444-5555");
        List<Note> noteList = new ArrayList<>();

        noteList.add(new Note(patient.getId(),"Le patient déclare être un fumeur à court terme", LocalDate.now()));
        noteList.add(new Note(patient.getId(),"Le patient déclare qu'il a arrêté au cours de l'année dernière Le patient se plaint également de crises respiratoires anormales Rapports de laboratoire Cholestérol LDL", LocalDate.now()));

        //noteList.add(new Note(patient.getId(),"", LocalDate.now()));

        when(patientsProxyMock.getPatient(patient.getId())).thenReturn(patient);
        when(noteProxyMock.getNotesForPatient(patient.getId())).thenReturn(noteList);

        Risk risk = calculateRiskService.getRisk(patient.getId());
        assertThat(risk).isNotNull();
        assertThat(risk.getRiskEnum()).isEqualTo(RiskEnum.DANGER);
    }

    @Test
    void defineRiskForTestInDanger_ByFamilyName()
    {
        Patient patient = new Patient(3,"Test","TestInDanger","M", LocalDate.of(2004,6,18),"3 Club Road","300-444-5555");
        List<Note> noteList = new ArrayList<>();

        noteList.add(new Note(patient.getId(),"Le patient déclare être un fumeur à court terme", LocalDate.now()));
        noteList.add(new Note(patient.getId(),"Le patient déclare qu'il a arrêté au cours de l'année dernière Le patient se plaint également de crises respiratoires anormales Rapports de laboratoire Cholestérol LDL", LocalDate.now()));

        when(patientsProxyMock.getPatient(patient.getLastname())).thenReturn(patient);
        when(noteProxyMock.getNotesForPatient(patient.getId())).thenReturn(noteList);

        Risk risk = calculateRiskService.getRisk(patient.getLastname());
        assertThat(risk).isNotNull();
        assertThat(risk.getRiskEnum()).isEqualTo(RiskEnum.DANGER);
    }


    @Test
    void defineRiskForTestEarlyOnset()
    {
        Patient patient = new Patient(4,"Test","TestEarlyOnset","F", LocalDate.of(2002,6,28),"4 Valley Dr","400-555-6666");

        List<Note> noteList = new ArrayList<>();

        noteList.add(new Note(patient.getId(),"Le patient déclare qu'il est devenu difficile de monter les escaliers Le patient se plaint également d'avoir un essoufflement Les résultats du laboratoire indiquent que les anticorps présentent une réaction élevée aux médicaments", LocalDate.now()));
        noteList.add(new Note(patient.getId(),"Le patient déclare avoir mal au dos lorsqu'il est assis pendant une longue période", LocalDate.now()));
        noteList.add(new Note(patient.getId(),"Le patient déclare qu'il est un fumeur à court terme d'hémoglobine A1C au-dessus du niveau recommandé", LocalDate.now()));
        noteList.add(new Note(patient.getId(),"Le patient déclare que la taille corporelle, le poids corporel, le cholestérol, les étourdissements et les réactions", LocalDate.now()));

        when(patientsProxyMock.getPatient(patient.getId())).thenReturn(patient);
        when(noteProxyMock.getNotesForPatient(patient.getId())).thenReturn(noteList);

        Risk risk = calculateRiskService.getRisk(patient.getId());
        assertThat(risk).isNotNull();
        assertThat(risk.getRiskEnum()).isEqualTo(RiskEnum.EARLY_ONSET);
    }

    @Test
    void defineRiskForTestEarlyOnset_ByFamilyName()
    {
        Patient patient = new Patient(4,"Test","TestEarlyOnset","F", LocalDate.of(2002,6,28),"4 Valley Dr","400-555-6666");

        List<Note> noteList = new ArrayList<>();

        noteList.add(new Note(patient.getId(),"Le patient déclare qu'il est devenu difficile de monter les escaliers Le patient se plaint également d'avoir un essoufflement Les résultats du laboratoire indiquent que les anticorps présentent une réaction élevée aux médicaments", LocalDate.now()));
        noteList.add(new Note(patient.getId(),"Le patient déclare avoir mal au dos lorsqu'il est assis pendant une longue période", LocalDate.now()));
        noteList.add(new Note(patient.getId(),"Le patient déclare qu'il est un fumeur à court terme d'hémoglobine A1C au-dessus du niveau recommandé", LocalDate.now()));
        noteList.add(new Note(patient.getId(),"Le patient déclare que la taille corporelle, le poids corporel, le cholestérol, les étourdissements et les réactions", LocalDate.now()));

        when(patientsProxyMock.getPatient(patient.getLastname())).thenReturn(patient);
        when(noteProxyMock.getNotesForPatient(patient.getId())).thenReturn(noteList);

        Risk risk = calculateRiskService.getRisk(patient.getLastname());
        assertThat(risk).isNotNull();
        assertThat(risk.getRiskEnum()).isEqualTo(RiskEnum.EARLY_ONSET);
    }
}
