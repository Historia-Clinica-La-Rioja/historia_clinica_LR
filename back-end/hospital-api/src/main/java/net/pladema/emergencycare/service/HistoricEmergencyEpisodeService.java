package net.pladema.emergencycare.service;

import net.pladema.emergencycare.service.domain.HistoricEmergencyEpisodeBo;

import java.util.List;

public interface HistoricEmergencyEpisodeService {

    HistoricEmergencyEpisodeBo saveChange(HistoricEmergencyEpisodeBo historicEmergencyEpisodeBo);

    List<HistoricEmergencyEpisodeBo> getAllHistoricByEmergecyEpisodeId(Integer emergencyEpisodeId);
}
