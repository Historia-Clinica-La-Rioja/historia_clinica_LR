package net.pladema.establishment.controller.constraints.validator.permissions;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.SnomedRepository;
import net.pladema.establishment.controller.dto.ERuleLevel;
import net.pladema.establishment.controller.dto.RuleDto;
import net.pladema.establishment.repository.RuleRepository;
import net.pladema.sgx.backoffice.rest.BackofficeEntityValidatorAdapter;

import net.pladema.sgx.exceptions.BackofficeValidationException;
import net.pladema.snowstorm.repository.SnomedRelatedGroupRepository;
import net.pladema.staff.repository.ClinicalSpecialtyRepository;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
public class BackofficeRuleValidator extends BackofficeEntityValidatorAdapter<RuleDto, Integer> {

	private final ClinicalSpecialtyRepository clinicalSpecialtyRepository;
	private final SnomedRepository snomedRepository;
	private final SnomedRelatedGroupRepository snomedRelatedGroupRepository;
	private final RuleRepository ruleRepository;

	public BackofficeRuleValidator(ClinicalSpecialtyRepository clinicalSpecialtyRepository,
								   SnomedRepository snomedRepository,
								   SnomedRelatedGroupRepository snomedRelatedGroupRepository,
								   RuleRepository ruleRepository)
	{
		this.clinicalSpecialtyRepository = clinicalSpecialtyRepository;
		this.snomedRepository = snomedRepository;
		this.snomedRelatedGroupRepository = snomedRelatedGroupRepository;
		this.ruleRepository = ruleRepository;
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ADMINISTRADOR_DE_ACCESO_DOMINIO')")
	public void assertCreate(RuleDto entity) {
		assertValidRule(entity);
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ADMINISTRADOR_DE_ACCESO_DOMINIO')")
	public void assertUpdate(Integer id, RuleDto entity) {
		assertValidRule(entity);
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ADMINISTRADOR_DE_ACCESO_DOMINIO')")
	public void assertDelete(Integer id) {
		// Do nothing
	}

	private void assertValidRule (RuleDto entity){
		if (entity.getClinicalSpecialtyId() == null && entity.getSnomedId() == null){
			throw new BackofficeValidationException("invalid.rule.format");
		}
		if (entity.getClinicalSpecialtyId() != null && ruleRepository.existsByClinicalSpecialtyIdAndLevel(entity.getClinicalSpecialtyId(), ERuleLevel.GENERAL.getId())) {
			throw new BackofficeValidationException("rule.clinical-specialty.exists");
		}
		if (entity.getSnomedId() != null && ruleRepository.existsBySnomedIdAndLevel(snomedRelatedGroupRepository.getSnomedIdById(entity.getSnomedId()).orElse(null), ERuleLevel.GENERAL.getId())) {
			throw new BackofficeValidationException("rule.snomed.exists");
		}
	}

}
