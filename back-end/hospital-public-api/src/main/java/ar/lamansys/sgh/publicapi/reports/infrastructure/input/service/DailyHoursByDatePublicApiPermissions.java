package ar.lamansys.sgh.publicapi.reports.infrastructure.input.service;

import java.util.Optional;

public interface DailyHoursByDatePublicApiPermissions {

	Optional<Integer> getInstitutionId(String refsetCode);

	boolean canFetchDailyHoursByDate();
}
