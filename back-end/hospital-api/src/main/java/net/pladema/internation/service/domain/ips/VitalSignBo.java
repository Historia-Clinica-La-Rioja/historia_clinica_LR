package net.pladema.internation.service.domain.ips;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class VitalSignBo implements Serializable {

    private ClinicalObservationBo systolicBloodPressure;

    private ClinicalObservationBo diastolicBloodPressure;

    private ClinicalObservationBo meanPressure;

    private ClinicalObservationBo temperature;

    private ClinicalObservationBo heartRate;

    private ClinicalObservationBo respiratoryRate;

    private ClinicalObservationBo bloodOxygenSaturation;

    public boolean hasValues(){
        return (systolicBloodPressure != null ||
                diastolicBloodPressure != null ||
                meanPressure != null ||
                temperature != null ||
                heartRate != null ||
                respiratoryRate != null ||
                bloodOxygenSaturation != null);
    }
}
