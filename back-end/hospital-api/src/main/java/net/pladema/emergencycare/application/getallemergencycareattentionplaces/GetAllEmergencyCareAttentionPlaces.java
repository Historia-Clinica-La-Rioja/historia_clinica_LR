package net.pladema.emergencycare.application.getallemergencycareattentionplaces;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.emergencycare.application.port.output.EmergencyCareClinicalSpecialtySectorStorage;
import net.pladema.emergencycare.application.port.output.EmergencyCareDoctorsOfficeStorage;
import net.pladema.emergencycare.application.port.output.EmergencyCareBedStorage;
import net.pladema.emergencycare.application.port.output.EmergencyCareSectorStorage;
import net.pladema.emergencycare.application.port.output.EmergencyCareShockroomStorage;
import net.pladema.emergencycare.domain.EmergencyCareAttentionPlaceBo;

import net.pladema.establishment.domain.ESectorOrganization;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetAllEmergencyCareAttentionPlaces {

	private final EmergencyCareSectorStorage emergencyCareSectorStorage;
	private final EmergencyCareDoctorsOfficeStorage emergencyCareDoctorsOfficeStorage;
	private final EmergencyCareShockroomStorage emergencyCareShockroomStorage;
	private final EmergencyCareBedStorage emergencyCareBedStorage;
	private final EmergencyCareClinicalSpecialtySectorStorage emergencyCareClinicalSpecialtySectorStorage;

	public List<EmergencyCareAttentionPlaceBo> run(Integer institutionId){
		log.debug("Input GetAllEmergencyCareAttentionPlaces parameters -> institutionId {}", institutionId);
		List<EmergencyCareAttentionPlaceBo> result = emergencyCareSectorStorage.getAllByInstitutionOrderByHierarchy(institutionId);
		result.forEach(this::setAttentionPlacesInfo);
		log.debug("Output -> result {}", result);
		return result;
	}

	private void setAttentionPlacesInfo(EmergencyCareAttentionPlaceBo ecap){
		Integer sectorId = ecap.getId();
		ecap.setDoctorsOffices(emergencyCareDoctorsOfficeStorage.getAllBySectorId(sectorId));
		ecap.setShockRooms(emergencyCareShockroomStorage.getAllBySectorId(sectorId));
		ecap.setBeds(emergencyCareBedStorage.getAllBySectorId(sectorId));
		Short sectorOrganizationId = ecap.getSectorOrganizationId();
		if (sectorOrganizationId != null && sectorOrganizationId.equals(ESectorOrganization.SERVICES.getId()))
			ecap.setClinicalSpecialtySectors(emergencyCareClinicalSpecialtySectorStorage.getAllBySectorId(sectorId));
	}
}
