package ar.lamansys.nursing.domain;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.EffectiveClinicalObservationDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.Valid;

@Getter
@Setter
@ToString
public class NursingVitalSignBo {

    @Valid
    private EffectiveClinicalObservationDto systolicBloodPressure;

    @Valid
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

}
