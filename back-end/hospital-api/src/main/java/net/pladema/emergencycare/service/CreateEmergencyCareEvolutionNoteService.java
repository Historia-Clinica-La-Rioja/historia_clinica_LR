package net.pladema.emergencycare.service;

import net.pladema.emergencycare.service.domain.EmergencyCareEvolutionNoteBo;

public interface CreateEmergencyCareEvolutionNoteService {

	EmergencyCareEvolutionNoteBo execute(Integer institutionId, Integer patientId, Integer doctorId, Integer clinicalSpecialtyId, Integer patientMedicalCoverageId);

}
