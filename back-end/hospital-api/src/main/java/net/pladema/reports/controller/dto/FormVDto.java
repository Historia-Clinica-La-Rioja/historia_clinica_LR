package net.pladema.reports.controller.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FormVDto {

    private String establishment;

    private String completePatientName;

	private String formalPatientName;

    private String address;

    private LocalDate reportDate;

    private String patientGender;

    private Short patientAge;

    private String documentType;

    private String documentNumber;

    private String medicalCoverage;

    private String affiliateNumber;

    private LocalDateTime consultationDate;

    private String problems;

    private String sisaCode;

    private String cie10Codes;

	private String medicalCoverageCondition;

	private String establishmentProvinceCode;

	private Integer hcnId;

	private String completeProfessionalName;

	private List<String> licenses;

	private String bedNumber;

	private String roomNumber;

}
