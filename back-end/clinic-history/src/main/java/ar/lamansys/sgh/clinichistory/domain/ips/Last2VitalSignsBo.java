package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.domain.ips.VitalSignBo;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class Last2VitalSignsBo implements Serializable {

    private VitalSignBo current;

    private VitalSignBo previous;

}
