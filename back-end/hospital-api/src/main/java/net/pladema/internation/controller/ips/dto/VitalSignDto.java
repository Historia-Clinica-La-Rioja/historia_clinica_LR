package net.pladema.internation.controller.ips.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.Valid;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class VitalSignDto implements Serializable {

    @Valid
    @Nullable
    private ClinicalObservationDto systolicBloodPressure;

    @Valid
    @Nullable
    private ClinicalObservationDto diastolicBloodPressure;

    @Valid
    @Nullable
    private ClinicalObservationDto temperature;

    @Valid
    @Nullable
    private ClinicalObservationDto heartRate;

    @Valid
    @Nullable
    private ClinicalObservationDto respiratoryRate;

    @Valid
    @Nullable
    private ClinicalObservationDto bloodOxygenSaturation;

}
