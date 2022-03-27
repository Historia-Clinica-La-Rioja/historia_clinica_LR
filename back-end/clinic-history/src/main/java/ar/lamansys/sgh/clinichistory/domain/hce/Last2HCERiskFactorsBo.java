package ar.lamansys.sgh.clinichistory.domain.hce;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class Last2HCERiskFactorsBo implements Serializable {

    private HCERiskFactorBo current;

    private HCERiskFactorBo previous;

}
