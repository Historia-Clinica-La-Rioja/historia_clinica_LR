package net.pladema.clinichistory.requests.servicerequests.application.port;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import net.pladema.clinichistory.requests.servicerequests.domain.observations.GetDiagnosticReportObservationGroupBo;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.exceptions.DiagnosticReportNotFoundException;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.exceptions.DiagnosticReportObservationException;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.DiagnosticReportObservationsForUpdateVo;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.NewDiagnosticReportObservationBo;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.UpdatedDiagnosticReportObservationBo;

public interface DiagnosticReportObservationStorage {
	Optional<DiagnosticReportObservationsForUpdateVo> findObservationsForUpdateByDiagnosticReportId(Integer id);

	Map<Integer, GetDiagnosticReportObservationGroupBo> findObservationsByDiagnosticReportIds(List<Integer> diagnosticReportIds);

	void saveNewObservation(Integer groupId, NewDiagnosticReportObservationBo dro) throws DiagnosticReportObservationException;

	Integer saveNewObservationGroup(Integer diagnosticReportId, Integer procedureTemplateId, Boolean isPartialUpload);

	void updateExistingObservation(UpdatedDiagnosticReportObservationBo updatedObservations) throws DiagnosticReportObservationException;

	void deleteGroup(Integer groupId);

	/**
	 * Returns true if the DiagnosticReport with the given id, or any of the ones linked to it through
	 * the associated service request, is in the finalized status. See DiagnostiReportStatus.FINAL.
	 * @param diagnosticReportId
	 * @return
	 */
	boolean existsRelatedFinalizedDiagnosticReport(Integer diagnosticReportId);

	boolean isFinal(Integer diagnosticReportId) throws DiagnosticReportNotFoundException;

	void updateDiagnosticReportStatusToPartial(Integer diagnosticReportId);

	boolean existsDiagnosticReport(Integer diagnosticReportId);
}
