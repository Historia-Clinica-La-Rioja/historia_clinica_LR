package net.pladema.clinichistory.outpatient.createoutpatient.service.outpatientReason;

import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;

import java.util.List;

public interface OutpatientReasonService {

    List<ReasonBo> addReasons(Integer outpatientId, List<ReasonBo> reasons);

}
