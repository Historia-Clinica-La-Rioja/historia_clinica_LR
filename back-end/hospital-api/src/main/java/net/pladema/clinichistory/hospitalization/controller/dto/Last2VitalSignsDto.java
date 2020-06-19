package net.pladema.clinichistory.hospitalization.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.ips.controller.dto.VitalSignDto;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class Last2VitalSignsDto implements Serializable {

    private VitalSignDto current;

    private VitalSignDto previous;

}
