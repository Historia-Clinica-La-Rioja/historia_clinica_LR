package ar.lamansys.sgh.publicapi.activities.infrastructure.input.service;

import java.util.Optional;

public interface ActivitiesPublicApiPermissions {
	boolean canAccess(Integer institutionId);

	boolean canFetchHealthcareProfessionals(Integer institutionId);

	boolean canFetchAllMedicalCoverages();

	Optional<Integer> findInstitutionId(String refsetCode);

	boolean canAccessActivityInfo(Integer institutionId);

	boolean canAccessProcessActivityInfo(Integer institutionId);

	boolean canAccessActivityByFilter(Integer institutionId);

	boolean canFetchAllInstitutions();

	boolean canFetchHolidays();
}
