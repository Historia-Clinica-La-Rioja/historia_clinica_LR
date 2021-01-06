package net.pladema.clinichistory.outpatient.createoutpatient.service;

import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ReasonBo;

import java.util.List;

public interface ReasonService {

    List<ReasonBo> addReasons(Integer outpatientId, List<ReasonBo> reasons);

    List<String> addReasons(List<ReasonBo> reasons);
}
