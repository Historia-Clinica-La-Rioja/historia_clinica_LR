package ar.lamansys.sgh.publicapi.documents.annex.application;

import java.util.Optional;

public interface AnnexReportByEncounterPublicApiPermissionsPort {
	boolean canAccess(Integer institutionId);

	Optional<Integer> findInstitutionId(String refsetCode);
}
