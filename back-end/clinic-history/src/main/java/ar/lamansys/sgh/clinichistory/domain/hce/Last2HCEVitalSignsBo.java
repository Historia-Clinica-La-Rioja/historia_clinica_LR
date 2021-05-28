package ar.lamansys.sgh.clinichistory.domain.hce;

import ar.lamansys.sgh.clinichistory.domain.hce.HCEVitalSignBo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class Last2HCEVitalSignsBo implements Serializable {

    private HCEVitalSignBo current;

    private HCEVitalSignBo previous;

}
