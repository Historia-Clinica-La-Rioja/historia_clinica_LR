package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class Last2RiskFactorsBo implements Serializable {

    private RiskFactorBo current;

    private RiskFactorBo previous;

}
