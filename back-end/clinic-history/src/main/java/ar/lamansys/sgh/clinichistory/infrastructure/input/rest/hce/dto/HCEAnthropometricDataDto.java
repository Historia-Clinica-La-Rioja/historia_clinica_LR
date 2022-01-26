package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.Valid;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class HCEAnthropometricDataDto implements Serializable {

    @Valid
    @Nullable
    private HCEEffectiveClinicalObservationDto bloodType;

    @Valid
    @Nullable
    private HCEEffectiveClinicalObservationDto height;

    @Valid
    @Nullable
    private HCEEffectiveClinicalObservationDto weight;

    @Valid
    @Nullable
    private HCEEffectiveClinicalObservationDto bmi;

    @Valid
    @Nullable
    private HCEEffectiveClinicalObservationDto headCircumference;

}
