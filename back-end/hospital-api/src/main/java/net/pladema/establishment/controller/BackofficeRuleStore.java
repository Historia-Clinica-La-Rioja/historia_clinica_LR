package net.pladema.establishment.controller;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.SnomedRepository;
import net.pladema.establishment.controller.dto.ERuleType;
import net.pladema.establishment.controller.dto.RuleDto;
import net.pladema.establishment.repository.RuleRepository;
import net.pladema.establishment.repository.entity.Rule;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import net.pladema.snowstorm.repository.SnomedRelatedGroupRepository;
import net.pladema.staff.repository.ClinicalSpecialtyRepository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BackofficeRuleStore implements BackofficeStore <RuleDto, Integer> {

	private final RuleRepository ruleRepository;
	private final ClinicalSpecialtyRepository clinicalSpecialtyRepository;
	private final SnomedRepository snomedRepository;
	private final SnomedRelatedGroupRepository snomedRelatedGroupRepository;

	public BackofficeRuleStore (RuleRepository ruleRepository,
								ClinicalSpecialtyRepository clinicalSpecialtyRepository,
								SnomedRepository snomedRepository,
								SnomedRelatedGroupRepository snomedRelatedGroupRepository) {
		this.ruleRepository = ruleRepository;
		this.clinicalSpecialtyRepository = clinicalSpecialtyRepository;
		this.snomedRepository = snomedRepository;
		this.snomedRelatedGroupRepository = snomedRelatedGroupRepository;
	}
	@Override
	public Page<RuleDto> findAll(RuleDto example, Pageable pageable) {
		List<RuleDto> rules = ruleRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
		if (pageable.getSort().getOrderFor("name") != null && pageable.getSort().getOrderFor("name").isDescending()) {
			rules = rules.stream().sorted(Comparator.comparing(ruleDto -> ruleDto.getName().toLowerCase(), Comparator.reverseOrder())).collect(Collectors.toList());
		} else
			rules = rules.stream().sorted(Comparator.comparing(ruleDto -> ruleDto.getName().toLowerCase())).collect(Collectors.toList());
		int totalElements = rules.size();
		List<RuleDto> resultList = rules.subList(pageable.getPageNumber() * pageable.getPageSize(), Math.min((pageable.getPageNumber() + 1) * pageable.getPageSize(), rules.size()));
		return new PageImpl<>(resultList, pageable, totalElements);
	}

	@Override
	public List<RuleDto> findAll() {
		return ruleRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
	}

	@Override
	public List<RuleDto> findAllById(List<Integer> ids) {
		return ruleRepository.findAllById(ids).stream().map(this::mapToDto).collect(Collectors.toList());
	}

	@Override
	public Optional<RuleDto> findById(Integer id) {
		return ruleRepository.findById(id).map(this::mapToDto);
	}

	@Override
	public RuleDto save(RuleDto entity) {
		entity.setTypeId(entity.getClinicalSpecialtyId() != null ? ERuleType.CLINICAL_SPECIALTY.getId() : ERuleType.PRACTICE_PROCEDURE.getId());
		if (entity.getSnomedId() != null){
			entity.setSnomedId(snomedRelatedGroupRepository.getSnomedIdById(entity.getSnomedId()).orElse(null));
		}
		entity.setId(ruleRepository.save(new Rule(entity)).getId());
		return entity;
	}

	@Override
	public void deleteById(Integer id) {
		ruleRepository.deleteById(id);
	}

	@Override
	public Example<RuleDto> buildExample(RuleDto entity) {
		return Example.of(entity);
	}

	private RuleDto mapToDto(Rule entity){
		RuleDto result = new RuleDto();
		result.setId(entity.getId());
		result.setTypeId(entity.getTypeId());
		result.setClinicalSpecialtyId(entity.getClinicalSpecialtyId());
		result.setSnomedId(entity.getSnomedId());
		String name = entity.getTypeId().equals(ERuleType.CLINICAL_SPECIALTY.getId()) ? clinicalSpecialtyRepository.getById(entity.getClinicalSpecialtyId()).getName() : snomedRepository.getById(entity.getSnomedId()).getPt();
		result.setName(name);
		return result;
	}

}
