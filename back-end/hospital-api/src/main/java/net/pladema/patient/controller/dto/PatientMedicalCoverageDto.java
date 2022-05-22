package net.pladema.patient.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;

import javax.annotation.Nullable;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class PatientMedicalCoverageDto {

    @Nullable
    private Integer id;

    @Nullable
    private String affiliateNumber;

    @Nullable
    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    private String vigencyDate;

    private Boolean active;

    private CoverageDto medicalCoverage;

    @Nullable
    private PrivateHealthInsuranceDetailsDto privateHealthInsuranceDetails;

	private EPatientMedicalCoverageCondition condition;
}
