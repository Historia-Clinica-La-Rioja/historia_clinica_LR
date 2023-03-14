package net.pladema.clinichistory.hospitalization.service.indication.pharmaco;

import ar.lamansys.sgh.shared.infrastructure.input.service.PharmacoSummaryDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedIndicationPort;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfessionalPharmacoServiceImpl implements ProfessionalPharmacoService {
	private final SharedIndicationPort sharedIndicationPort;
	private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;

	@Value("${app.indication.medication.most-frequent.max:15}")
	private Integer indicationMedicationMostFrequentMax;

	@Override
	public List<PharmacoSummaryDto> getMostFrequentPharmacos(Integer institutionId) {
		log.debug("Input parameter -> institutionId {}", institutionId);

		Integer professionalId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());

		List<PharmacoSummaryDto> result = sharedIndicationPort.getMostFrequentPharmacos(professionalId, institutionId, indicationMedicationMostFrequentMax);
		log.debug("Output -> {}", result);
		return result;
	}
}
