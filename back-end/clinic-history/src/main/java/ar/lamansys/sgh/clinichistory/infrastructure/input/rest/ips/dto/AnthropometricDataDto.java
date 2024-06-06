package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.constraints.HeightDataValid;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.constraints.WeightDataValid;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.Valid;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class AnthropometricDataDto implements Serializable {

    @Valid
    @Nullable
    private ClinicalObservationDto bloodType;

    @Valid
    @Nullable
    @HeightDataValid(message = "{diagnosis.anthropometric.height.invalid}")
    private ClinicalObservationDto height;

    @Valid
    @Nullable
    @WeightDataValid(message = "{diagnosis.anthropometric.weight.invalid}")
    private ClinicalObservationDto weight;

    @Nullable
    private ClinicalObservationDto bmi;

    @Valid
    @Nullable
    private ClinicalObservationDto headCircumference;

    public boolean hasValues() {
        return this.hasMinimalValues()
                || (bmi != null && bmi.getValue() != null)
                || (headCircumference != null && headCircumference.getValue() != null);
    }

    public boolean hasMinimalValues() {
        return (bloodType != null && bloodType.getValue() != null)
                ||(height != null && height.getValue() != null)
                ||(weight != null && weight.getValue() != null);
    }

    @Nullable
    public ClinicalObservationDto getBMI() {
        return bmi;
    }
}
