package net.pladema.emergencycare.application.port.output;

import net.pladema.establishment.domain.ClinicalSpecialtySectorBo;

import java.util.List;

public interface EmergencyCareClinicalSpecialtySectorStorage {

	List<ClinicalSpecialtySectorBo> getAllBySectorTypeAndInstitution(Short sectorTypeId, Integer institutionId);

	String getDescriptionById(Integer clinicalSpecialtySectorId);
}
