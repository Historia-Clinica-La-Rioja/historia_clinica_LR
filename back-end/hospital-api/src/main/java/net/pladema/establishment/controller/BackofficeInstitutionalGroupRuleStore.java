package net.pladema.establishment.controller;

import net.pladema.establishment.controller.dto.ERuleLevel;
import net.pladema.establishment.controller.dto.InstitutionalGroupRuleDto;
import net.pladema.establishment.repository.InstitutionalGroupRuleRepository;
import net.pladema.establishment.repository.RuleRepository;
import net.pladema.establishment.repository.domain.InstitutionalGroupRuleVo;
import net.pladema.establishment.repository.entity.InstitutionalGroupRule;
import net.pladema.establishment.repository.entity.Rule;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import net.pladema.snowstorm.repository.SnomedRelatedGroupRepository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BackofficeInstitutionalGroupRuleStore implements BackofficeStore<InstitutionalGroupRuleDto, Integer> {

	private final InstitutionalGroupRuleRepository institutionalGroupRuleRepository;
	private final RuleRepository ruleRepository;
	private final SnomedRelatedGroupRepository snomedRelatedGroupRepository;

	public BackofficeInstitutionalGroupRuleStore(InstitutionalGroupRuleRepository institutionalGroupRuleRepository,
												 RuleRepository ruleRepository,
												 SnomedRelatedGroupRepository snomedRelatedGroupRepository){
		this.institutionalGroupRuleRepository = institutionalGroupRuleRepository;
		this.ruleRepository = ruleRepository;
		this.snomedRelatedGroupRepository = snomedRelatedGroupRepository;
	}

	@Override
	public Page<InstitutionalGroupRuleDto> findAll(InstitutionalGroupRuleDto example, Pageable pageable) {
		if (example.getInstitutionalGroupId() == null)
			return new PageImpl<>(Collections.emptyList(), pageable, 0);
		List<InstitutionalGroupRuleDto> result = institutionalGroupRuleRepository.findAllByInstitutionalGroupId(example.getInstitutionalGroupId())
				.stream()
				.map(this::mapToDto)
				.collect(Collectors.toList());
		int minIndex = pageable.getPageNumber() * pageable.getPageSize();
		int maxIndex = minIndex + pageable.getPageSize();
		return new PageImpl<>(result.subList(minIndex, Math.min(maxIndex, result.size())), pageable, result.size());
	}

	@Override
	public List<InstitutionalGroupRuleDto> findAll() {
		return Collections.emptyList();
	}

	@Override
	public List<InstitutionalGroupRuleDto> findAllById(List<Integer> ids) {
		return Collections.emptyList();
	}

	@Override
	public Optional<InstitutionalGroupRuleDto> findById(Integer id) {
		return institutionalGroupRuleRepository.findVoById(id).map(this::mapToDto);
	}

	@Override
	public InstitutionalGroupRuleDto save(InstitutionalGroupRuleDto entity) {
		if (entity.getId() != null){
			institutionalGroupRuleRepository.findById(entity.getId()).ifPresent(institutionalGroupRule -> {
				institutionalGroupRule.setRegulated(entity.isRegulated());
				institutionalGroupRule.setComment(entity.getComment());
				institutionalGroupRuleRepository.save(institutionalGroupRule);
			});
		} else {
			entity.setRuleId(saveRule(entity));
			entity.setRegulated(true);
			entity.setId(institutionalGroupRuleRepository.save(new InstitutionalGroupRule(entity)).getId());
		}
		return entity;
	}

	@Override
	public void deleteById(Integer id) {
		institutionalGroupRuleRepository.deleteById(id);
	}

	@Override
	public Example<InstitutionalGroupRuleDto> buildExample(InstitutionalGroupRuleDto entity) {
		return Example.of(entity);
	}

	private Integer saveRule(InstitutionalGroupRuleDto dto){
		Rule toSave = new Rule();
		toSave.setLevel(ERuleLevel.LOCAL.getId());
		toSave.setClinicalSpecialtyId(dto.getClinicalSpecialtyId());
		toSave.setSnomedId(dto.getSnomedId());
		return ruleRepository.save(toSave).getId();
	}

	private InstitutionalGroupRuleDto mapToDto (InstitutionalGroupRuleVo vo){
		InstitutionalGroupRuleDto result = new InstitutionalGroupRuleDto();
		result.setId(vo.getId());
		result.setRuleId(vo.getRuleId());
		result.setInstitutionalGroupId(vo.getInstitutionalGroupId());
		result.setComment(vo.getComment());
		result.setRuleLevel(ERuleLevel.map(vo.getRuleLevel()).getValue());
		result.setRuleName(vo.getClinicalSpecialtyName() != null ? vo.getClinicalSpecialtyName() : vo.getConceptPt());
		result.setRegulated(vo.isRegulated());
		return result;
	}

}
