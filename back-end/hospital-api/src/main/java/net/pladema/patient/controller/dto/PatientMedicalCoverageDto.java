package net.pladema.patient.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import lombok.ToString;

import javax.annotation.Nullable;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@ToString
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

    private EPatientMedicalCoverageCondition condition;
    
	@Nullable
	@JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
	private String startDate;

	@Nullable
	@JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
	private String endDate;

	@Nullable
	private Integer planId;

	@Nullable
	private String planName;

	@Nullable
	public String getConditionValue() {
		if (condition != null)
			return condition.getValue();
		return null;
	}

	@Nullable
	public String getMedicalCoverageName() {
		return medicalCoverage.getName();
	}
}
