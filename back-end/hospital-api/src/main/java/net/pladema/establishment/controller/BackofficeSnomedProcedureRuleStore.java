package net.pladema.establishment.controller;

import net.pladema.establishment.controller.dto.ERuleLevel;
import net.pladema.establishment.controller.dto.SnomedProcedureDto;
import net.pladema.establishment.repository.RuleRepository;
import net.pladema.establishment.repository.entity.Rule;
import net.pladema.sgx.backoffice.repository.BackofficeStore;
import net.pladema.snowstorm.repository.SnomedGroupRepository;
import net.pladema.snowstorm.repository.SnomedRelatedGroupRepository;
import net.pladema.snowstorm.repository.VSnomedGroupConceptRepository;
import net.pladema.snowstorm.repository.entity.VSnomedGroupConcept;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BackofficeSnomedProcedureRuleStore implements BackofficeStore<SnomedProcedureDto, Long> {

	private final VSnomedGroupConceptRepository vSnomedGroupConceptRepository;
	private final RuleRepository ruleRepository;
	private final SnomedRelatedGroupRepository snomedRelatedGroupRepository;
	private final SnomedGroupRepository snomedGroupRepository;

	private final String GROUP_DESCRIPTION = "PROCEDURE";

	public BackofficeSnomedProcedureRuleStore(VSnomedGroupConceptRepository vSnomedGroupConceptRepository,
											  RuleRepository ruleRepository,
											  SnomedRelatedGroupRepository snomedRelatedGroupRepository,
											  SnomedGroupRepository snomedGroupRepository){
		super();
		this.vSnomedGroupConceptRepository = vSnomedGroupConceptRepository;
		this.ruleRepository = ruleRepository;
		this.snomedRelatedGroupRepository = snomedRelatedGroupRepository;
		this.snomedGroupRepository = snomedGroupRepository;
	}

	@Override
	public Page<SnomedProcedureDto> findAll(SnomedProcedureDto example, Pageable pageable) {
		List<Integer> existingRulesPracticeProcedureIds = ruleRepository.findAll().stream().filter(rule -> rule.getLevel().equals(ERuleLevel.GENERAL.getId())).map(Rule::getSnomedId).filter(Objects::nonNull).collect(Collectors.toList());
		List<Integer> snomedRelatedGroupIds = snomedRelatedGroupRepository.getIdsBySnomedIds(existingRulesPracticeProcedureIds);
		var groupId = snomedGroupRepository.getIdByDescriptionAndInstitutionId(GROUP_DESCRIPTION, -1).orElse(null);
		VSnomedGroupConcept exampleEntity = new VSnomedGroupConcept(groupId, example.getConceptPt());
		List<SnomedProcedureDto> concepts = vSnomedGroupConceptRepository.findAll(
				buildExample(exampleEntity),
				PageRequest.of(
						pageable.getPageNumber(),
						100,
						pageable.getSort()
				)
		).stream().filter(concept -> !snomedRelatedGroupIds.contains(concept.getId())).map(this::mapToDto).collect(Collectors.toList());
		int maxIndex = Math.min(concepts.size(), pageable.getPageSize());
		return new PageImpl<>(concepts.subList(0, maxIndex), pageable, maxIndex);
	}

	@Override
	public List<SnomedProcedureDto> findAll() {
		return Collections.emptyList();
	}

	@Override
	public List<SnomedProcedureDto> findAllById(List<Long> ids) {
		return vSnomedGroupConceptRepository.findAllById(ids.stream().map(Long::intValue).collect(Collectors.toList()))
				.stream()
				.map(this::mapToDto)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<SnomedProcedureDto> findById(Long id) {
		return vSnomedGroupConceptRepository.findById(id.intValue()).map(this::mapToDto);
	}

	@Override
	public SnomedProcedureDto save(SnomedProcedureDto entity) {
		return null;
	}

	@Override
	public void deleteById(Long id) {}

	@Override
	public Example<SnomedProcedureDto> buildExample(SnomedProcedureDto entity) {
		ExampleMatcher matcher = ExampleMatcher
				.matching()
				.withMatcher("conceptPt", x -> x.ignoreCase().contains())
				;
		return Example.of(entity, matcher);
	}

	private Example<VSnomedGroupConcept> buildExample(VSnomedGroupConcept entity){
		ExampleMatcher matcher = ExampleMatcher
				.matching()
				.withMatcher("conceptPt", x -> x.ignoreCase().contains())
				;
		return Example.of(entity, matcher);
	}

	private SnomedProcedureDto mapToDto (VSnomedGroupConcept entity){
		SnomedProcedureDto result = new SnomedProcedureDto();
		result.setId(entity.getId().longValue());
		result.setConceptPt(entity.getConceptPt());
		result.setConceptId(entity.getConceptId().longValue());
		result.setGroupId(entity.getGroupId());
		return result;
	}

}
