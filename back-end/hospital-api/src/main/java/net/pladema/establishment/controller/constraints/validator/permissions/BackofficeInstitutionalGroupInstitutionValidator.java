package net.pladema.establishment.controller.constraints.validator.permissions;

import net.pladema.establishment.controller.dto.InstitutionalGroupInstitutionDto;
import net.pladema.establishment.repository.InstitutionalGroupInstitutionRepository;

import net.pladema.sgx.backoffice.validation.BackofficeEntityValidator;

import net.pladema.sgx.exceptions.BackofficeValidationException;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
public class BackofficeInstitutionalGroupInstitutionValidator implements BackofficeEntityValidator<InstitutionalGroupInstitutionDto, Integer> {


	private final InstitutionalGroupInstitutionRepository institutionalGroupInstitutionRepository;

	public BackofficeInstitutionalGroupInstitutionValidator(InstitutionalGroupInstitutionRepository institutionalGroupInstitutionRepository) {
		this.institutionalGroupInstitutionRepository = institutionalGroupInstitutionRepository;
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ADMINISTRADOR_DE_ACCESO_DOMINIO')")
	public void assertCreate(InstitutionalGroupInstitutionDto entity) {
		if (entity.getInstitutionalGroupId() == null || entity.getInstitutionId() == null)
			throw new BackofficeValidationException("institutional-group-institution.invalid.format");
		if (institutionalGroupInstitutionRepository.existsByInstitutionId(entity.getInstitutionId()))
			throw new BackofficeValidationException("institutional-group-institution.exists");
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ADMINISTRADOR_DE_ACCESO_DOMINIO')")
	public void assertUpdate(Integer id, InstitutionalGroupInstitutionDto entity) {}

	@Override
	@PreAuthorize("hasAnyAuthority('ADMINISTRADOR_DE_ACCESO_DOMINIO')")
	public void assertDelete(Integer id) {}

}
