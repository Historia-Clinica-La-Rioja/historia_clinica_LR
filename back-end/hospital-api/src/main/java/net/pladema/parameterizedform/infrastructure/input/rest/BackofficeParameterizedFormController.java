package net.pladema.parameterizedform.infrastructure.input.rest;

import net.pladema.parameterizedform.application.GetFormsByStatus;
import net.pladema.parameterizedform.application.UpdateFormStatus;
import net.pladema.parameterizedform.domain.enums.EFormStatus;
import net.pladema.parameterizedform.infrastructure.input.rest.constraints.validator.BackofficeParameterizedFormValidator;
import net.pladema.parameterizedform.infrastructure.output.BackofficeParameterizedFormStore;
import net.pladema.parameterizedform.infrastructure.output.repository.entity.ParameterizedForm;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("backoffice/parameterizedform")
@RestController
public class BackofficeParameterizedFormController extends AbstractBackofficeController<ParameterizedForm, Integer> {

	private final GetFormsByStatus getFormsByStatus;
	private final UpdateFormStatus updateFormStatus;

	public BackofficeParameterizedFormController(BackofficeParameterizedFormStore store,
												 BackofficeParameterizedFormValidator validator,
												 GetFormsByStatus getFormsByStatus,
												 UpdateFormStatus updateFormStatus) {
		super(store, validator);
		this.getFormsByStatus = getFormsByStatus;
		this.updateFormStatus = updateFormStatus;
	}

	@GetMapping(params="excludeInactive=true")
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR','ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public @ResponseBody Page<ParameterizedForm> getList(Pageable pageable,
											@RequestParam(name = "excludeInactive") Boolean excludeInactive,
											@RequestParam(name = "name", required = false) String name) {
		logger.debug("GET_LIST {}", "excludeInactive {}", excludeInactive);
		List<Short> statusIds = EFormStatus.getStatus();
		if (excludeInactive != null && excludeInactive) {
			statusIds = statusIds.stream().filter(statusId -> !statusId.equals(EFormStatus.INACTIVE.getId())).collect(Collectors.toList());
		}
		return getFormsByStatus.run(statusIds, name, pageable);
	}

	@PutMapping(value = "/{id}/update-status")
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR','ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	@ResponseStatus(HttpStatus.OK)
	public void updateStatus(@PathVariable Integer id) {
		updateFormStatus.run(id);
	}
}
