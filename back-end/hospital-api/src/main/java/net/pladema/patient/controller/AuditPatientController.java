package net.pladema.patient.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.patient.controller.dto.AuditPatientSearch;
import net.pladema.patient.controller.dto.DuplicatePatientDto;
import net.pladema.patient.controller.dto.PatientPersonalInfoDto;
import net.pladema.patient.controller.dto.PatientRegistrationSearchDto;
import net.pladema.patient.controller.dto.PatientRegistrationSearchFilter;
import net.pladema.patient.controller.mapper.PatientMapper;
import net.pladema.patient.controller.service.exception.AuditPatientException;
import net.pladema.patient.controller.service.exception.AuditPatientExceptionEnum;
import net.pladema.patient.service.PatientService;
import net.pladema.patient.service.domain.PatientRegistrationSearch;
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

	private final PatientService patientService;

	private final PatientMapper patientMapper;

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

	@GetMapping("/patients-personal-info")
	@PreAuthorize("hasPermission(#institutionId, 'AUDITOR_MPI')")
	public ResponseEntity<List<PatientPersonalInfoDto>> getPatientPersonalInfo(
			@PathVariable(name = "institutionId") Integer institutionId,
			@RequestParam String searchPatientInfoStr) {
		log.debug("institutionId {}, searchPatientInfoStr {}", institutionId, searchPatientInfoStr);
		DuplicatePatientDto duplicatePatientDto = null;
		try {
			duplicatePatientDto = jackson.readValue(searchPatientInfoStr, DuplicatePatientDto.class);
		} catch (IOException e) {
			log.error(String.format("Error mappeando filter: %s", searchPatientInfoStr), e);
		}
		List<PatientPersonalInfoDto> result = personExternalService.getPatientsPersonalInfo(duplicatePatientDto);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/search-registration-patients")
	@PreAuthorize("hasPermission(#institutionId, 'AUDITOR_MPI')")
	public  ResponseEntity<List<PatientRegistrationSearchDto>> searchRegistrationPatient(
			@PathVariable(name = "institutionId") Integer institutionId,
			@RequestParam String searchFilterStr) {
		log.debug("Input data -> institutionId {}, searchFilterStr {}", institutionId, searchFilterStr);
		PatientRegistrationSearchFilter searchFilter = null;
		try {
			searchFilter = jackson.readValue(searchFilterStr, PatientRegistrationSearchFilter.class);
		} catch (IOException e) {
			log.error(String.format("Error mappeando filter: %s", searchFilterStr), e);
		}
		List<PatientRegistrationSearch> result;
		if (searchFilter.getPatientId() != null)
			result = patientService.getPatientRegistrationById(searchFilter.getPatientId());
		else
			result = patientService.getPatientsRegistrationByFilter(searchFilter);
		return ResponseEntity.ok(patientMapper.fromListPatientRegistrationSearch(result));
	}

	private void validateFilter(AuditPatientSearch auditPatientSearch) {
		if ((auditPatientSearch == null) || (!auditPatientSearch.getName() && !auditPatientSearch.getIdentify() && !auditPatientSearch.getBirthdate())) {
			throw new AuditPatientException(AuditPatientExceptionEnum.INVALID_FILTER_FOR_SEARCH,String.format("No se esta filtrando por ningún dato personal."));
		}
	}

}
