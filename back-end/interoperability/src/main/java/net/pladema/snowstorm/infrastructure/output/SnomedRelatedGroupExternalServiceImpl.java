package net.pladema.snowstorm.infrastructure.output;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.snowstorm.infrastructure.input.service.SnomedRelatedGroupExternalService;
import net.pladema.snowstorm.repository.SnomedRelatedGroupRepository;
import net.pladema.snowstorm.repository.entity.SnomedGroupType;

import net.pladema.snowstorm.services.domain.semantics.SnomedECL;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class SnomedRelatedGroupExternalServiceImpl implements SnomedRelatedGroupExternalService {

	private final SnomedRelatedGroupRepository snomedRelatedGroupRepository;

	@Override
	public List<SharedSnomedDto> getPracticesByInstitution(Integer institutionId) {
		log.debug("Input parameter -> institutionId {}", institutionId);
		String description = SnomedECL.PROCEDURE.toString();
		List<SharedSnomedDto> result = snomedRelatedGroupRepository.getAllByInstitutionAndDescriptionAndGroupType(institutionId, description, SnomedGroupType.SEARCH_GROUP).stream()
				.map(bo -> 	new SharedSnomedDto(bo.getId(), bo.getSctid(), bo.getPt()))
				.collect(Collectors.toList());
		log.debug("Output {}", result);
		return result;

	}

	@Override
	public List<SharedSnomedDto> getPracticesFromAllInstitutions() {
		log.debug("Fetch practices for all institutions");
		return snomedRelatedGroupRepository.getAllByDescriptionAndGroupType(SnomedECL.PROCEDURE.toString(), SnomedGroupType.SEARCH_GROUP)
				.stream()
				.map(bo -> 	new SharedSnomedDto(bo.getId(), bo.getSctid(), bo.getPt()))
				.collect(Collectors.toList());
	}

	@Override
	public List<SharedSnomedDto> getPracticesFromAllInstitutionsByCareLineId(Integer careLineId) {
		log.debug("Fetch practices for all institutions");
		List<SharedSnomedDto> result = snomedRelatedGroupRepository.getAllByDescriptionAndGroupTypeAndCareLineId(SnomedECL.PROCEDURE.toString(), SnomedGroupType.SEARCH_GROUP, careLineId)
				.stream()
				.map(bo -> 	new SharedSnomedDto(bo.getId(), bo.getSctid(), bo.getPt()))
				.collect(Collectors.toList());
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public List<SharedSnomedDto> getPracticesByDepartmentId(Short departmentId) {
		log.debug("Input parameter -> departmentId {} ", departmentId);
		List<SharedSnomedDto> result = snomedRelatedGroupRepository.getAllByDepartmentId(SnomedECL.PROCEDURE.toString(), SnomedGroupType.SEARCH_GROUP, departmentId)
				.stream()
				.map(bo -> 	new SharedSnomedDto(bo.getId(), bo.getSctid(), bo.getPt()))
				.collect(Collectors.toList());
		log.debug("Output -> {}", result);
		return result;
	}

}
