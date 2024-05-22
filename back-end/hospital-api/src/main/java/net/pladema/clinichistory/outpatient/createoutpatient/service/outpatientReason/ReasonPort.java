package net.pladema.clinichistory.outpatient.createoutpatient.service.outpatientReason;

import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;

import java.util.Optional;

public interface ReasonPort {

    ReasonBo saveReason(ReasonBo reasonBo);

	Optional<ReasonBo> getByReasonId(String reasonId);
}
