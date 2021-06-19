package net.pladema.clinichistory.outpatient.createoutpatient.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.EffectiveClinicalObservationDto;

import javax.annotation.Nullable;
import javax.validation.Valid;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class OutpatientVitalSignDto implements Serializable {

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
