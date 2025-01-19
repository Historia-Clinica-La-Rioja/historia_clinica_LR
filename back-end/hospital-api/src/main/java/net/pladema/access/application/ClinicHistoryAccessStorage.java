package net.pladema.access.application;

import net.pladema.access.domain.bo.ClinicHistoryAccessBo;

public interface ClinicHistoryAccessStorage {

	void createClinicHistoryAccess(Integer institutionId, Integer patientId, ClinicHistoryAccessBo clinicHistoryAccessBo);
}
