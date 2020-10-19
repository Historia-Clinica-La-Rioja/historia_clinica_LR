package net.pladema.clinichistory.hospitalization.controller.generalstate.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.controller.constraints.AnthropometricDataValid;
import net.pladema.clinichistory.ips.controller.dto.ClinicalObservationDto;

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
    @AnthropometricDataValid
    private ClinicalObservationDto height;

    @Valid
    @Nullable
    @AnthropometricDataValid
    private ClinicalObservationDto weight;

    @Nullable
    private ClinicalObservationDto bmi;

}
