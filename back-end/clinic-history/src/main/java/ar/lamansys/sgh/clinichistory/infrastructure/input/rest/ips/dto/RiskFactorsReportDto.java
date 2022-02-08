package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.Valid;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class RiskFactorsReportDto implements Serializable {

    @Valid
    @Nullable
    private ReportClinicalObservationDto systolicBloodPressure;

    @Valid
    @Nullable
    private ReportClinicalObservationDto diastolicBloodPressure;

    @Valid
    @Nullable
    private ReportClinicalObservationDto temperature;

    @Valid
    @Nullable
    private ReportClinicalObservationDto heartRate;

    @Valid
    @Nullable
    private ReportClinicalObservationDto respiratoryRate;

    @Valid
    @Nullable
    private ReportClinicalObservationDto bloodOxygenSaturation;

	@Valid
	@Nullable
	private ReportClinicalObservationDto bloodGlucose;

	@Valid
	@Nullable
	private ReportClinicalObservationDto glycosylatedHemoglobin;

	@Valid
	@Nullable
	private ReportClinicalObservationDto cardiovascularRisk;

}
