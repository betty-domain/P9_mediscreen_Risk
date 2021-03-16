package com.mediscreen.risk.services;

import com.mediscreen.risk.model.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name="mediscreen-patient", url="http://localhost:9101")
public interface PatientsProxy {

    @GetMapping(value = "/patients")
    List<Patient> getAllPatients();

    @GetMapping("/patient")
    Patient getPatient(@RequestParam Integer id);

    @PostMapping(value="/patient/add")
    Patient addPatient(@RequestParam String family, @RequestParam  String given, @RequestParam @DateTimeFormat(pattern ="yyyy-MM-dd") LocalDate dob, @RequestParam String sex, @RequestParam String address, @RequestParam String phone);

    @PutMapping(value="/patient/update")
    Patient updatePatient(@RequestParam Integer id, @RequestParam String family, @RequestParam  String given, @RequestParam @DateTimeFormat(pattern ="yyyy-MM-dd") LocalDate dob, @RequestParam String sex, @RequestParam String address, @RequestParam String phone);


}
