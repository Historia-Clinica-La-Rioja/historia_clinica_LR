package ar.lamansys.sgh.clinichistory.domain.hce;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HCERiskFactorBo implements Serializable {

    private HCEClinicalObservationBo systolicBloodPressure;

    private HCEClinicalObservationBo diastolicBloodPressure;

    private HCEClinicalObservationBo meanPressure;

    private HCEClinicalObservationBo temperature;

    private HCEClinicalObservationBo heartRate;

    private HCEClinicalObservationBo respiratoryRate;

    private HCEClinicalObservationBo bloodOxygenSaturation;

    private HCEClinicalObservationBo bloodGlucose;

    private HCEClinicalObservationBo glycosylatedHemoglobin;

    private HCEClinicalObservationBo cardiovascularRisk;

    public boolean hasValues(){
        return (systolicBloodPressure != null ||
                diastolicBloodPressure != null ||
                meanPressure != null ||
                temperature != null ||
                heartRate != null ||
                respiratoryRate != null ||
                bloodOxygenSaturation != null ||
                bloodGlucose != null ||
                glycosylatedHemoglobin != null ||
                cardiovascularRisk != null);
    }
}
