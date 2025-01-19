package net.pladema.establishment.controller;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.SnomedRepository;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedReferenceCounterReference;
import lombok.RequiredArgsConstructor;
import net.pladema.establishment.controller.dto.ERuleLevel;
import net.pladema.establishment.controller.dto.RuleDto;
import net.pladema.establishment.repository.InstitutionalGroupInstitutionRepository;
import net.pladema.establishment.repository.InstitutionalGroupRepository;
import net.pladema.establishment.repository.InstitutionalGroupRuleRepository;
import net.pladema.establishment.repository.RuleRepository;
import net.pladema.establishment.repository.entity.InstitutionalGroup;
import net.pladema.establishment.repository.entity.InstitutionalGroupRule;
import net.pladema.establishment.repository.entity.Rule;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import net.pladema.snowstorm.repository.SnomedRelatedGroupRepository;
import net.pladema.staff.repository.ClinicalSpecialtyRepository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BackofficeRuleStore implements BackofficeStore <RuleDto, Integer> {

	private final RuleRepository ruleRepository;
	private final ClinicalSpecialtyRepository clinicalSpecialtyRepository;
	private final SnomedRepository snomedRepository;
	private final SnomedRelatedGroupRepository snomedRelatedGroupRepository;
	private final InstitutionalGroupRepository institutionalGroupRepository;
	private final InstitutionalGroupRuleRepository institutionalGroupRuleRepository;
	private final InstitutionalGroupInstitutionRepository institutionalGroupInstitutionRepository;
	private final SharedReferenceCounterReference sharedReferenceCounterReference;

	@Override
	public Page<RuleDto> findAll(RuleDto example, Pageable pageable) {
		List<RuleDto> rules = ruleRepository.findAll().stream().filter(rule -> rule.getLevel().equals(ERuleLevel.GENERAL.getId())).map(this::mapToDto).collect(Collectors.toList());
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
		return ruleRepository.findAll().stream().filter(rule -> rule.getLevel().equals(ERuleLevel.GENERAL.getId())).map(this::mapToDto).collect(Collectors.toList());
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
		if (entity.getSnomedId() != null){
			entity.setSnomedId(snomedRelatedGroupRepository.getSnomedIdById(entity.getSnomedId()).orElse(null));
		}
		entity.setLevel(ERuleLevel.GENERAL.getId());
		entity.setId(ruleRepository.save(new Rule(entity)).getId());
		List<Integer> localRuleIds = ruleRepository.findByClinicalSpecialtyIdOrSnomedId(entity.getClinicalSpecialtyId(), entity.getSnomedId())
				.stream().filter(rule -> rule.getLevel().equals(ERuleLevel.LOCAL.getId())).map(Rule::getId).collect(Collectors.toList());
		institutionalGroupRuleRepository.deleteByRuleIds(localRuleIds);
		List<Integer> institutionalGroupsIds = institutionalGroupRepository.findAll().stream().map(InstitutionalGroup::getId).collect(Collectors.toList());
		addRuleToInstitutionalGroups(institutionalGroupsIds, entity.getId());
		sharedReferenceCounterReference.updateRuleOnReferences(entity.getId(), entity.getLevel(), localRuleIds);
		return entity;
	}

	@Override
	public void deleteById(Integer id) {
		sharedReferenceCounterReference.approveReferencesByRuleId(id, new ArrayList<>());
		institutionalGroupRuleRepository.deleteByRuleIds(List.of(id));
		ruleRepository.deleteById(id);
	}

	@Override
	public Example<RuleDto> buildExample(RuleDto entity) {
		return Example.of(entity);
	}

	private RuleDto mapToDto(Rule entity){
		RuleDto result = new RuleDto();
		result.setId(entity.getId());
		result.setClinicalSpecialtyId(entity.getClinicalSpecialtyId());
		result.setSnomedId(entity.getSnomedId());
		String ruleName = entity.getClinicalSpecialtyId() != null ? clinicalSpecialtyRepository.getById(entity.getClinicalSpecialtyId()).getName() : snomedRepository.getById(entity.getSnomedId()).getPt();
		result.setName(ruleName);
		return result;
	}

	private void addRuleToInstitutionalGroups(List<Integer> institutionalGroupIds, Integer ruleId){
		List<InstitutionalGroupRule> result = new ArrayList<>();
		institutionalGroupIds.forEach(institutionalGroupId -> {
			InstitutionalGroupRule localRule = new InstitutionalGroupRule(null, ruleId, institutionalGroupId, true, null);
			result.add(localRule);
		});
		institutionalGroupRuleRepository.saveAll(result);
	}

}
