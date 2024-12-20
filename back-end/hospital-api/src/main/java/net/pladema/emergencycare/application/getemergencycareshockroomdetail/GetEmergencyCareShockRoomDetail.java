package net.pladema.emergencycare.application.getemergencycareshockroomdetail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.emergencycare.application.exception.EmergencyCareAttentionPlaceException;
import net.pladema.emergencycare.application.exception.EmergencyCareAttentionPlaceExceptionEnum;
import net.pladema.emergencycare.application.getemergencycareepisodeinfoforattentionplacedetail.GetEmergencyCareEpisodeInfoForAttentionPlaceDetail;
import net.pladema.emergencycare.application.port.output.EmergencyCareEpisodeStorage;

import net.pladema.emergencycare.application.port.output.EmergencyCareShockroomStorage;
import net.pladema.emergencycare.domain.EmergencyCareShockRoomDetailBo;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;

import net.pladema.medicalconsultation.shockroom.domain.ShockRoomBo;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetEmergencyCareShockRoomDetail {

	private final EmergencyCareShockroomStorage emergencyCareShockroomStorage;
	private final EmergencyCareEpisodeStorage emergencyCareEpisodeStorage;
	private final GetEmergencyCareEpisodeInfoForAttentionPlaceDetail getEmergencyCareEpisodeInfoForAttentionPlaceDetail;

	public EmergencyCareShockRoomDetailBo run(Integer shockroomId){
		log.debug("Input GetEmergencyCareShockRoomDetail parameters -> shockroomId {}", shockroomId);
		ShockRoomBo shockroom = getShockroomOrFail(shockroomId);
		EmergencyCareShockRoomDetailBo result = new EmergencyCareShockRoomDetailBo(shockroom);
		var episode = getEmergencyCareEpisodeOrFail(shockroomId);
		episode.ifPresent(ec ->
			getEmergencyCareEpisodeInfoForAttentionPlaceDetail.run(result, ec)
		);
		log.debug("Output -> result {}", result);
		return result;
	}

	private ShockRoomBo getShockroomOrFail(Integer shockroomId){
		return emergencyCareShockroomStorage.getById(shockroomId)
				.orElseThrow(() -> new EmergencyCareAttentionPlaceException(
						EmergencyCareAttentionPlaceExceptionEnum.ATTENTION_PLACE_NOT_FOUND,
						"No se encontró un shockroom con id " + shockroomId)
				);
	}

	private Optional<EmergencyCareBo> getEmergencyCareEpisodeOrFail(Integer shockroomId){
		return emergencyCareEpisodeStorage.getByShockroomIdInAttention(shockroomId);
	}
}
