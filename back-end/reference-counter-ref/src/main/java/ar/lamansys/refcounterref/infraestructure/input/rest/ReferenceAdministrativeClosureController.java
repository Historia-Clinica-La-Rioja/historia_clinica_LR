package ar.lamansys.refcounterref.infraestructure.input.rest;

import ar.lamansys.refcounterref.application.administrativeclosurereference.AdministrativeReferenceClosure;
import ar.lamansys.refcounterref.domain.counterreference.ReferenceAdministrativeClosureBo;

import ar.lamansys.refcounterref.infraestructure.input.rest.dto.counterreference.ReferenceAdministrativeClosureDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/institutions/{institutionId}/administrative-closure")
@Tag(name = "Reference Administrative Closure", description = "Reference Administrative Closure")
public class ReferenceAdministrativeClosureController {

	private final AdministrativeReferenceClosure administrativeReferenceClosure;

	@PostMapping
	@ResponseStatus(code = HttpStatus.OK)
	@PreAuthorize("hasPermission(#institutionId, 'GESTOR_DE_ACCESO_INSTITUCIONAL') || " +
			"hasAnyAuthority('GESTOR_DE_ACCESO_DE_DOMINIO', 'GESTOR_DE_ACCESO_REGIONAL', 'GESTOR_DE_ACCESO_LOCAL')")
	public boolean administrativeClosure(@PathVariable(name = "institutionId") Integer institutionId,
										 @RequestBody @Valid ReferenceAdministrativeClosureDto administrativeClosure) {
		log.debug("Input parameters -> institutionId {}, administrativeClosure {}", institutionId, administrativeClosure);
		var closure = new ReferenceAdministrativeClosureBo(institutionId, administrativeClosure.getReferenceId(), administrativeClosure.getClosureNote(), administrativeClosure.getFileIds());
		administrativeReferenceClosure.run(closure, institutionId);
		return true;
	}

}
