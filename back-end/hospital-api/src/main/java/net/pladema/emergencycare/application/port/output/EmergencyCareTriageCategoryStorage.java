package net.pladema.emergencycare.application.port.output;

import net.pladema.emergencycare.triage.service.domain.TriageCategoryBo;

public interface EmergencyCareTriageCategoryStorage {

	TriageCategoryBo getById(Short id);
}
