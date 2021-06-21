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
    private HCEClinicalObservationDto bloodType;

    @Valid
    @Nullable
    private HCEClinicalObservationDto height;

    @Valid
    @Nullable
    private HCEClinicalObservationDto weight;

    @Nullable
    private HCEClinicalObservationDto bmi;

}
