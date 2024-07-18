package net.pladema.parameterizedform.infrastructure.input.rest;

import net.pladema.parameterizedform.UpdateFormEnablementInInstitution;
import net.pladema.parameterizedform.application.GetFormsByStatus;
import net.pladema.parameterizedform.application.UpdateFormStatus;
import net.pladema.parameterizedform.domain.enums.EFormStatus;
import net.pladema.parameterizedform.infrastructure.input.rest.dto.ParameterizedFormDto;
import net.pladema.parameterizedform.infrastructure.output.BackofficeParameterizedFormStore;
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

@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
@RequestMapping("backoffice/parameterizedform")
@RestController
public class BackofficeParameterizedFormController extends AbstractBackofficeController<ParameterizedFormDto, Integer> {

	private final GetFormsByStatus getFormsByStatus;
	private final UpdateFormStatus updateFormStatus;
	private final UpdateFormEnablementInInstitution updateFormEnablementInInstitution;

	public BackofficeParameterizedFormController(BackofficeParameterizedFormStore store,
												 GetFormsByStatus getFormsByStatus,
												 UpdateFormStatus updateFormStatus,
												 UpdateFormEnablementInInstitution updateFormEnablementInInstitution) {
		super(store);
		this.getFormsByStatus = getFormsByStatus;
		this.updateFormStatus = updateFormStatus;
		this.updateFormEnablementInInstitution = updateFormEnablementInInstitution;
	}

	@GetMapping(params="excludeInactive=true")
	public @ResponseBody Page<ParameterizedFormDto> getList(Pageable pageable,
														 @RequestParam(name = "excludeInactive") Boolean excludeInactive,
														 @RequestParam(name = "name", required = false) String name,
														 @RequestParam (name="isDomain") Boolean isDomain) {
		logger.debug("GET_LIST {}", "excludeInactive {}", excludeInactive);
		List<Short> statusIds = EFormStatus.getStatus();
		if (excludeInactive != null && excludeInactive) {
			statusIds = statusIds.stream().filter(statusId -> !statusId.equals(EFormStatus.INACTIVE.getId())).collect(Collectors.toList());
		}
		return getFormsByStatus.run(statusIds, name, isDomain, pageable);
	}

	@PutMapping(value = "/{id}/update-status")
	@ResponseStatus(HttpStatus.OK)
	public void updateStatus(@PathVariable Integer id) {
		logger.debug("updateStatus {}", "formId {}", id);
		updateFormStatus.run(id);
	}

	@PutMapping(value = "/{id}/update-institutional-enablement")
	@ResponseStatus(HttpStatus.OK)
	public void updateFormEnablementInInstitution(@PathVariable Integer id,
												  @RequestParam Integer institutionId,
												  @RequestParam Boolean enablement
	) {
		logger.debug("updateStatus {}", "formId {}, institutionId {}, enablement {}", id, institutionId, enablement);
		updateFormEnablementInInstitution.run(id, institutionId, enablement);
	}
}
