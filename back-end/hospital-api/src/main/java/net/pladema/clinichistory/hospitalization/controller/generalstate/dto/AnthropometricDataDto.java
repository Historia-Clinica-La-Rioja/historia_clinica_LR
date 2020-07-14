package net.pladema.clinichistory.hospitalization.controller.generalstate.dto;

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
    private ClinicalObservationDto height;

    @Valid
    @Nullable
    private ClinicalObservationDto weight;

    @Nullable
    private ClinicalObservationDto bmi;

}
