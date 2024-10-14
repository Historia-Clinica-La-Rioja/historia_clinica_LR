package net.pladema.emergencycare.application.getallclinicalspecialtysectorbyinstitution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.emergencycare.application.port.output.EmergencyCareClinicalSpecialtySectorStorage;

import net.pladema.establishment.domain.ClinicalSpecialtySectorBo;

import net.pladema.establishment.repository.entity.SectorType;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetAllClinicalSpecialtySectorByInstitution {

	private final EmergencyCareClinicalSpecialtySectorStorage emergencyCareClinicalSpecialtySectorStorage;

	public List<ClinicalSpecialtySectorBo> run(Integer institutionId){
		log.debug("Input GetAllClinicalSpecialtySectorByInstitution parameters -> institutionId {}", institutionId);
		List<ClinicalSpecialtySectorBo> result = emergencyCareClinicalSpecialtySectorStorage.getAllBySectorTypeAndInstitution(SectorType.EMERGENCY_CARE_ID,institutionId);
		log.debug("Output -> result {}", result);
		return result;
	}
}
