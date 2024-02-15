package net.pladema.establishment.controller.constraints.validator.permissions;

import net.pladema.establishment.controller.dto.InstitutionalGroupDto;
import net.pladema.establishment.repository.InstitutionalGroupRepository;

import net.pladema.sgx.backoffice.validation.BackofficeEntityValidator;

import net.pladema.sgx.exceptions.BackofficeValidationException;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
public class BackofficeInstitutionalGroupValidator implements BackofficeEntityValidator<InstitutionalGroupDto, Integer> {

	private final InstitutionalGroupRepository institutionalGroupRepository;

	public BackofficeInstitutionalGroupValidator(InstitutionalGroupRepository institutionalGroupRepository){
		this.institutionalGroupRepository = institutionalGroupRepository;
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ADMINISTRADOR_DE_ACCESO_DOMINIO')")
	public void assertCreate(InstitutionalGroupDto entity) {
		assertValidFields(entity);
		assertGroupNotExists(entity);
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ADMINISTRADOR_DE_ACCESO_DOMINIO')")
	public void assertUpdate(Integer id, InstitutionalGroupDto entity) {
		var institutionalGroup = institutionalGroupRepository.findById(entity.getId());
		institutionalGroup.ifPresent(ig -> {
			if (!ig.getTypeId().equals(entity.getTypeId()) || !ig.getName().equals(entity.getName())){
				assertGroupNotExists(entity);
			}
		});
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ADMINISTRADOR_DE_ACCESO_DOMINIO')")
	public void assertDelete(Integer id) {
		if(institutionalGroupRepository.existsUsersByGroupId(id)){
			throw new BackofficeValidationException("institutional-group.have.users.related");
		}
	}

	public void assertValidFields (InstitutionalGroupDto entity){
		if (entity.getTypeId() == null || entity.getName() == null)
			throw new BackofficeValidationException("institutional-group.invalid.format");
	}

	public void assertGroupNotExists(InstitutionalGroupDto entity){
		if (institutionalGroupRepository.existsByTypeIdAndName(entity.getTypeId(), entity.getName()))
			throw new BackofficeValidationException("institutional-group.exists");
	}
}
