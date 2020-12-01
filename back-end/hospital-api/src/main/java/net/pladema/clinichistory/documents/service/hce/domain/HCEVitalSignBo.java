package net.pladema.clinichistory.documents.service.hce.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HCEVitalSignBo implements Serializable {

    private HCEClinicalObservationBo systolicBloodPressure;

    private HCEClinicalObservationBo diastolicBloodPressure;

    private HCEClinicalObservationBo meanPressure;

    private HCEClinicalObservationBo temperature;

    private HCEClinicalObservationBo heartRate;

    private HCEClinicalObservationBo respiratoryRate;

    private HCEClinicalObservationBo bloodOxygenSaturation;

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
