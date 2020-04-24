package net.pladema.patient.controller;

import io.swagger.annotations.Api;
import net.pladema.patient.repository.IdentityVerificationStatusRepository;
import net.pladema.patient.repository.entity.IdentityVerificationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("masterdata/patient")
@Api(value = "PatientMasterData", tags = { "PatientMasterData" })
public class PatientMasterDataController {

	private static final Logger LOG = LoggerFactory.getLogger(PatientMasterDataController.class);

	private final IdentityVerificationStatusRepository identityVerificationStatusRepository;

	public PatientMasterDataController(IdentityVerificationStatusRepository identityVerificationStatusRepository) {
		this.identityVerificationStatusRepository = identityVerificationStatusRepository;
	}


	@RequestMapping(value = "/withoutIdentityReasons")
	public ResponseEntity<List<IdentityVerificationStatus>> getIdentityVerificationStatus(){
		List<IdentityVerificationStatus> resultado = identityVerificationStatusRepository.findAll();
		LOG.debug("Get all identityVerificationStatus -> {} ", resultado);
		return ResponseEntity.ok(resultado);
	}

}