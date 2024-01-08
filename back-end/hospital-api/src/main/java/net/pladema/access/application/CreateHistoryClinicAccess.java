package net.pladema.access.application;

import net.pladema.access.domain.bo.ClinicHistoryAccessBo;

public interface CreateHistoryClinicAccess {

	void run(Integer institutionId, Integer patientId, ClinicHistoryAccessBo clinicHistoryAccessBo);
}
