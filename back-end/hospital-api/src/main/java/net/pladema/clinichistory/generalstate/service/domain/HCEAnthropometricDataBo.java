package net.pladema.clinichistory.generalstate.service.domain;

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

    public boolean hasValues(){
        return (bloodType != null ||
                height != null ||
                weight != null);
    }

    public HCEClinicalObservationBo getBMI(){
        if (height == null || weight == null)
            return null;
        if (height.getValue() == null || weight.getValue() == null)
            return null;
        if (height.getValue().isEmpty() || weight.getValue().isEmpty())
            return null;
        try {
            Double bmi = Float.parseFloat(weight.getValue()) / Math.pow((Float.parseFloat(height.getValue())/100),2);
            return new HCEClinicalObservationBo(null, String.format("%.02f", bmi), weight.getEffectiveTime());
        } catch (Exception e) {
            return null;
        }
    }
}
