package net.pladema.establishment.application.institutionpractices;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.snowstorm.infrastructure.input.service.SnomedRelatedGroupExternalService;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class GetPracticesFromInstitutionsByCareLineId {

	private final SnomedRelatedGroupExternalService snomedRelatedGroupExternalService;

	public List<SharedSnomedDto> run(Integer careLineId) {
		log.debug("Fetch practices for all institutions");
		List<SharedSnomedDto> result;
		if (careLineId == null)
			result = snomedRelatedGroupExternalService.getPracticesFromAllInstitutions();
		else
			result = snomedRelatedGroupExternalService.getPracticesFromAllInstitutionsByCareLineId(careLineId);
		log.debug("Output -> {}", result);
		return result;
	}

}
