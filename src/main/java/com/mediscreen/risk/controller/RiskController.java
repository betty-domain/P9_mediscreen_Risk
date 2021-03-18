package com.mediscreen.risk.controller;

import com.mediscreen.risk.services.CalculateRiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RiskController {

    @Autowired
    private CalculateRiskService riskService;

    @GetMapping("/assess/id")
    public String getRiskForPatient(@RequestParam Integer patId)
    {
        return riskService.getRisk(patId).toString();
    }

    @GetMapping("/assess/familyName")
    public String getRiskForPatient(@RequestParam String familyName)
    {
        return riskService.getRisk(familyName).toString();
    }
}
