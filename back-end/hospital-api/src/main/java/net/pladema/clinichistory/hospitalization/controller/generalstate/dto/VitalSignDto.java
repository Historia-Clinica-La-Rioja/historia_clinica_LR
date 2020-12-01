package net.pladema.clinichistory.hospitalization.controller.generalstate.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.controller.dto.EffectiveClinicalObservationDto;

import javax.annotation.Nullable;
import javax.validation.Valid;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class VitalSignDto implements Serializable {

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

}
