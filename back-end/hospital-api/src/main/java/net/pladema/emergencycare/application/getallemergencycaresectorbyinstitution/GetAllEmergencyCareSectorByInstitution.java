package net.pladema.emergencycare.application.getallemergencycaresectorbyinstitution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.emergencycare.application.port.output.EmergencyCareClinicalSpecialtySectorStorage;
import net.pladema.emergencycare.application.port.output.EmergencyCareSectorStorage;

import net.pladema.emergencycare.domain.EmergencyCareAttentionPlaceBo;

import net.pladema.establishment.domain.ESectorOrganization;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetAllEmergencyCareSectorByInstitution {

	private final EmergencyCareSectorStorage emergencyCareSectorStorage;
	private final EmergencyCareClinicalSpecialtySectorStorage emergencyCareClinicalSpecialtySectorStorage;

	public List<EmergencyCareAttentionPlaceBo> run(Integer institutionId){
		log.debug("Input GetAllEmergencyCareSectorByInstitution parameters -> institutionId {}", institutionId);
		List<EmergencyCareAttentionPlaceBo> result = emergencyCareSectorStorage.getAllByInstitutionOrderByHierarchy(institutionId);
		result.forEach(this::setClinicalSpecialtySectors);
		log.debug("Output -> result {}", result);
		return result;
	}

	private void setClinicalSpecialtySectors(EmergencyCareAttentionPlaceBo ecap){
		Short sectorOrganizationId = ecap.getSectorOrganizationId();
		if (sectorOrganizationId != null && sectorOrganizationId.equals(ESectorOrganization.SERVICES.getId()))
			ecap.setClinicalSpecialtySectors(emergencyCareClinicalSpecialtySectorStorage.getAllBySectorId(ecap.getId()));
	}
}
