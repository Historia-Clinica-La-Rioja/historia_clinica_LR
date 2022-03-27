package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class Last2RiskFactorsDto implements Serializable {

    private RiskFactorDto current;

    private RiskFactorDto previous;

}
