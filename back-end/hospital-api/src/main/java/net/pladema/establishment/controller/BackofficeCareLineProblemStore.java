package net.pladema.establishment.controller;

import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.RequiredArgsConstructor;
import net.pladema.establishment.controller.dto.CareLineProblemDto;
import net.pladema.establishment.repository.CareLineProblemRepository;
import net.pladema.establishment.repository.entity.CareLineProblem;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import net.pladema.sgx.exceptions.BackofficeValidationException;


import net.pladema.snowstorm.repository.SnomedGroupRepository;
import net.pladema.snowstorm.repository.SnomedRelatedGroupRepository;
import net.pladema.snowstorm.repository.VSnomedGroupConceptRepository;

import net.pladema.snowstorm.repository.entity.SnomedRelatedGroup;
import net.pladema.snowstorm.repository.entity.VSnomedGroupConcept;

import net.pladema.snowstorm.services.domain.semantics.SnomedECL;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BackofficeCareLineProblemStore implements BackofficeStore<CareLineProblemDto, Integer> {

	private final CareLineProblemRepository careLineProblemRepository;
	private final FeatureFlagsService featureFlagsService;
	private final VSnomedGroupConceptRepository vSnomedGroupConceptRepository;
	private final BackofficeSnowstormStore backofficeSnowstormStore;
	private final SnomedGroupRepository snomedGroupRepository;
	private final SnomedRelatedGroupRepository snomedRelatedGroupRepository;
	private final DateTimeProvider dateTimeProvider;

	@Override
	public Page<CareLineProblemDto> findAll(CareLineProblemDto entity, Pageable pageable) {
		return careLineProblemRepository.findByCareLineId(entity.getCareLineId(), pageable);
	}

	@Override
	public List<CareLineProblemDto> findAll() {
		return careLineProblemRepository.findAll()
				.stream()
				.map(this::mapToCareLineProblemDto)
				.collect(Collectors.toList());
	}

	@Override
	public List<CareLineProblemDto> findAllById(List<Integer> ids) {
		return careLineProblemRepository.findAllById(ids)
				.stream()
				.map(this::mapToCareLineProblemDto)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<CareLineProblemDto> findById(Integer id) {
		return careLineProblemRepository.findById(id).map(this::mapToCareLineProblemDto);
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	@Transactional
	public CareLineProblemDto save(CareLineProblemDto dto) {
		Integer snomedId;
		/* If cached concepts search is active, the id of Snomed entity will be on conceptId field */
		if (featureFlagsService.isOn(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS))
			snomedId = vSnomedGroupConceptRepository.findById(dto.getConceptId().intValue()).map(VSnomedGroupConcept::getConceptId).orElseThrow(null);
		/* If not, search the concept term on snowstorm to check if already exists, or it has to be created */
		else {
			snomedId = saveSnowstormConceptToGroup(dto.getConceptId().toString());
		}
		dto.setSnomedId(snomedId);
		Optional<CareLineProblem> careLineProblem = careLineProblemRepository.findByCareLineIdAndSnomedId(dto.getCareLineId(), dto.getSnomedId());
		/* Check if problem is associated to a care line already */
		if(careLineProblem.isPresent()){
			if (careLineProblem.get().isDeleted()) {
				careLineProblem.get().setDeleted(false);
				dto.setId(careLineProblemRepository.save(careLineProblem.get()).getId());
			} else {
				throw new BackofficeValidationException("care-line.problem.exists");
			}
		} else {
			dto.setId(careLineProblemRepository.save(mapToEntity(dto)).getId());
		}
		return dto;
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void deleteById(Integer id) {
		careLineProblemRepository.deleteById(id);
	}

	@Override
	public Example<CareLineProblemDto> buildExample(CareLineProblemDto entity) {
		return Example.of(entity);
	}

	private CareLineProblemDto mapToCareLineProblemDto(CareLineProblem entity){
		CareLineProblemDto result = new CareLineProblemDto();
		result.setId(entity.getId());
		result.setCareLineId(entity.getCareLineId());
		result.setSnomedId(entity.getSnomedId());
		return result;
	}

	private CareLineProblem mapToEntity(CareLineProblemDto dto){
		CareLineProblem result = new CareLineProblem();
		result.setId(dto.getId());
		result.setCareLineId(dto.getCareLineId());
		result.setSnomedId(dto.getSnomedId());
		return result;
	}

	private Integer saveSnowstormConceptToGroup(String conceptSctid) {
		Integer snomedId = backofficeSnowstormStore.saveSnowstormConcept(conceptSctid);
		Integer groupId = snomedGroupRepository.getIdByDescriptionAndInstitutionId(SnomedECL.DIAGNOSIS.toString(), -1).get();
		Integer orden = snomedRelatedGroupRepository.getLastOrdenByGroupId(groupId).orElse(0) + 1;
		Optional<SnomedRelatedGroup> snomedRelatedGroup = snomedRelatedGroupRepository.getByGroupIdAndSnomedId(groupId, snomedId);
		if (snomedRelatedGroup.isEmpty()){
			snomedRelatedGroupRepository.save(new SnomedRelatedGroup(snomedId, groupId, orden, dateTimeProvider.nowDate()));
		}
		return snomedId;
	}

}
