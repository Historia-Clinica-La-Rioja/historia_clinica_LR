package net.pladema.establishment.controller;

import com.google.common.base.Joiner;

import net.pladema.establishment.controller.dto.ERuleLevel;
import net.pladema.establishment.controller.dto.InstitutionalGroupDto;
import net.pladema.establishment.repository.InstitutionalGroupInstitutionRepository;
import net.pladema.establishment.repository.InstitutionalGroupRepository;
import net.pladema.establishment.repository.InstitutionalGroupRuleRepository;
import net.pladema.establishment.repository.RuleRepository;
import net.pladema.establishment.repository.entity.InstitutionalGroup;
import net.pladema.establishment.repository.entity.InstitutionalGroupRule;
import net.pladema.establishment.repository.entity.Rule;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BackofficeInstitutionalGroupStore implements BackofficeStore<InstitutionalGroupDto, Integer> {

	private final InstitutionalGroupRepository repository;
	private final InstitutionalGroupInstitutionRepository institutionsGroupInstitutionRepository;
	private final InstitutionalGroupRuleRepository institutionalGroupRuleRepository;
	private final RuleRepository ruleRepository;

	public BackofficeInstitutionalGroupStore(InstitutionalGroupRepository repository,
											 InstitutionalGroupInstitutionRepository institutionsGroupInstitutionRepository,
											 InstitutionalGroupRuleRepository institutionalGroupRuleRepository,
											 RuleRepository ruleRepository){
		this.repository = repository;
		this.institutionsGroupInstitutionRepository = institutionsGroupInstitutionRepository;
		this.institutionalGroupRuleRepository = institutionalGroupRuleRepository;
		this.ruleRepository = ruleRepository;
	}

	@Override
	public Page<InstitutionalGroupDto> findAll(InstitutionalGroupDto example, Pageable pageable) {
		ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		List<InstitutionalGroupDto> list = repository.findAll(Example.of(new InstitutionalGroup(example), customExampleMatcher), pageable)
				.map(this::mapToDto)
				.toList();
		long totalElements = repository.count();
		return new PageImpl<>(list, pageable, list.isEmpty() ? 0 : totalElements);
	}

	@Override
	public List<InstitutionalGroupDto> findAll() {
		return repository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
	}

	@Override
	public List<InstitutionalGroupDto> findAllById(List<Integer> ids) {
		return repository.findAllById(ids).stream().map(this::mapToDto).collect(Collectors.toList());
	}

	@Override
	public Optional<InstitutionalGroupDto> findById(Integer id) {
		return repository.findById(id).map(this::mapToDto);
	}

	@Override
	public InstitutionalGroupDto save(InstitutionalGroupDto entity) {
		if(entity.getId() != null) {
			repository.findById(entity.getId()).ifPresent(group -> {
				group.setName(entity.getName());
				group.setTypeId(entity.getTypeId());
				repository.save(group);
			});
		} else {
			InstitutionalGroup entityToSave = new InstitutionalGroup(entity);
			entity.setId(repository.save(entityToSave).getId());
			List<Integer> generalRuleIds = ruleRepository.findAll().stream().filter(rule -> rule.getLevel().equals(ERuleLevel.GENERAL.getId())).map(Rule::getId).collect(Collectors.toList());
			addGeneralRulesToGroup(generalRuleIds, entity.getId());
		}
		return entity;
	}

	@Override
	public void deleteById(Integer id) {
		institutionalGroupRuleRepository.deleteByInstitutionalGroupId(id);
		institutionsGroupInstitutionRepository.deleteByInstitutionalGroupId(id);
		repository.deleteById(id);
	}

	@Override
	public Example<InstitutionalGroupDto> buildExample(InstitutionalGroupDto entity) {
		return Example.of(entity);
	}

	private InstitutionalGroupDto mapToDto (InstitutionalGroup entity){
		InstitutionalGroupDto result = new InstitutionalGroupDto();
		result.setId(entity.getId());
		result.setName(entity.getName());
		result.setTypeId(entity.getTypeId());
		result.setInstitutions(getInstitutions(entity));
		return result;
	}

	private String getInstitutions(InstitutionalGroup entity){
		List<String> institutionNames = repository.getInstitutionsNamesById(entity.getId());
		String institutions = Joiner.on(", ").join(institutionNames);
		if (institutions.length() > 150){
			institutions = institutions.substring(0,150).concat("...");
		}
		return institutions;
	}

	private void addGeneralRulesToGroup (List<Integer> generalRuleIds, Integer institutionalGroupId){
		List<InstitutionalGroupRule> result = new ArrayList<>();
		generalRuleIds.forEach(ruleId -> {
			InstitutionalGroupRule localRule = new InstitutionalGroupRule(null, ruleId, institutionalGroupId, true, null);
			result.add(localRule);
		});
		institutionalGroupRuleRepository.saveAll(result);
	}

}
