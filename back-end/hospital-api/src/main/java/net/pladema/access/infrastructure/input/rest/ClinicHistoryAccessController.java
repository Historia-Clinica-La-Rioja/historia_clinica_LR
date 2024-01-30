package net.pladema.access.infrastructure.input.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.access.application.CreateClinicHistoryAccess;

import net.pladema.access.application.GetIsValidAccess;
import net.pladema.access.domain.bo.ClinicHistoryAccessBo;
import net.pladema.access.infrastructure.input.dto.ClinicHistoryAccessDto;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@Slf4j
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ENFERMERO_ADULTO_MAYOR, ENFERMERO, ADMINISTRADOR_DE_CAMAS, PERSONAL_DE_IMAGENES, PERSONAL_DE_LABORATORIO, PERSONAL_DE_FARMACIA, PRESCRIPTOR, ABORDAJE_VIOLENCIAS')")
@RequestMapping("/institutions/{institutionId}/patient/{patientId}/clinic-history-access")
@RestController
public class ClinicHistoryAccessController {

	private final static String OUTPUT = "Output -> {}";

	private final GetIsValidAccess getIsValidAccess;

	private final CreateClinicHistoryAccess createClinicHistoryAccess;

	@GetMapping
	public Boolean isValid(@PathVariable(name = "institutionId") Integer institutionId,
						   @PathVariable(name = "patientId") Integer patientId) {
		log.debug("Input parameters -> institutionId {}, patientId {}", institutionId, patientId);
		boolean isValid = getIsValidAccess.run(institutionId, patientId);
		log.debug(OUTPUT, isValid);
		return isValid;
	}

	@PostMapping
	public void saveAudit(@PathVariable(name = "institutionId") Integer institutionId,
						  @PathVariable(name = "patientId") Integer patientId,
						  @RequestBody ClinicHistoryAccessDto clinicHistoryAccess) {
		log.debug("Input parameters -> institutionId {}, patientId {}, clinicHistoryAccess {}", institutionId, patientId, clinicHistoryAccess);
		ClinicHistoryAccessBo clinicHistoryAccessBo = new ClinicHistoryAccessBo(clinicHistoryAccess);
		createClinicHistoryAccess.run(institutionId, patientId, clinicHistoryAccessBo);
	}
}
