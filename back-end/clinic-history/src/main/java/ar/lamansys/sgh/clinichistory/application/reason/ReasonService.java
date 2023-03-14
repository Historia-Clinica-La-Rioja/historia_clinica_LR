package ar.lamansys.sgh.clinichistory.application.reason;

import java.util.List;

import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;

public interface ReasonService {

    List<String> addReasons(List<ReasonBo> reasons);

	List<ReasonBo> fetchFromDocumentId(Long documentId);
}
