package ar.lamansys.sgh.publicapi.application.port.out;

import ar.lamansys.sgh.publicapi.domain.AttentionInfoBo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ActivityStorage {

    Optional<AttentionInfoBo> getActivityById(String refsetCode, String provinceCode, Long activityId);

    List<AttentionInfoBo> getActivitiesByInstitution(String refsetCode, String provinceCode, LocalDate fromDate, LocalDate toDate, Boolean reprocessing);

    List<AttentionInfoBo> getActivitiesByInstitutionAndPatient(String refsetCode, String provinceCode, String identificationNumber, LocalDate fromDate, LocalDate toDate, Boolean reprocessing);

    List<AttentionInfoBo> getActivitiesByInstitutionAndCoverage(String refsetCode, String provinceCode, String coverageCuit, LocalDate fromDate, LocalDate toDate, Boolean reprocessing);
}
