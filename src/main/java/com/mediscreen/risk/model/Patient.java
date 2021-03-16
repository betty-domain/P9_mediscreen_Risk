package com.mediscreen.risk.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class Patient {

    private Integer id;

    private String firstname;

    private String lastname;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private String address;

    private String phone;

    private String sex;

    public Patient()
    {

    }

    public Patient(Integer id, String firstname, String lastname, String sex, LocalDate birthdate, String address, String phone)
    {
        this.id =  id;
        this.firstname =  firstname;
        this.lastname = lastname;
        this.sex = sex;
        this.address= address;
        this.phone = phone;
        this.birthDate = birthdate;
    }
}
