package net.pladema.internation.service.domain.ips;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class VitalSignBo implements Serializable {

    private ClinicalObservation systolicBloodPressure;

    private ClinicalObservation diastolicBloodPressure;

    private ClinicalObservation meanPressure;

    private ClinicalObservation temperature;

    private ClinicalObservation heartRate;

    private ClinicalObservation respiratoryRate;

    private ClinicalObservation bloodOxygenSaturation;

}
