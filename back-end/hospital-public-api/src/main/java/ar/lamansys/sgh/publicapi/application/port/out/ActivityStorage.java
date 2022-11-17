package ar.lamansys.sgh.publicapi.application.port.out;

import ar.lamansys.sgh.publicapi.domain.AttentionInfoBo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ActivityStorage {

	Optional<AttentionInfoBo> getActivityById(String refsetCode, Long activityId);

	List<AttentionInfoBo> getActivitiesByInstitution(String refsetCode, LocalDate fromDate, LocalDate toDate, Boolean reprocessing);

	List<AttentionInfoBo> getActivitiesByInstitutionAndPatient(String refsetCode, String identificationNumber, LocalDate fromDate, LocalDate toDate, Boolean reprocessing);

	List<AttentionInfoBo> getActivitiesByInstitutionAndCoverage(String refsetCode, String coverageCuit, LocalDate fromDate, LocalDate toDate, Boolean reprocessing);
}
