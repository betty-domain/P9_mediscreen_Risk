package com.mediscreen.risk.controller;

import com.mediscreen.risk.exceptions.BadRequestException;
import com.mediscreen.risk.model.Risk;
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
        Risk risk = riskService.getRisk(patId);
        if (risk!=null)
        {
            return risk.toString();
        }
        else
        {
            throw new BadRequestException("Can not calculate Risk");
        }
    }

    @GetMapping("/assess/familyName")
    public String getRiskForPatient(@RequestParam String familyName)
    {
        Risk risk = riskService.getRisk(familyName);
        if (risk!=null)
        {
            return risk.toString();
        }
        else
        {
            throw new BadRequestException("Can not calculate Risk");
        }
    }
}
