package net.pladema.establishment.application.port;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;

import java.util.List;
import java.util.Map;

public interface PracticeCareLineStorage {

	Map<Integer, List<SnomedBo>> fetchAllByCareLineIds(List<Integer> careLineIds);

}
