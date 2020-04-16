package net.pladema.patient.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import net.pladema.patient.controller.dto.PatientSearchDto;
import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.controller.mapper.PatientMapper;
import net.pladema.patient.service.PatientService;
import net.pladema.patient.service.domain.PatientSearch;

@RestController
@RequestMapping("/patient")
@Api(value = "Patient", tags = { "Patient" })
public class PatientController {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	private final PatientService patientService;
	
	private final PatientMapper patientMapper;

	private final ObjectMapper jackson;
	
	public PatientController(PatientService patientService, ObjectMapper jackson, PatientMapper patientMapper) {
		this.patientService = patientService;
		this.jackson = jackson;
		this.patientMapper = patientMapper;
	}

	@RequestMapping(value = "/search")
	public ResponseEntity<List<PatientSearchDto>> searchPatient(@RequestParam String searchFilterStr){
		PatientSearchFilter searchFilter = null;
		try {
			searchFilter = jackson.readValue(searchFilterStr, PatientSearchFilter.class);
		} catch (IOException e) {
			LOG.error(String.format("Error mappeando filter: %s", searchFilterStr), e);
		}
		List<PatientSearch> resultado = patientService.searchPatient(searchFilter);
		
		return ResponseEntity.ok(patientMapper.fromListPatientSearch(resultado));
	}

}