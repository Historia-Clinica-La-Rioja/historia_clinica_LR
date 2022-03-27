package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgx.shared.exceptions.SelfValidating;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;

@Getter
@Setter
@ToString
public class AnthropometricDataBo extends SelfValidating<AnthropometricDataBo> {

    @Valid
    private ClinicalObservationBo bloodType;

    @Valid
    private ClinicalObservationBo height;

    @Valid
    private ClinicalObservationBo weight;

    @Valid
    private ClinicalObservationBo headCircumference;

    public boolean hasValues(){
        return (bloodType != null ||
                height != null ||
                weight != null ||
                headCircumference != null);
    }

    public ClinicalObservationBo getBMI(){
        if (height == null || weight == null)
            return null;
        if (height.getValue() == null || weight.getValue() == null)
            return null;
        if (height.getValue().isEmpty() || weight.getValue().isEmpty())
            return null;
		Float convertedHeight = Float.parseFloat(height.getValue());
		if (convertedHeight == 0f){
			return new ClinicalObservationBo(null, "-", weight.getEffectiveTime());
		}
        Double bmi = Float.parseFloat(weight.getValue()) / Math.pow((convertedHeight/100),2);
        return new ClinicalObservationBo(null, String.format("%.02f", bmi), weight.getEffectiveTime());
    }
}
