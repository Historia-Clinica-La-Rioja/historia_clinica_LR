package net.pladema.emergencycare.service;

import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;
import net.pladema.emergencycare.service.domain.EmergencyCareEpisodeInProgressBo;
import net.pladema.emergencycare.service.domain.PatientECEBo;

import java.time.LocalDateTime;
import java.util.List;

public interface  EmergencyCareEpisodeService {

    List<EmergencyCareBo> getAll(Integer institutionId);

    EmergencyCareEpisodeInProgressBo emergencyCareEpisodeInProgressByInstitution(Integer institutionId, Integer patientId);

	EmergencyCareEpisodeInProgressBo emergencyCareEpisodeInProgress(Integer institutionId, Integer patientId);

	EmergencyCareBo getEpisodeSummary(Integer institutionId, Integer episodeId);

    EmergencyCareBo get(Integer episodeId, Integer institutionId);

    EmergencyCareBo createAdministrative(EmergencyCareBo newEmergencyCare, Integer institutionId);

    EmergencyCareBo createAdult(EmergencyCareBo newEmergencyCare, Integer institutionId, RiskFactorBo riskFactors);

    EmergencyCareBo createPediatric(EmergencyCareBo newEmergencyCare, Integer institutionId, RiskFactorBo riskFactors);

    Integer updateAdministrative(EmergencyCareBo newEmergencyCare, Integer institutionId);

    Boolean validateAndSetPatient(Integer episodeId, Integer patientId, Integer institutionId);

	boolean haveMoreThanOneEmergencyCareEpisodeFromPatients(List<Integer> patients);
	
	PatientECEBo getRelatedPatientData(Integer episodeId);

	Integer getPatientMedicalCoverageIdByEpisode(Integer emergencyCareEpisodeId);

	boolean isBedOccupiedByEmergencyEpisode(Integer bedId);
	
	Boolean hasEvolutionNote(Integer episodeId);

	Integer getEmergencyEpisodeEpisodeIdByDate(Integer institutionId, Integer patientId, LocalDateTime date);

}
