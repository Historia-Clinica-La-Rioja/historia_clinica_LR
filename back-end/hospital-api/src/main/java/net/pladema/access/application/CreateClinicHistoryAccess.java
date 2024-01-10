package net.pladema.access.application;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.access.domain.bo.ClinicHistoryAccessBo;

import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class CreateClinicHistoryAccess {

	private final ClinicHistoryAccessStorage clinicHistoryAccessStorage;

	public void run(Integer institutionId, Integer patientId, ClinicHistoryAccessBo clinicHistoryAccessBo) {
		log.debug("Input parameters -> institutionId {}, patientId {}, clinicHistoryAccessBo {}", institutionId, patientId, clinicHistoryAccessBo);
		clinicHistoryAccessStorage.createClinicHistoryAccess(institutionId, patientId, clinicHistoryAccessBo);
	}
}
