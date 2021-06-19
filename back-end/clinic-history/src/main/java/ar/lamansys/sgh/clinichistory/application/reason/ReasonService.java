package ar.lamansys.sgh.clinichistory.application.reason;

import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;

import java.util.List;

public interface ReasonService {

    List<String> addReasons(List<ReasonBo> reasons);
}
