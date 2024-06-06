package ar.lamansys.sgh.publicapi.activities.infrastructure.input.service;

import java.util.Optional;

public interface ActivitiesPublicApiPermissions {
	boolean canAccess(Integer institutionId);

	boolean canFetchHealthcareProfessionals(Integer institutionId);

	Optional<Integer> findInstitutionId(String refsetCode);
}
