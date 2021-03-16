package net.pladema.patient.portal.service;

import net.pladema.patient.repository.PatientRepository;
import net.pladema.security.utils.SecurityContextUtils;
import net.pladema.sgx.exceptions.NotFoundException;
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

}
