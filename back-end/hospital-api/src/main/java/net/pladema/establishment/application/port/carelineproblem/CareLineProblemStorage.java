package net.pladema.establishment.application.port.carelineproblem;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;

import java.util.List;
import java.util.Map;

public interface CareLineProblemStorage {

	Map<Integer, List<SnomedBo>> fetchAllByCareLineIds(List<Integer> careLineIds);

}
