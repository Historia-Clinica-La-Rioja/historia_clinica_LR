package net.pladema.clinichistory.documents.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.Valid;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class HCEVitalSignDto implements Serializable {

    @Valid
    @Nullable
    private HCEEffectiveClinicalObservationDto systolicBloodPressure;

    @Valid
    @Nullable
    private HCEEffectiveClinicalObservationDto diastolicBloodPressure;

    @Valid
    @Nullable
    private HCEEffectiveClinicalObservationDto temperature;

    @Valid
    @Nullable
    private HCEEffectiveClinicalObservationDto heartRate;

    @Valid
    @Nullable
    private HCEEffectiveClinicalObservationDto respiratoryRate;

    @Valid
    @Nullable
    private HCEEffectiveClinicalObservationDto bloodOxygenSaturation;

}
