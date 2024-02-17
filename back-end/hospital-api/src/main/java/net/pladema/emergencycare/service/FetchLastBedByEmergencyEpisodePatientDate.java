package net.pladema.emergencycare.service;

import net.pladema.emergencycare.service.domain.EmergencyEpisodePatientBedRoomBo;

import java.time.LocalDateTime;

public interface FetchLastBedByEmergencyEpisodePatientDate {

	EmergencyEpisodePatientBedRoomBo run(Integer emergencyEpisodeId, LocalDateTime requestDate, Integer instutitionId);
}
