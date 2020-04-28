package net.pladema.internation.controller.dto.ips;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class AnthropometricDataDto implements Serializable {

    private ClinicalObservationDto bloodType;

    private ClinicalObservationDto height;

    private ClinicalObservationDto weight;

    private boolean deleted = false;

}
