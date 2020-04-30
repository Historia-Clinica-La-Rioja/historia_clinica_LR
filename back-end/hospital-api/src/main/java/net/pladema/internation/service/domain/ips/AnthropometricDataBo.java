package net.pladema.internation.service.domain.ips;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class AnthropometricDataBo implements Serializable {

    private ClinicalObservationBo bloodType;

    private ClinicalObservationBo height;

    private ClinicalObservationBo weight;

}
