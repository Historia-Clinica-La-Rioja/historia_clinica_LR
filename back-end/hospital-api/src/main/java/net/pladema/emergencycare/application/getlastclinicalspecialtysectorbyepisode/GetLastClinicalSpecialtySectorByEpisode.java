package net.pladema.emergencycare.application.getlastclinicalspecialtysectorbyepisode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.emergencycare.application.port.output.EmergencyCareClinicalSpecialtySectorStorage;
import net.pladema.establishment.domain.ClinicalSpecialtySectorBo;

import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class GetLastClinicalSpecialtySectorByEpisode {

	private final EmergencyCareClinicalSpecialtySectorStorage emergencyCareClinicalSpecialtySectorStorage;

	public ClinicalSpecialtySectorBo run(Integer episodeId){
		log.debug("Input GetLastClinicalSpecialtySectorByEpisode parameters -> episodeId {}", episodeId);
		ClinicalSpecialtySectorBo result = emergencyCareClinicalSpecialtySectorStorage.getLastByEpisodeId(episodeId);
		log.debug("Output -> result {}", result);
		return result;
	}
}
