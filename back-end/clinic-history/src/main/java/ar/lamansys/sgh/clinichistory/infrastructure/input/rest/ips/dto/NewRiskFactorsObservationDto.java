package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import java.io.Serializable;

import javax.annotation.Nullable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class NewRiskFactorsObservationDto implements Serializable {

    @Nullable
    private NewEffectiveClinicalObservationDto systolicBloodPressure;

    @Nullable
    private NewEffectiveClinicalObservationDto diastolicBloodPressure;

    @Nullable
    private NewEffectiveClinicalObservationDto temperature;

    @Nullable
    private NewEffectiveClinicalObservationDto heartRate;

    @Nullable
    private NewEffectiveClinicalObservationDto respiratoryRate;

    @Nullable
    private NewEffectiveClinicalObservationDto bloodOxygenSaturation;

	@Nullable
	private NewEffectiveClinicalObservationDto bloodGlucose;

	@Nullable
	private NewEffectiveClinicalObservationDto glycosylatedHemoglobin;

	@Nullable
	private NewEffectiveClinicalObservationDto cardiovascularRisk;

}
