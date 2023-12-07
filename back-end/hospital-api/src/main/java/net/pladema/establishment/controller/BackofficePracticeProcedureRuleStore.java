package net.pladema.establishment.controller;

import net.pladema.establishment.repository.RuleRepository;
import net.pladema.establishment.repository.entity.Rule;
import net.pladema.sgx.backoffice.repository.BackofficeStore;
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
public class BackofficePracticeProcedureRuleStore implements BackofficeStore<VSnomedGroupConcept, Integer> {

	private final VSnomedGroupConceptRepository vSnomedGroupConceptRepository;
	private final RuleRepository ruleRepository;
	private final SnomedRelatedGroupRepository snomedRelatedGroupRepository;

	public BackofficePracticeProcedureRuleStore(VSnomedGroupConceptRepository vSnomedGroupConceptRepository,
												RuleRepository ruleRepository,
												SnomedRelatedGroupRepository snomedRelatedGroupRepository){
		super();
		this.vSnomedGroupConceptRepository = vSnomedGroupConceptRepository;
		this.ruleRepository = ruleRepository;
		this.snomedRelatedGroupRepository = snomedRelatedGroupRepository;
	}

	@Override
	public Page<VSnomedGroupConcept> findAll(VSnomedGroupConcept example, Pageable pageable) {
		List<Integer> existingRulesPracticeProcedureIds = ruleRepository.findAll().stream().map(Rule::getSnomedId).filter(Objects::nonNull).collect(Collectors.toList());
		List<Integer> snomedRelatedGroupIds = snomedRelatedGroupRepository.getIdsBySnomedIds(existingRulesPracticeProcedureIds);
		List<VSnomedGroupConcept> concepts = vSnomedGroupConceptRepository.findAll(
				buildExample(example),
				PageRequest.of(
						pageable.getPageNumber(),
						100,
						pageable.getSort()
				)
		).stream().filter(concept -> !snomedRelatedGroupIds.contains(concept.getId())).collect(Collectors.toList());
		int maxIndex = Math.min(concepts.size(), pageable.getPageSize());
		return new PageImpl<>(concepts.subList(0, maxIndex), pageable, maxIndex);
	}

	@Override
	public List<VSnomedGroupConcept> findAll() {
		return Collections.emptyList();
	}

	@Override
	public List<VSnomedGroupConcept> findAllById(List<Integer> ids) {
		return vSnomedGroupConceptRepository.findAllById(ids);
	}

	@Override
	public Optional<VSnomedGroupConcept> findById(Integer id) {
		return Optional.empty();
	}

	@Override
	public VSnomedGroupConcept save(VSnomedGroupConcept entity) {
		return null;
	}

	@Override
	public void deleteById(Integer id) {}

	@Override
	public Example<VSnomedGroupConcept> buildExample(VSnomedGroupConcept entity) {
		ExampleMatcher matcher = ExampleMatcher
				.matching()
				.withMatcher("conceptPt", x -> x.ignoreCase().contains())
				;
		return Example.of(entity, matcher);
	}

}
