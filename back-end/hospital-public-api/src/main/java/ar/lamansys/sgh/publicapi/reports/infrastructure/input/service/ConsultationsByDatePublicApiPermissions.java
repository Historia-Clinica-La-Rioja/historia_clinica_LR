package ar.lamansys.sgh.publicapi.reports.infrastructure.input.service;

import java.util.Optional;

public interface ConsultationsByDatePublicApiPermissions {

	boolean canFetchConsultations();

	Optional<Integer> getInstitutionId(String refsetCode);
}
