package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgh.clinichistory.domain.ips.visitor.IpsVisitor;
import ar.lamansys.sgx.shared.exceptions.SelfValidating;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RiskFactorBo extends SelfValidating<RiskFactorBo> implements IpsBo {

    @Valid
    private ClinicalObservationBo systolicBloodPressure;

    @Valid
    private ClinicalObservationBo diastolicBloodPressure;

    @Valid
    private ClinicalObservationBo meanPressure;

    @Valid
    private ClinicalObservationBo temperature;

    @Valid
    private ClinicalObservationBo heartRate;

    @Valid
    private ClinicalObservationBo respiratoryRate;

    @Valid
    private ClinicalObservationBo bloodOxygenSaturation;

    @Valid
    private ClinicalObservationBo bloodGlucose;

    @Valid
    private ClinicalObservationBo glycosylatedHemoglobin;

    @Valid
    private ClinicalObservationBo cardiovascularRisk;

    @Valid
    private ClinicalObservationBo hematocrit;

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
                cardiovascularRisk != null ||
                hematocrit != null);
    }

	public boolean isEmpty() {
		return systolicBloodPressure == null && diastolicBloodPressure == null && meanPressure == null && temperature == null && heartRate == null && respiratoryRate == null
				&& bloodOxygenSaturation == null && bloodGlucose == null && glycosylatedHemoglobin == null && cardiovascularRisk == null && hematocrit == null;
	}

    @Override
    public void accept(IpsVisitor visitor) {
        visitor.visitRiskFactor(this);
    }
}
