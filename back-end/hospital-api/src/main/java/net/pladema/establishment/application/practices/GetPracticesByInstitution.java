package net.pladema.establishment.application.practices;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.snowstorm.infrastructure.input.service.SnomedRelatedGroupExternalService;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class GetPracticesByInstitution {

	private final SnomedRelatedGroupExternalService snomedRelatedGroupExternalService;

	public List<SharedSnomedDto> run(Integer institutionId) {
		log.debug("Input parameter -> institutionId {}", institutionId);
		List<SharedSnomedDto> result = snomedRelatedGroupExternalService.getPracticesByInstitution(institutionId);
		log.debug("Output Get Institution Practices By Institution{}", result.toString());
		return result;
	}
}
