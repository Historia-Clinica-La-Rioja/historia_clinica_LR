package net.pladema.internation.controller.ips.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class AnthropometricDataDto implements Serializable {

    @Valid
    private ClinicalObservationDto bloodType;

    @Valid
    private ClinicalObservationDto height;

    @Valid
    private ClinicalObservationDto weight;

    private ClinicalObservationDto BMI;

}
