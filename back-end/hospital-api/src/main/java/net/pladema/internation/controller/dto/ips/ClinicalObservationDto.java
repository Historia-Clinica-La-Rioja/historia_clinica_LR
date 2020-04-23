package net.pladema.internation.controller.dto.ips;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.controller.dto.SnomedDto;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class ClinicalObservationDto implements Serializable {

    private Integer id;

    private String value;

    private boolean deleted;
}
