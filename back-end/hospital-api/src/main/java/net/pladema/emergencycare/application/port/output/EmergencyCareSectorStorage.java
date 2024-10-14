package net.pladema.emergencycare.application.port.output;

import net.pladema.emergencycare.domain.EmergencyCareAttentionPlaceBo;

import java.util.List;

public interface EmergencyCareSectorStorage {

	List<EmergencyCareAttentionPlaceBo>  getAllByInstitutionOrderByHierarchy(Integer institutionId);
}
