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
public class RiskFactorDto implements Serializable {

    @Valid
    @Nullable
    private EffectiveClinicalObservationDto systolicBloodPressure;

    @Valid
    @Nullable
    private EffectiveClinicalObservationDto diastolicBloodPressure;

    @Valid
    @Nullable
    private EffectiveClinicalObservationDto temperature;

    @Valid
    @Nullable
    private EffectiveClinicalObservationDto heartRate;

    @Valid
    @Nullable
    private EffectiveClinicalObservationDto respiratoryRate;

    @Valid
    @Nullable
    private EffectiveClinicalObservationDto bloodOxygenSaturation;

    @Valid
    @Nullable
    private EffectiveClinicalObservationDto bloodGlucose;

    @Valid
    @Nullable
    private EffectiveClinicalObservationDto glycosylatedHemoglobin;

    @Valid
    @Nullable
    private EffectiveClinicalObservationDto cardiovascularRisk;

}
