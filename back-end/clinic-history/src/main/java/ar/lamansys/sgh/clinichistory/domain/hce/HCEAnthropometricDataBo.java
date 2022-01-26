package ar.lamansys.sgh.clinichistory.domain.hce;

import ar.lamansys.sgh.clinichistory.domain.hce.HCEClinicalObservationBo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class HCEAnthropometricDataBo implements Serializable {

    private HCEClinicalObservationBo bloodType;

    private HCEClinicalObservationBo height;

    private HCEClinicalObservationBo weight;

    private HCEClinicalObservationBo headCircumference;

    public boolean hasValues(){
        return (bloodType != null ||
                height != null ||
                weight != null ||
                headCircumference != null);
    }

    public HCEClinicalObservationBo getBmi(){
        if (height == null || weight == null)
            return null;
        if (height.getValue() == null || weight.getValue() == null)
            return null;
        if (height.getValue().isEmpty() || weight.getValue().isEmpty())
            return null;
        if (Float.parseFloat(height.getValue()) <= 0)
        	return null;
        try {
            Double bmi = Float.parseFloat(weight.getValue()) / Math.pow((Float.parseFloat(height.getValue())/100),2);
            return new HCEClinicalObservationBo(null, String.format("%.02f", bmi), weight.getEffectiveTime());
        } catch (Exception e) {
            return null;
        }
    }
}
