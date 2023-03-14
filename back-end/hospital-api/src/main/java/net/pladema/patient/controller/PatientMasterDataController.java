package net.pladema.patient.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.patient.repository.IdentityVerificationStatusRepository;
import net.pladema.patient.repository.PatientTypeRepository;
import net.pladema.patient.repository.entity.IdentityVerificationStatus;
import net.pladema.patient.repository.entity.PatientType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("masterdata/patient")
@Tag(name = "Patient master data", description = "Patient master data")
public class PatientMasterDataController {

	private static final Logger LOG = LoggerFactory.getLogger(PatientMasterDataController.class);

	private final IdentityVerificationStatusRepository identityVerificationStatusRepository;

	private final PatientTypeRepository patientTypeRepository;

	public PatientMasterDataController(IdentityVerificationStatusRepository identityVerificationStatusRepository, PatientTypeRepository patientTypeRepository) {
		this.identityVerificationStatusRepository = identityVerificationStatusRepository;
		this.patientTypeRepository = patientTypeRepository;
	}


	@GetMapping(value = "/withoutIdentityReasons")
	public ResponseEntity<List<IdentityVerificationStatus>> getIdentityVerificationStatus(){
		List<IdentityVerificationStatus> result = identityVerificationStatusRepository.findAll();
		LOG.debug("Get all identityVerificationStatus -> {} ", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping(value = "/types")
	public ResponseEntity<List<PatientType>> getTypes(){
		List<PatientType> result = patientTypeRepository.findAll();
		LOG.debug("Get all patient types -> {} ", result);
		return ResponseEntity.ok(result);
	}

}