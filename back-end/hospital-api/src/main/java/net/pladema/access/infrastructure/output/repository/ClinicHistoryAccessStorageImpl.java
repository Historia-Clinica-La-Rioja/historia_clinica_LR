package net.pladema.access.infrastructure.output.repository;

import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.access.application.ClinicHistoryAccessStorage;

import net.pladema.access.domain.bo.ClinicHistoryAccessBo;

import net.pladema.access.infrastructure.output.repository.entity.ClinicHistoryAudit;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ClinicHistoryAccessStorageImpl implements ClinicHistoryAccessStorage {

	private final ClinicHistoryAccessRepository clinicHistoryAccessRepository;

	@Override
	public void createClinicHistoryAccess(Integer institutionId, Integer patientId, ClinicHistoryAccessBo clinicHistoryAccessBo) {
		log.debug("Input parameters -> institutionId {}, patientId {}, clinicHistoryAccessBo {}", clinicHistoryAccessBo);
		ClinicHistoryAudit clinicHistoryAudit = new ClinicHistoryAudit(clinicHistoryAccessBo);
		Integer userId = UserInfo.getCurrentAuditor();
		clinicHistoryAudit.setUserId(userId);
		clinicHistoryAudit.setPatientId(patientId);
		clinicHistoryAudit.setInstitutionId(institutionId);
		clinicHistoryAccessRepository.save(clinicHistoryAudit);
	}
}
