package net.pladema.snowstorm.infrastructure.output;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.snowstorm.application.port.SnomedRelatedGroupStorage;

import net.pladema.snowstorm.repository.SnomedRelatedGroupRepository;
import net.pladema.snowstorm.repository.entity.SnomedGroupType;
import net.pladema.snowstorm.services.domain.SnomedRelatedGroupBo;

import net.pladema.snowstorm.services.domain.semantics.SnomedECL;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class SnomedRelatedGroupStorageImpl implements SnomedRelatedGroupStorage {

	private final SnomedRelatedGroupRepository snomedRelatedGroupRepository;

	@Override
	public List<SnomedRelatedGroupBo> getPracticesByInstitution(Integer institutionId) {

		log.debug("Input parameter -> institutionId {}", institutionId);
		String description = SnomedECL.PROCEDURE.toString();
		List<SnomedRelatedGroupBo> result = snomedRelatedGroupRepository.getAllByInstitutionAndDescriptionAndGroupType(institutionId, description, SnomedGroupType.SEARCH_GROUP);
		log.debug("Output {}", result);
		return result;

	}
}
