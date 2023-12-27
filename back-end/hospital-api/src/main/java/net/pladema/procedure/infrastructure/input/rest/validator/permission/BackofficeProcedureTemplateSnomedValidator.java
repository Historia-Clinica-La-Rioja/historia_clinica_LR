package net.pladema.procedure.infrastructure.input.rest.validator.permission;

import lombok.AllArgsConstructor;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.procedure.infrastructure.input.rest.dto.ProcedureTemplateDto;
import net.pladema.procedure.infrastructure.input.rest.dto.SnomedPracticeDto;
import net.pladema.procedure.infrastructure.output.repository.ProcedureTemplateRepository;
import net.pladema.procedure.infrastructure.output.repository.ProcedureTemplateSnomedRepository;
import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureTemplate;
import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureTemplateSnomedPK;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;
import net.pladema.sgx.backoffice.rest.ItemsAllowed;
import net.pladema.sgx.exceptions.BackofficeValidationException;
import net.pladema.user.controller.BackofficeAuthoritiesValidator;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@AllArgsConstructor
public class BackofficeProcedureTemplateSnomedValidator implements BackofficePermissionValidator<ProcedureTemplateDto, Integer> {

	private final BackofficeAuthoritiesValidator authoritiesValidator;

	private ProcedureTemplateSnomedRepository repository;

	private ProcedureTemplateRepository procedureTemplateRepository;

	@Override
	public void assertGetList(ProcedureTemplateDto entity) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return;
	}

	@Override
	public List<Integer> filterIdsByPermission(List<Integer> ids) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return ids;
		return List.of();
	}

	@Override
	public void assertGetOne(Integer id) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return;
	}

	@Override
	public void assertCreate(ProcedureTemplateDto entity) {
		assertNotExists(entity);
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return;
	}

	@Override
	public void assertUpdate(Integer id, ProcedureTemplateDto entity) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return;
	}

	@Override
	public void assertDelete(Integer id) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return;

	}

	@Override
	public ItemsAllowed<Integer> itemsAllowedToList(ProcedureTemplateDto entity) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return new ItemsAllowed<>();
		return new ItemsAllowed<>(false, Collections.emptyList());
	}

	@Override
	public ItemsAllowed<Integer> itemsAllowedToList() {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return new ItemsAllowed<>();
		return new ItemsAllowed<>(false, Collections.emptyList());
	}

	private void assertNotExists(ProcedureTemplateDto procedureTemplate) {
		if (!procedureTemplateRepository.existsById(procedureTemplate.getId()))
			throw new BackofficeValidationException("No existe el procedureTemplate con id: " + procedureTemplate.getId());
		var practices = procedureTemplate.getAssociatedPractices();
		if (practices != null) for (SnomedPracticeDto practice : practices) {
			if (repository.existsById(new ProcedureTemplateSnomedPK(procedureTemplate.getId(),practice.getId()))) {
				throw new BackofficeValidationException("La práctica seleccionada ya pertenece al estudio");
			}
		}
	}
}
