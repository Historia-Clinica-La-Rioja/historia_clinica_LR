package net.pladema.snowstorm.infrastructure.input.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;

import java.util.List;

public interface SnomedRelatedGroupExternalService {

	List<SharedSnomedDto> getPracticesByInstitution(Integer institutionId);

	List<SharedSnomedDto> getPracticesFromAllInstitutions();

	List<SharedSnomedDto> getPracticesFromAllInstitutionsByCareLineId(Integer careLineId);

	List<SharedSnomedDto> getPracticesByDepartmentId(Short departmentId);

}
