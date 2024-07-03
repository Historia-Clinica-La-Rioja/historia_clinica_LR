package net.pladema.clinichistory.hospitalization.controller.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.constraints.HeightDataValid;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.constraints.WeightDataValid;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.EffectiveClinicalObservationDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.Valid;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class InternmentAnthropometricDataDto implements Serializable {

    @Valid
    @Nullable
    private EffectiveClinicalObservationDto bloodType;

    @Valid
    @Nullable
    @HeightDataValid(message = "{diagnosis.anthropometric.height.invalid}")
    private EffectiveClinicalObservationDto height;

    @Valid
    @Nullable
    @WeightDataValid(message = "{diagnosis.anthropometric.weight.invalid}")
    private EffectiveClinicalObservationDto weight;

    @Nullable
    private EffectiveClinicalObservationDto bmi;

    @Valid
    @Nullable
    private EffectiveClinicalObservationDto headCircumference;

}
