package net.pladema.clinichistory.requests.medicationrequests.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.snowstorm.exceptions.SnowstormPortException;
import net.pladema.clinichistory.requests.medicationrequests.service.domain.SnomedBo;
import java.util.List;

public interface FetchMostFrequentPharmacos {

	List<SnomedBo> run(Integer institutionId) throws SnowstormPortException;
}
