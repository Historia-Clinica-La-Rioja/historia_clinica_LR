package net.pladema.snowstorm.application.getSnomedGroup;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.snowstorm.application.port.SnomedRelatedGroupStorage;
import net.pladema.snowstorm.repository.entity.SnomedGroupType;
import net.pladema.snowstorm.services.domain.SnomedRelatedGroupBo;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class GetInstitutionPractices {

	private final SnomedRelatedGroupStorage snomedRelatedGroupStorage;

	public List<SnomedRelatedGroupBo> run(Integer institutionId) {
		log.debug("Input parameter -> institutionId {}", institutionId);
		List<SnomedRelatedGroupBo> result = snomedRelatedGroupStorage.getPracticesByInstitution(institutionId);
		log.debug("Output Get Institution Practices {}", result.toString());
		return result;
	}
}
