package net.pladema.establishment.controller.constraints.validator.permissions;

import net.pladema.establishment.controller.dto.InstitutionalGroupRuleDto;
import net.pladema.establishment.repository.InstitutionalGroupRuleRepository;
import net.pladema.establishment.repository.RuleRepository;
import net.pladema.establishment.repository.entity.Rule;
import net.pladema.sgx.backoffice.validation.BackofficeEntityValidator;

import net.pladema.sgx.exceptions.BackofficeValidationException;

import net.pladema.snowstorm.repository.SnomedRelatedGroupRepository;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BackofficeInstitutionalGroupRuleValidator implements BackofficeEntityValidator<InstitutionalGroupRuleDto, Integer> {

	private final InstitutionalGroupRuleRepository institutionalGroupRuleRepository;
	private final RuleRepository ruleRepository;
	private final SnomedRelatedGroupRepository snomedRelatedGroupRepository;

	public BackofficeInstitutionalGroupRuleValidator(InstitutionalGroupRuleRepository institutionalGroupRuleRepository,
													 RuleRepository ruleRepository,
													 SnomedRelatedGroupRepository snomedRelatedGroupRepository)
	{
		this.institutionalGroupRuleRepository = institutionalGroupRuleRepository;
		this.ruleRepository = ruleRepository;
		this.snomedRelatedGroupRepository = snomedRelatedGroupRepository;
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ADMINISTRADOR_DE_ACCESO_DOMINIO')")
	public void assertCreate(InstitutionalGroupRuleDto entity) {
		if(entity.getSnomedId() != null) {
			entity.setSnomedId(snomedRelatedGroupRepository.getSnomedIdById(entity.getSnomedId()).orElse(null));
		}
		if (entity.getClinicalSpecialtyId() == null && entity.getSnomedId() == null){
			throw new BackofficeValidationException("rule.invalid.format");
		}
		List<Integer> ruleIds = ruleRepository.findByClinicalSpecialtyIdOrSnomedId(entity.getClinicalSpecialtyId(), entity.getSnomedId()).stream().map(Rule::getId).collect(Collectors.toList());
		if(institutionalGroupRuleRepository.existsByRuleIdsAndInstitutionalGroupId(ruleIds, entity.getInstitutionalGroupId())) {
			throw new BackofficeValidationException("institutional-group-rule.already.exists");
		}
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ADMINISTRADOR_DE_ACCESO_DOMINIO')")
	public void assertUpdate(Integer id, InstitutionalGroupRuleDto entity) {}

	@Override
	@PreAuthorize("hasAnyAuthority('ADMINISTRADOR_DE_ACCESO_DOMINIO')")
	public void assertDelete(Integer id) {}

}
