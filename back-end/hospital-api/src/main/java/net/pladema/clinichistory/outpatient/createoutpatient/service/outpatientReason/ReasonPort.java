package net.pladema.clinichistory.outpatient.createoutpatient.service.outpatientReason;

import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;

public interface ReasonPort {

    ReasonBo saveReason(ReasonBo reasonBo);
}
