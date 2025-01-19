package net.pladema.emergencycare.service;

import net.pladema.emergencycare.service.domain.EpisodeDischargeBo;
import net.pladema.emergencycare.service.domain.EpisodeDischargeSummaryBo;
import net.pladema.emergencycare.service.domain.MedicalDischargeBo;

public interface EmergencyCareEpisodeDischargeService {

    boolean newMedicalDischarge(MedicalDischargeBo medicalDischargeBo, Integer institutionId);

    EpisodeDischargeBo getDischarge(Integer episodeId);

	boolean hasMedicalDischarge(Integer episodeId);

	EpisodeDischargeSummaryBo getEpisodeDischargeSummary(Integer episodeId);

}
