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
}
