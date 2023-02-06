package net.pladema.sipplus.infrastructure.input.rest;

import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.exception.RestTemplateApiException;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.sipplus.application.createmother.CreateMother;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/institution/{institutionId}/patient/{patientId}/sip-plus/mother")
@Tag(name = "Sip Plus Pregnancies", description = "Sip Plus Pregnancies")
@Slf4j
@RequiredArgsConstructor
@RestController
public class SipPlusMotherController {

	private final CreateMother createMother;

	@PostMapping(value = "/{pregnancy}")
	@ResponseStatus(code = HttpStatus.CREATED)
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	public void createMother(@PathVariable(name = "institutionId") Integer institutionId,
							   @PathVariable(name = "patientId") Integer patientId,
							   @PathVariable(name = "pregnancy") Integer pregnancy) throws RestTemplateApiException {
		log.debug("Input parameters -> institutionId {}, patientId {}, pregnancy {}", institutionId, patientId, pregnancy);
		createMother.run(patientId, pregnancy);
	}
}