package ar.lamansys.sgh.clinichistory.application.ports;

import ar.lamansys.sgh.clinichistory.domain.isolation.FetchPatientIsolationAlertBo;
import ar.lamansys.sgh.clinichistory.domain.isolation.IsolationAlertBo;
import ar.lamansys.sgh.clinichistory.domain.isolation.IsolationAlertForPdfDocumentBo;

import java.util.List;
import java.util.Optional;

public interface IsolationAlertStorage {

	void save(Long documentId, List<IsolationAlertBo> isolationAlerts);

	List<IsolationAlertBo> findByDocumentId(Long documentId);

	List<FetchPatientIsolationAlertBo> findByPatientId(Integer patientId);

	Optional<FetchPatientIsolationAlertBo> findByAlertId(Integer alertId);

	List<IsolationAlertForPdfDocumentBo> findByDocumentIdForDocumentPdf(Long documentId);

	Optional<Integer> cancel(Integer alert);
}
