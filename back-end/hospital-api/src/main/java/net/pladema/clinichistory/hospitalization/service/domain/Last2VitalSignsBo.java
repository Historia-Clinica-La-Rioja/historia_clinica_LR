package net.pladema.clinichistory.hospitalization.service.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.ips.service.domain.VitalSignBo;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class Last2VitalSignsBo implements Serializable {

    private VitalSignBo current;

    private VitalSignBo previous;

}
