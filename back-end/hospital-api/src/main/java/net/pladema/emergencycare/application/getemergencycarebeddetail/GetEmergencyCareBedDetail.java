package net.pladema.emergencycare.application.getemergencycarebeddetail;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import net.pladema.emergencycare.application.exception.EmergencyCareAttentionPlaceException;
import net.pladema.emergencycare.application.exception.EmergencyCareAttentionPlaceExceptionEnum;
import net.pladema.emergencycare.application.getemergencycareepisodeinfoforattentionplacedetail.GetEmergencyCareEpisodeInfoForAttentionPlaceDetail;
import net.pladema.emergencycare.application.port.output.EmergencyCareBedStorage;
import net.pladema.emergencycare.application.port.output.EmergencyCareEpisodeStorage;

import net.pladema.emergencycare.domain.EmergencyCareBedDetailBo;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;
import net.pladema.establishment.domain.bed.EmergencyCareBedBo;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetEmergencyCareBedDetail {

	private final EmergencyCareBedStorage emergencyCareBedStorage;
	private final EmergencyCareEpisodeStorage emergencyCareEpisodeStorage;
	private final GetEmergencyCareEpisodeInfoForAttentionPlaceDetail getEmergencyCareEpisodeInfoForAttentionPlaceDetail;

	public EmergencyCareBedDetailBo run(Integer bedId){
		log.debug("Input GetEmergencyCareBedDetail parameters -> bedId {}", bedId);
		EmergencyCareBedBo bed = getBedOrFail(bedId);
		EmergencyCareBedDetailBo result = new EmergencyCareBedDetailBo(bed);
		var episode = getEmergencyCareEpisode(bedId);
		episode.ifPresent(ec ->
			getEmergencyCareEpisodeInfoForAttentionPlaceDetail.run(result, ec)
		);
		log.debug("Output -> result {}", result);
		return result;
	}

	private EmergencyCareBedBo getBedOrFail(Integer bedId){
		return emergencyCareBedStorage.getById(bedId)
				.orElseThrow(() -> new EmergencyCareAttentionPlaceException(
						EmergencyCareAttentionPlaceExceptionEnum.ATTENTION_PLACE_NOT_FOUND,
						"No se encontró una cama con id " + bedId)
				);
	}

	private Optional<EmergencyCareBo> getEmergencyCareEpisode(Integer bedId){
		return emergencyCareEpisodeStorage.getByBedIdInAttention(bedId);
	}
}
