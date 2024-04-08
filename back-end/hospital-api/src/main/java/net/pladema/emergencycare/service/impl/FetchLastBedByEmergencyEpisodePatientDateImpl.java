package net.pladema.emergencycare.service.impl;
import lombok.AllArgsConstructor;
import net.pladema.emergencycare.repository.HistoricEmergencyEpisodeRepository;
import net.pladema.emergencycare.service.EmergencyCareEpisodeService;
import net.pladema.emergencycare.service.FetchLastBedByEmergencyEpisodePatientDate;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;
import net.pladema.emergencycare.service.domain.EmergencyEpisodePatientBedRoomBo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class FetchLastBedByEmergencyEpisodePatientDateImpl implements FetchLastBedByEmergencyEpisodePatientDate {

	private final HistoricEmergencyEpisodeRepository historicEmergencyEpisodeRepository;

	private final EmergencyCareEpisodeService emergencyCareEpisodeService;

	@Override
	public EmergencyEpisodePatientBedRoomBo run(Integer emergencyEpisodeId, LocalDateTime requestDate, Integer institutionId) {
		Page<EmergencyEpisodePatientBedRoomBo> result = historicEmergencyEpisodeRepository.getLastBedByEmergencyEpisodePatientDate(emergencyEpisodeId, requestDate, PageRequest.of(0, 1));
		if (!result.getContent().isEmpty())
			return new EmergencyEpisodePatientBedRoomBo(result.getContent().get(0).getBed(), result.getContent().get(0).getRoom());
		EmergencyCareBo emergencyCareBo = emergencyCareEpisodeService.get(emergencyEpisodeId, institutionId);
		return new EmergencyEpisodePatientBedRoomBo(emergencyCareBo.getBedNumber(), emergencyCareBo.getBedNumber());
	}
}
