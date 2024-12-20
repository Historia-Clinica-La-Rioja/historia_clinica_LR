package net.pladema.emergencycare.application.sectorhasattentionplaces;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.emergencycare.application.port.output.EmergencyCareBedStorage;
import net.pladema.emergencycare.application.port.output.EmergencyCareDoctorsOfficeStorage;
import net.pladema.emergencycare.application.port.output.EmergencyCareShockroomStorage;

import net.pladema.emergencycare.domain.EmergencyCareSectorHasAttentionPlaceBo;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SectorHasAttentionPlaces {

	private final EmergencyCareDoctorsOfficeStorage emergencyCareDoctorsOfficeStorage;
	private final EmergencyCareShockroomStorage emergencyCareShockroomStorage;
	private final EmergencyCareBedStorage emergencyCareBedStorage;

	public EmergencyCareSectorHasAttentionPlaceBo run(Integer sectorId){
		log.debug("Input SectorHasAttentionPlaces parameters -> sectorId {}", sectorId);
		EmergencyCareSectorHasAttentionPlaceBo result = new EmergencyCareSectorHasAttentionPlaceBo(
				!emergencyCareDoctorsOfficeStorage.getAllBySectorId(sectorId).isEmpty(),
				!emergencyCareShockroomStorage.getAllBySectorId(sectorId).isEmpty(),
				!emergencyCareBedStorage.getAllBySectorId(sectorId).isEmpty()
		);
		log.debug("Output -> result {}", result);
		return result;
	}
}
