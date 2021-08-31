package net.pladema.patient.portal.service;

import ar.lamansys.sgx.shared.auth.user.SecurityContextUtils;
import net.pladema.patient.repository.PatientRepository;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PatientPortalServiceImpl implements PatientPortalService{

	private static final Logger LOG = LoggerFactory.getLogger(PatientPortalServiceImpl.class);

	private final PatientRepository patientRepository;

	public PatientPortalServiceImpl(PatientRepository patientRepository) {
		this.patientRepository = patientRepository;
	}

	@Override
	public Integer getPatientId() {
		Integer userId = SecurityContextUtils.getUserDetails().userId;
		Integer patientId = patientRepository.getPatientIdByUser(userId).orElseThrow(
				() -> new NotFoundException("Patient-not-found", "Patient not found"));
		LOG.debug("Output -> {}", patientId);
		return patientId;
	}

	@Override
	public Integer getPersonId() {
		Integer userId = SecurityContextUtils.getUserDetails().userId;
		Integer personId = patientRepository.getPersonIdByUser(userId).orElseThrow(
				() -> new NotFoundException("Person-not-found", "Person not found"));
		LOG.debug("Output -> {}", personId);
		return personId;
	}

}
