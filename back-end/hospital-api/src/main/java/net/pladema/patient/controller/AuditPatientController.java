package net.pladema.patient.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.patient.controller.dto.AuditPatientSearch;
import net.pladema.patient.controller.dto.DuplicatePatientDto;
import net.pladema.patient.controller.service.exception.AuditPatientException;
import net.pladema.patient.controller.service.exception.AuditPatientExceptionEnum;
import net.pladema.person.controller.service.PersonExternalService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("audit/institution/{institutionId}")
@Tag(name = "Audit patient", description = "Audit patient")
public class AuditPatientController {

	private final PersonExternalService personExternalService;
	private final ObjectMapper jackson;


	@GetMapping("/duplicate-patients-by-filter")
	@PreAuthorize("hasPermission(#institutionId, 'AUDITOR_MPI')")
	public ResponseEntity<List<DuplicatePatientDto>> getDuplicatePatientSearchFilter(
			@PathVariable(name = "institutionId") Integer institutionId,
			@RequestParam String searchFilterStr) {
		log.debug("institutionId {}, searchFilterStr {}", institutionId, searchFilterStr);
		AuditPatientSearch searchFilter = null;
		try {
			searchFilter = jackson.readValue(searchFilterStr, AuditPatientSearch.class);
		} catch (IOException e) {
			log.error(String.format("Error mappeando filter: %s", searchFilterStr), e);
		}
		validateFilter(searchFilter);
		List<DuplicatePatientDto> result = personExternalService.getDuplicatePersonsByFilter(searchFilter);
		return ResponseEntity.ok().body(result);
	}

	private void validateFilter(AuditPatientSearch auditPatientSearch) {
		if ((auditPatientSearch == null) || (!auditPatientSearch.getName() && !auditPatientSearch.getIdentify() && !auditPatientSearch.getBirthdate())) {
			throw new AuditPatientException(AuditPatientExceptionEnum.INVALID_FILTER_FOR_SEARCH,String.format("No se esta filtrando por ningún dato personal."));
		}
	}

}
