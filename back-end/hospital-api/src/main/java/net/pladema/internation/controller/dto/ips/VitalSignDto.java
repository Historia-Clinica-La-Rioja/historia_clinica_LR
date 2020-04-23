package net.pladema.internation.controller.dto.ips;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class VitalSignDto implements Serializable {

    private ClinicalObservationDto systolicBloodPressure;

    private ClinicalObservationDto diastolicBloodPressure;

    private ClinicalObservationDto meanPressure;

    private ClinicalObservationDto temperature;

    private ClinicalObservationDto heartRate;

    private ClinicalObservationDto respiratoryRate;

    private ClinicalObservationDto bloodOxygenSaturation;

}
