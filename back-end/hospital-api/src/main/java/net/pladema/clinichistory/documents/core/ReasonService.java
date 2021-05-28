package net.pladema.clinichistory.documents.core;

import net.pladema.clinichistory.documents.service.ips.domain.ReasonBo;

import java.util.List;

public interface ReasonService {

    List<ReasonBo> addReasons(Integer outpatientId, List<ReasonBo> reasons);

    List<String> addReasons(List<ReasonBo> reasons);
}
