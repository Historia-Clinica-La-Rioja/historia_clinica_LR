package net.pladema.emergencycare.application.port.output;

import net.pladema.emergencycare.domain.EmergencyCarePatientBo;

public interface EmergencyCarePatientStorage {

	EmergencyCarePatientBo getById(Integer id);
}
