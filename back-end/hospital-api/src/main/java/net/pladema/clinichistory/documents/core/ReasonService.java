package net.pladema.clinichistory.documents.core;

import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;

import java.util.List;

public interface ReasonService {

    List<ReasonBo> addReasons(Integer outpatientId, List<ReasonBo> reasons);

    List<String> addReasons(List<ReasonBo> reasons);
}
