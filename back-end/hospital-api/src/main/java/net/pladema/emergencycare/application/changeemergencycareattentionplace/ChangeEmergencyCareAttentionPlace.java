package net.pladema.emergencycare.application.changeemergencycareattentionplace;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.emergencycare.application.exception.EmergencyCareAttentionPlaceException;
import net.pladema.emergencycare.application.exception.EmergencyCareAttentionPlaceExceptionEnum;
import net.pladema.emergencycare.application.exception.EmergencyCareEpisodeExcepcionEnum;
import net.pladema.emergencycare.application.exception.EmergencyCareEpisodeException;
import net.pladema.emergencycare.application.port.output.EmergencyCareEpisodeStateStorage;
import net.pladema.emergencycare.application.port.output.EmergencyCareEpisodeStorage;
import net.pladema.emergencycare.application.port.output.HistoricEmergencyEpisodeStorage;
import net.pladema.emergencycare.domain.ChangeEmergencyCareEpisodeAttentionPlaceBo;

import net.pladema.emergencycare.domain.EmergencyCareEpisodeAttentionPlaceBo;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;

import net.pladema.emergencycare.service.domain.HistoricEmergencyEpisodeBo;

import net.pladema.establishment.application.port.BlockAttentionPlaceStorage;
import net.pladema.establishment.controller.service.BedExternalService;

import net.pladema.establishment.domain.AttentionPlaceBo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChangeEmergencyCareAttentionPlace {

	private final EmergencyCareEpisodeStorage emergencyCareEpisodeStorage;
	private final EmergencyCareEpisodeStateStorage emergencyCareEpisodeStateStorage;
	private final BedExternalService bedExternalService;
	private final HistoricEmergencyEpisodeStorage historicEmergencyEpisodeStorage;
	private final BlockAttentionPlaceStorage blockAttentionPlaceStorage;

	@Transactional
	public Boolean run(ChangeEmergencyCareEpisodeAttentionPlaceBo ceceap){
		log.debug("Input ChangeEmergencyCareAttentionPlace parameters -> changeEmergencyCareEpisodeAttentionPlaceBo {}", ceceap);
		EmergencyCareEpisodeAttentionPlaceBo eceap = ceceap.getEmergencyCareEpisodeAttentionPlace();
		validateAttentionPlace(eceap);
		Integer episodeId = ceceap.getEpisodeId();
		EmergencyCareBo ec = getEmergencyCareEpisodeOrFail(episodeId);
		Short emergencyCareStateId = ec.getEmergencyCareStateId();
		updateEpisode(episodeId, ec.getInstitutionId(), eceap.getDoctorsOfficeId(), eceap.getShockroomId(), eceap.getBedId(), emergencyCareStateId);
		saveHistoricEmergencyEpisode(episodeId, eceap, emergencyCareStateId);
		if(ec.getBedId() != null)
			bedExternalService.freeBed(ec.getBedId());
		Boolean result = true;
		log.debug("Output -> {}", result);
		return result;
	}

	private EmergencyCareBo getEmergencyCareEpisodeOrFail(Integer episodeId){
		return emergencyCareEpisodeStorage.getById(episodeId)
				.orElseThrow(() -> new EmergencyCareAttentionPlaceException(
						EmergencyCareAttentionPlaceExceptionEnum.NO_ACTIVE_EPISODE_FOUND,
						"No se encontró un episodio de guardia activo con id " + episodeId)
				);
	}

	private void validateAttentionPlace(EmergencyCareEpisodeAttentionPlaceBo eceap){
		if (eceap == null ||
				(eceap.getDoctorsOfficeId() == null && eceap.getShockroomId() == null && eceap.getBedId() == null))
			throw new EmergencyCareAttentionPlaceException(EmergencyCareAttentionPlaceExceptionEnum.ATTENTION_PLACE_NOT_FOUND,
				"El nuevo lugar de atención es requerido. ");
	}

	private void updateEpisode(Integer episodeId, Integer institutionId, Integer doctorsOfficeId, Integer shockroomId,
							   Integer bedId, Short emergencyCareStateId){
		if (doctorsOfficeId != null || shockroomId != null)
			validateDoctorsOfficeOrShockRoomAvailable(doctorsOfficeId, shockroomId, institutionId);
		if (bedId != null) {
			validateBedStatus(bedId);
			emergencyCareEpisodeStateStorage.updateStateWithBed(episodeId, institutionId, emergencyCareStateId, bedId);
			bedExternalService.updateBedStatusOccupied(bedId);
		}
		if (shockroomId != null)
			emergencyCareEpisodeStateStorage.updateStateWithShockroom(episodeId, institutionId, emergencyCareStateId, shockroomId);
		if (doctorsOfficeId != null)
			emergencyCareEpisodeStateStorage.updateStateWithDoctorsOffice(episodeId, institutionId, emergencyCareStateId, doctorsOfficeId);
	}

	private void validateDoctorsOfficeOrShockRoomAvailable(Integer doctorsOfficeId, Integer shockroomId, Integer institutionId) {
		log.debug("Input parameters validateAttentionPlace -> doctorsOfficeId {}, shockroomId {}", doctorsOfficeId, shockroomId);
		if (emergencyCareEpisodeStorage.existsEpisodeInOffice(doctorsOfficeId, shockroomId)) {
			if (doctorsOfficeId != null)
				throw new EmergencyCareEpisodeException(EmergencyCareEpisodeExcepcionEnum.DOCTORS_OFFICE_NOT_AVAILABLE,
						"El consultorio elegido se encuentra ocupado.");
			throw new EmergencyCareEpisodeException(EmergencyCareEpisodeExcepcionEnum.SHOCKROOM_NOT_AVAILABLE,
					"El shockroom elegido se encuentra ocupado.");
		}
		if (doctorsOfficeId != null && blockAttentionPlaceStorage.findDoctorsOfficeByIdAndInstitutionId(doctorsOfficeId, institutionId).map(AttentionPlaceBo::isBlocked).orElse(false))
			throw new EmergencyCareEpisodeException(EmergencyCareEpisodeExcepcionEnum.DOCTORS_OFFICE_NOT_AVAILABLE, "El consultorio elegido se encuentra ocupado.");
		if (shockroomId != null && blockAttentionPlaceStorage.findShockRoomByIdAndInstitutionId(shockroomId, institutionId).map(AttentionPlaceBo::isBlocked).orElse(false))
			throw new EmergencyCareEpisodeException(EmergencyCareEpisodeExcepcionEnum.SHOCKROOM_NOT_AVAILABLE, "El shockroom elegido se encuentra ocupado.");
	}

	private void validateBedStatus(Integer bedId) {
		log.debug("Input parameters validateBedStatus -> bedId {}", bedId);
		if (!bedExternalService.isBedFreeAndAvailable(bedId)) {
			throw new EmergencyCareEpisodeException(EmergencyCareEpisodeExcepcionEnum.BED_NOT_AVAILABLE,
					"La cama seleccionada no está disponible.");
		}
	}

	private void saveHistoricEmergencyEpisode(Integer episodeId, EmergencyCareEpisodeAttentionPlaceBo eceap, Short emergencyCareStateId) {
		HistoricEmergencyEpisodeBo toSave = new HistoricEmergencyEpisodeBo(episodeId, LocalDateTime.now(), emergencyCareStateId,
				eceap.getDoctorsOfficeId(), eceap.getShockroomId(), eceap.getBedId(), null);
		historicEmergencyEpisodeStorage.create(toSave);
	}

}
