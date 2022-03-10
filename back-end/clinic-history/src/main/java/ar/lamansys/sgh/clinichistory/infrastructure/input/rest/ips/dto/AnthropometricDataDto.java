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

}
