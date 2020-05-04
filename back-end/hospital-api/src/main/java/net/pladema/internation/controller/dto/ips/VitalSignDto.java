package net.pladema.internation.controller.dto.ips;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class VitalSignDto implements Serializable {

    @Valid
    private ClinicalObservationDto systolicBloodPressure;

    @Valid
    private ClinicalObservationDto diastolicBloodPressure;

    @Valid
    private ClinicalObservationDto temperature;

    @Valid
    private ClinicalObservationDto heartRate;

    @Valid
    private ClinicalObservationDto respiratoryRate;

    @Valid
    private ClinicalObservationDto bloodOxygenSaturation;

}
