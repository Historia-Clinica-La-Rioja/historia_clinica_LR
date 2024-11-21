package net.pladema.establishment.application.attentionplaces;

import lombok.RequiredArgsConstructor;
import net.pladema.emergencycare.application.port.output.EmergencyCareEpisodeStorage;
import net.pladema.establishment.application.attentionplaces.exceptions.BlockAttentionPlaceException;
import net.pladema.establishment.application.port.BlockAttentionPlaceStorage;
import net.pladema.establishment.domain.EBlockAttentionPlaceReason;

import net.pladema.sgx.session.application.port.UserSessionStorage;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BlockAttentionPlace {

	private final UserSessionStorage userSessionStorage;
	private final BlockAttentionPlaceStorage blockAttentionPlaceStoragePort;
	private final EmergencyCareEpisodeStorage emergencyCareEpisodeStorage;

	@Transactional
	public void blockBed(Integer institutionId, Integer bedId, EBlockAttentionPlaceReason reasonEnum, String reason) {
		var bed = blockAttentionPlaceStoragePort
			.findBedByIdAndInstitutionId(bedId, institutionId).orElseThrow(() -> bedNotFound(institutionId, bedId));
		if (bed.isBlocked()) throw bedAlreadyBlocked(institutionId, bedId);
		if (!bed.getIsFree()) throw bedIsNotFree(institutionId, bedId);
		Integer newStatusId = blockAttentionPlaceStoragePort.newBlockedStatus(
				reasonEnum,
				reason,
				userSessionStorage.getUserId()
				);
		blockAttentionPlaceStoragePort.updateBedStatusToBlocked(bedId, newStatusId);
	}

	@Transactional
	public void blockShockRoom(Integer institutionId, Integer shockroomId, EBlockAttentionPlaceReason reasonEnum, String reason) {
		if (emergencyCareEpisodeStorage.existsEpisodeInOffice(null, shockroomId))
			throw shockRoomIsNotFree(institutionId, shockroomId);
		var shockRoom = blockAttentionPlaceStoragePort
				.findShockRoomByIdAndInstitutionId(shockroomId, institutionId).orElseThrow(() -> shockRoomNotFound(institutionId, shockroomId));
		if (shockRoom.isBlocked()) throw shockRoomAlreadyBlocked(institutionId, shockroomId);
		if (!shockRoom.getIsFree()) throw shockRoomIsNotFree(institutionId, shockroomId);
		Integer newStatusId = blockAttentionPlaceStoragePort.newBlockedStatus(
				reasonEnum,
				reason,
				userSessionStorage.getUserId()
		);
		blockAttentionPlaceStoragePort.updateShockRoomStatus(shockroomId, newStatusId);
	}

	@Transactional
	public void blockdoctorsOffice(Integer institutionId, Integer doctorsOfficeId, EBlockAttentionPlaceReason reasonEnum, String reason) {
		if (emergencyCareEpisodeStorage.existsEpisodeInOffice(doctorsOfficeId, null))
			throw doctorsOfficeIsNotFree(institutionId, doctorsOfficeId);
		var doctorsOffice = blockAttentionPlaceStoragePort
			.findDoctorsOfficeByIdAndInstitutionId(doctorsOfficeId, institutionId).orElseThrow(() -> doctorsOfficeNotFound(institutionId, doctorsOfficeId));
		if (doctorsOffice.isBlocked()) throw doctorsOfficeAlreadyBlocked(institutionId, doctorsOfficeId);
		if (!doctorsOffice.getIsFree()) throw doctorsOfficeIsNotFree(institutionId, doctorsOfficeId);
		Integer newStatusId = blockAttentionPlaceStoragePort.newBlockedStatus(
				reasonEnum,
				reason,
				userSessionStorage.getUserId()
		);
		blockAttentionPlaceStoragePort.updateDoctorsOfficeStatus(doctorsOfficeId, newStatusId);
	}

	@Transactional
	public void unblockBed(Integer institutionId, Integer bedId) {
		var bed = blockAttentionPlaceStoragePort
			.findBedByIdAndInstitutionId(bedId, institutionId)
			.orElseThrow(() -> bedNotFound(institutionId, bedId));
		bed.getStatusId().ifPresent(statusId -> {
			blockAttentionPlaceStoragePort.updateBedStatusToUnblocked(bed.getId(), statusId);
		});
	}

	@Transactional
	public void unblockShockRoom(Integer institutionId, Integer id) {
		var shockRoom = blockAttentionPlaceStoragePort
				.findShockRoomByIdAndInstitutionId(id, institutionId)
				.orElseThrow(() -> shockRoomNotFound(institutionId, id));
		shockRoom.getStatusId()
			.ifPresent(statusId -> blockAttentionPlaceStoragePort.deleteShockRoomStatus(shockRoom.getId(), statusId));
	}

	@Transactional
	public void unblockDoctorsOffice(Integer institutionId, Integer id) {
		var doctorsOffice = blockAttentionPlaceStoragePort
				.findDoctorsOfficeByIdAndInstitutionId(id, institutionId)
				.orElseThrow(() -> doctorsOfficeNotFound(institutionId, id));
		doctorsOffice.getStatusId()
			.ifPresent(statusId -> blockAttentionPlaceStoragePort.deleteDoctorsOfficeStatus(doctorsOffice.getId(), statusId));
	}

	private BlockAttentionPlaceException shockRoomAlreadyBlocked(Integer institutionId, Integer shockRoomId) {
		return BlockAttentionPlaceException.shockRoomAlreadyBlocked(institutionId, shockRoomId);
	}

	private BlockAttentionPlaceException doctorsOfficeAlreadyBlocked(Integer institutionId, Integer doctorsOfficeId) {
		return BlockAttentionPlaceException.doctorsOfficeAlreadyBlocked(institutionId, doctorsOfficeId);
	}

	private BlockAttentionPlaceException bedAlreadyBlocked(Integer institutionId, Integer bedId) {
		return BlockAttentionPlaceException.bedAlreadyBlocked(institutionId, bedId);
	}

	private BlockAttentionPlaceException bedNotFound(Integer institutionId, Integer bedId) {
		return BlockAttentionPlaceException.bedNotFound(institutionId, bedId);
	}

	private BlockAttentionPlaceException shockRoomNotFound(Integer institutionId, Integer shockroomId) {
		return BlockAttentionPlaceException.shockRoomNotFound(institutionId, shockroomId);
	}

	private BlockAttentionPlaceException doctorsOfficeNotFound(Integer institutionId, Integer doctorsOfficeId) {
		return BlockAttentionPlaceException.doctorsOfficeNotFound(institutionId, doctorsOfficeId);
	}

	private BlockAttentionPlaceException bedIsNotFree(Integer institutionId, Integer id) {
		return BlockAttentionPlaceException.bedNotFree(institutionId, id);
	}
	private BlockAttentionPlaceException shockRoomIsNotFree(Integer institutionId, Integer id) {
		return BlockAttentionPlaceException.shockRoomNotFree(institutionId, id);
	}
	private BlockAttentionPlaceException doctorsOfficeIsNotFree(Integer institutionId, Integer id) {
		return BlockAttentionPlaceException.doctorsOfficeNotFree(institutionId, id);
	}
}
