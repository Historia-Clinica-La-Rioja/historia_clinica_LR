package net.pladema.reports.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FormVDto {

    private String establishment;

    private String completePatientName;

    private String address;

    private LocalDate reportDate;

    private String patientGender;

    private Short patientAge;

    private String documentType;

    private String documentNumber;

    private String medicalCoverage;

    private String affiliateNumber;

    private LocalDate consultationDate;

    private String problems;

    private String sisaCode;

    private String cie10Codes;
}
