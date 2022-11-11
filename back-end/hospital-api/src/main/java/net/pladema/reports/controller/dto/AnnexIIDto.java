package net.pladema.reports.controller.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnnexIIDto {

    private LocalDate reportDate;

    private String establishment;

    private String completePatientName;

    private String documentType;

    private String documentNumber;

    private String patientGender;

    private Short patientAge;

    private String appointmentState;

    private LocalDate attentionDate;

    private String medicalCoverage;

    private String affiliateNumber;

    private Boolean existsConsultation;

    private Boolean hasProcedures;

    private String specialty;

    private LocalDate consultationDate;

    private String sisaCode;

    private String problems;

	private Integer rnos;
}
