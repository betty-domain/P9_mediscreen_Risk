package com.mediscreen.risk.controller;

import com.mediscreen.risk.exceptions.ObjectNotFoundException;
import com.mediscreen.risk.model.Patient;
import com.mediscreen.risk.model.Risk;
import com.mediscreen.risk.model.RiskEnum;
import com.mediscreen.risk.services.CalculateRiskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RiskController.class)
public class RiskControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CalculateRiskService riskServiceMock;

    @Test
    void getRiskByPatId_StatusOk() throws Exception {
        Patient patient = new Patient(1, "firstname", "lastname", "M", LocalDate.of(2000, 1, 15), "address", "phone");
        Risk risk = new Risk(patient, 25, RiskEnum.DANGER);

        when(riskServiceMock.getRisk(patient.getId())).thenReturn(risk);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/assess/id").
                contentType(MediaType.APPLICATION_JSON)
                .param("patId", patient.getId().toString());

        mockMvc.perform(builder).
                andExpect(status().isOk());
    }

    @Test
    void getRiskByPatId_NotFound() throws Exception {
        Patient patient = new Patient(1, "firstname", "lastname", "M", LocalDate.of(2000, 1, 15), "address", "phone");
        Risk risk = new Risk(patient, 25, RiskEnum.DANGER);

        given(riskServiceMock.getRisk(patient.getId())).willThrow(new ObjectNotFoundException("Not Found Exception Message"));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/assess/id").
                contentType(MediaType.APPLICATION_JSON)
                .param("patId", patient.getId().toString());

        mockMvc.perform(builder).
                andExpect(status().isNotFound());
    }

    @Test
    void getRiskByFamilyName_StatusOk() throws Exception {
        Patient patient = new Patient(1, "firstname", "lastname", "M", LocalDate.of(2000, 1, 15), "address", "phone");
        Risk risk = new Risk(patient, 25, RiskEnum.DANGER);

        when(riskServiceMock.getRisk(patient.getLastname())).thenReturn(risk);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/assess/familyName").
                contentType(MediaType.APPLICATION_JSON)
                .param("familyName", patient.getLastname());

        mockMvc.perform(builder).
                andExpect(status().isOk());
    }

    @Test
    void getRiskByFamilyName_NotFound() throws Exception {
        Patient patient = new Patient(1, "firstname", "lastname", "M", LocalDate.of(2000, 1, 15), "address", "phone");
        Risk risk = new Risk(patient, 25, RiskEnum.DANGER);

        given(riskServiceMock.getRisk(patient.getLastname())).willThrow(new ObjectNotFoundException("Not Found Exception Message"));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/assess/familyName").
                contentType(MediaType.APPLICATION_JSON)
                .param("familyName", patient.getLastname());

        mockMvc.perform(builder).
                andExpect(status().isNotFound());
    }
}
