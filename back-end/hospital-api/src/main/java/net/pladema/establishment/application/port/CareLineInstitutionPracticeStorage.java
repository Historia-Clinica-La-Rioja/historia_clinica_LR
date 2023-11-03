package net.pladema.establishment.application.port;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;

import java.util.List;
import java.util.Map;

public interface CareLineInstitutionPracticeStorage {

	Map<Integer, List<SnomedBo>> fetchAllByCareLineIds(List<Integer> careLineIds);

 	List<SnomedBo> fetchPracticesByCareLineId(Integer careLineId);

}
