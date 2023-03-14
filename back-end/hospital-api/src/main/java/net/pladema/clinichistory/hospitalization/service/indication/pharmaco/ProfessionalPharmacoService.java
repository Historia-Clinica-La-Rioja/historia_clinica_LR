package net.pladema.clinichistory.hospitalization.service.indication.pharmaco;

import ar.lamansys.sgh.shared.infrastructure.input.service.PharmacoSummaryDto;

import java.util.List;

public interface ProfessionalPharmacoService {
	List<PharmacoSummaryDto> getMostFrequentPharmacos(Integer institutionId);
}
