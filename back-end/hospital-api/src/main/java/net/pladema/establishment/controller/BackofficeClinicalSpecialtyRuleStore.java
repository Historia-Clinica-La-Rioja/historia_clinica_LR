package net.pladema.establishment.controller;

import net.pladema.establishment.controller.dto.ERuleLevel;
import net.pladema.establishment.repository.RuleRepository;
import net.pladema.establishment.repository.entity.Rule;
import net.pladema.sgx.backoffice.repository.BackofficeStore;
import net.pladema.staff.repository.ClinicalSpecialtyRepository;
import net.pladema.staff.repository.entity.ClinicalSpecialty;

import net.pladema.staff.repository.entity.ClinicalSpecialtyType;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BackofficeClinicalSpecialtyRuleStore implements BackofficeStore<ClinicalSpecialty, Integer> {
	private final ClinicalSpecialtyRepository clinicalSpecialtyRepository;
	private final RuleRepository ruleRepository;

	public BackofficeClinicalSpecialtyRuleStore(ClinicalSpecialtyRepository clinicalSpecialtyRepository, RuleRepository ruleRepository){
		super();
		this.clinicalSpecialtyRepository = clinicalSpecialtyRepository;
		this.ruleRepository = ruleRepository;
	}

	@Override
	public Page<ClinicalSpecialty> findAll(ClinicalSpecialty clinicalSpecialty, Pageable pageable) {
		clinicalSpecialty.setClinicalSpecialtyTypeId(ClinicalSpecialtyType.Specialty);
		if(clinicalSpecialty.withoutName())
			clinicalSpecialty.setName(null);
		List<Integer> existingRulesSpecialtyIds = ruleRepository.findAll().stream().filter(rule -> rule.getLevel().equals(ERuleLevel.GENERAL.getId())).map(Rule::getClinicalSpecialtyId).filter(Objects::nonNull).collect(Collectors.toList());
		List<ClinicalSpecialty> specialties = clinicalSpecialtyRepository.findAll(
				buildExample(clinicalSpecialty),
				PageRequest.of(
						pageable.getPageNumber(),
						100,
						pageable.getSort()
				)
		).stream().filter(specialty -> !existingRulesSpecialtyIds.contains(specialty.getId())).collect(Collectors.toList());
		int maxIndex = Math.min(specialties.size(), pageable.getPageSize());
		return new PageImpl<>(specialties.subList(0, maxIndex), pageable, maxIndex);
	}

	@Override
	public List<ClinicalSpecialty> findAll() {
		return Collections.emptyList();
	}

	@Override
	public List<ClinicalSpecialty> findAllById(List<Integer> ids) {
		List<ClinicalSpecialty> specialties = clinicalSpecialtyRepository.findAllById(ids);
		specialties.forEach(ClinicalSpecialty::fixSpecialtyType);
		specialties.sort(Comparator.comparing(ClinicalSpecialty::getName, String::compareTo));
		return specialties;
	}

	@Override
	public Optional<ClinicalSpecialty> findById(Integer id) {
		return Optional.empty();
	}

	@Override
	public ClinicalSpecialty save(ClinicalSpecialty entity) {
		return entity;
	}

	@Override
	public void deleteById(Integer id) {}

	@Override
	public Example<ClinicalSpecialty> buildExample(ClinicalSpecialty entity) {
		ExampleMatcher matcher = ExampleMatcher
				.matching()
				.withIgnoreCase()
				.withMatcher("name", ExampleMatcher.GenericPropertyMatcher::contains)
				.withMatcher("sctidCode", ExampleMatcher.GenericPropertyMatcher::contains);
		return Example.of(entity, matcher);
	}

}
