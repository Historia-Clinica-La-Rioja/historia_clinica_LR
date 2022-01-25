package net.pladema.patient.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.patient.repository.IdentityVerificationStatusRepository;
import net.pladema.patient.repository.entity.IdentityVerificationStatus;
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

	public PatientMasterDataController(IdentityVerificationStatusRepository identityVerificationStatusRepository) {
		this.identityVerificationStatusRepository = identityVerificationStatusRepository;
	}


	@GetMapping(value = "/withoutIdentityReasons")
	public ResponseEntity<List<IdentityVerificationStatus>> getIdentityVerificationStatus(){
		List<IdentityVerificationStatus> result = identityVerificationStatusRepository.findAll();
		LOG.debug("Get all identityVerificationStatus -> {} ", result);
		return ResponseEntity.ok(result);
	}

}