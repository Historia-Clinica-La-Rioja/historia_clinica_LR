package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class HCELast2VitalSignsDto implements Serializable {

    private HCEVitalSignDto current;

    private HCEVitalSignDto previous;

}
