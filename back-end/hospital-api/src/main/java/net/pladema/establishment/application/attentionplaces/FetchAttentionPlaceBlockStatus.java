package net.pladema.establishment.application.attentionplaces;

import lombok.RequiredArgsConstructor;
import net.pladema.establishment.application.port.BlockAttentionPlaceStorage;
import net.pladema.establishment.domain.FetchAttentionPlaceBlockStatusBo;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FetchAttentionPlaceBlockStatus {
	private final BlockAttentionPlaceStorage blockAttentionPlaceStorage;
	public Optional<FetchAttentionPlaceBlockStatusBo> findForBed(Integer institutionId, Integer bedId) {
		return blockAttentionPlaceStorage.fetchBedDetailedStatus(institutionId, bedId);
	}

	public Optional<FetchAttentionPlaceBlockStatusBo> findForShockRoom(Integer institutionId, Integer shockRoomId) {
		return blockAttentionPlaceStorage.fetchShockRoomDetailedStatus(institutionId, shockRoomId);
	}

	public Optional<FetchAttentionPlaceBlockStatusBo> findForDoctorsOffice(Integer institutionId, Integer doctorsOfficeId) {
		return blockAttentionPlaceStorage.fetchDoctorsOfficeDetailedStatus(institutionId, doctorsOfficeId);
	}

	public Boolean isBedBlocked(Integer institutionId, Integer bedId) {
		return blockAttentionPlaceStorage
		.findBedByIdAndInstitutionId(bedId, institutionId)
				.map(x -> x.getStatusId().isPresent())
				.orElse(false);
	}

}
