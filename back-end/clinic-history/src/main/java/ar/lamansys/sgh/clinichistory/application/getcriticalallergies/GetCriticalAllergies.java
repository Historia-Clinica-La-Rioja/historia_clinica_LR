package ar.lamansys.sgh.clinichistory.application.getcriticalallergies;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.fetchHCE.HCEAllergyService;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEAllergyBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetCriticalAllergies {

	private final HCEAllergyService hceAllergyService;

	public List<HCEAllergyBo> run(Integer institutionId, Integer patientId) {
		log.debug("Input parameters -> patientId {}", patientId);
		List<HCEAllergyBo> result = hceAllergyService.getAllergies(patientId)
				.stream()
				.filter(a -> a.getCriticalityId() != null && a.getCriticalityId() == 2)
				.collect(Collectors.toList());
		result.addAll(hceAllergyService.getActiveInternmentEpisodeAllergies(institutionId, patientId));
		log.debug("Output -> {}", result);
		return result;
	}

}
