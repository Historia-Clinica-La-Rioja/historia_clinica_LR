package net.pladema.establishment.controller;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.establishment.controller.dto.BackofficeSnowstormDto;
import net.pladema.establishment.controller.dto.SnomedProblemDto;
import net.pladema.establishment.controller.dto.SnomedProcedureDto;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import net.pladema.snowstorm.repository.SnomedGroupRepository;
import net.pladema.snowstorm.repository.VSnomedGroupConceptRepository;

import net.pladema.snowstorm.repository.entity.VSnomedGroupConcept;
import net.pladema.snowstorm.services.domain.semantics.SnomedECL;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BackofficeSnomedProblemsStore implements BackofficeStore<SnomedProblemDto, Long> {

	private final BackofficeSnowstormStore backofficeSnowstormStore;
	private final VSnomedGroupConceptRepository vSnomedGroupConceptRepository;
	private final FeatureFlagsService featureFlagsService;
	private final SnomedGroupRepository snomedGroupRepository;

	public BackofficeSnomedProblemsStore(BackofficeSnowstormStore backofficeSnowstormStore,
										 VSnomedGroupConceptRepository vSnomedGroupConceptRepository,
										 FeatureFlagsService featureFlagsService, SnomedGroupRepository snomedGroupRepository){
		this.backofficeSnowstormStore = backofficeSnowstormStore;
		this.vSnomedGroupConceptRepository = vSnomedGroupConceptRepository;
		this.featureFlagsService = featureFlagsService;
		this.snomedGroupRepository = snomedGroupRepository;
	}

	@Override
	public Page<SnomedProblemDto> findAll(SnomedProblemDto example, Pageable pageable) {
		List<SnomedProblemDto> result = new ArrayList<>();
		if (featureFlagsService.isOn(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS)) {
			var groupId = snomedGroupRepository.getIdByDescriptionAndInstitutionId(example.getGroupDescription(), -1);
			if (groupId.isPresent()) {
				example.setGroupId(groupId.get());
				result = vSnomedGroupConceptRepository
						.findAll(buildExampleEntity(example), PageRequest.of(pageable.getPageNumber(), 100, pageable.getSort()))
						.stream()
						.map(this::mapToDto)
						.collect(Collectors.toList());
			}
		} else {
			if (example.getConceptPt() != null && !example.getConceptPt().isEmpty()) {
				var apiConcepts = backofficeSnowstormStore.findAll(new BackofficeSnowstormDto(example.getConceptPt()), pageable, SnomedECL.PROCEDURE).getContent();
				result = apiConcepts.stream().map(this::mapToDto).collect(Collectors.toList());
			}
		}
		return new PageImpl<>(result, pageable, result.size());

	}

	@Override
	public List<SnomedProblemDto> findAll() {
		return Collections.emptyList();
	}

	@Override
	public List<SnomedProblemDto> findAllById(List<Long> ids) {
		if (!ids.isEmpty()) {
			List<SnomedProcedureDto> result = new ArrayList<>();
			if (featureFlagsService.isOn(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS)) {
				return vSnomedGroupConceptRepository.findAllById(ids.stream().map(Long::intValue).collect(Collectors.toList()))
						.stream()
						.map(this::mapToDto)
						.collect(Collectors.toList());
			} else
				return backofficeSnowstormStore.findAllById(ids).stream().map(this::mapToDto).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	public Optional<SnomedProblemDto> findById(Long id) {
		return Optional.empty();
	}

	@Override
	public SnomedProblemDto save(SnomedProblemDto entity) {
		return null;
	}

	@Override
	public void deleteById(Long id) {

	}
	@Override
	public Example<SnomedProblemDto> buildExample(SnomedProblemDto entity) {
		ExampleMatcher matcher = ExampleMatcher
				.matching()
				.withMatcher("conceptPt", x -> x.ignoreCase().contains())
				;
		return Example.of(entity, matcher);
	}

	private Example<VSnomedGroupConcept> buildExampleEntity(SnomedProblemDto entity){
		VSnomedGroupConcept exampleEntity = new VSnomedGroupConcept();
		exampleEntity.setConceptPt(entity.getConceptPt());
		exampleEntity.setGroupId(entity.getGroupId());
		ExampleMatcher matcher = ExampleMatcher
				.matching()
				.withMatcher("conceptPt", x -> x.ignoreCase().contains())
				;
		return Example.of(exampleEntity, matcher);
	}

	private SnomedProblemDto mapToDto (VSnomedGroupConcept concept){
		SnomedProblemDto result = new SnomedProblemDto();
		result.setId(concept.getId().longValue());
		result.setConceptPt(concept.getConceptPt());
		result.setConceptId(concept.getConceptId().longValue());
		result.setGroupId(concept.getGroupId());
		return result;
	}

	private SnomedProblemDto mapToDto (BackofficeSnowstormDto concept){
		SnomedProblemDto result = new SnomedProblemDto();
		result.setId(concept.getId());
		result.setConceptPt(concept.getTerm());
		result.setConceptId(Long.parseLong(concept.getConceptId()));
		return result;
	}

}
