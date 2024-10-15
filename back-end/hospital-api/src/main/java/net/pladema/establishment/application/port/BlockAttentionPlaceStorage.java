package net.pladema.establishment.application.port;

import net.pladema.establishment.domain.AttentionPlaceBo;
import net.pladema.establishment.domain.EBlockAttentionPlaceReason;
import net.pladema.establishment.domain.FetchAttentionPlaceBlockStatusBo;

import java.util.Optional;

public interface BlockAttentionPlaceStorage {

	//Find attention place
	Optional<AttentionPlaceBo> findBedByIdAndInstitutionId(Integer bedId, Integer institutionId);
	Optional<AttentionPlaceBo> findShockRoomByIdAndInstitutionId(Integer shockroomId, Integer institutionId);
	Optional<AttentionPlaceBo> findDoctorsOfficeByIdAndInstitutionId(Integer doctorsOfficeId, Integer institutionId);

	//Block
	Integer newBlockedStatus(EBlockAttentionPlaceReason reasonEnum, String reason, Integer userId);
	void updateBedStatusToBlocked(Integer bedId, Integer newStatusId);
	void updateShockRoomStatus(Integer shockRoomId, Integer newStatusId);
	void updateDoctorsOfficeStatus(Integer doctorsOfficeId, Integer newStatusId);

	//Unblock
	void updateBedStatusToUnblocked(Integer id, Integer statusId);
	void deleteShockRoomStatus(Integer bedId, Integer statusId);
	void deleteDoctorsOfficeStatus(Integer bedId, Integer statusId);

	//Compute status
	Optional<FetchAttentionPlaceBlockStatusBo> fetchBedDetailedStatus(Integer institutionId, Integer id);
	Optional<FetchAttentionPlaceBlockStatusBo> fetchShockRoomDetailedStatus(Integer institutionId, Integer id);
	Optional<FetchAttentionPlaceBlockStatusBo> fetchDoctorsOfficeDetailedStatus(Integer institutionId, Integer id);
}
