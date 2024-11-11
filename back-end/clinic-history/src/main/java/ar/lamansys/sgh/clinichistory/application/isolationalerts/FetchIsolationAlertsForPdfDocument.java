package ar.lamansys.sgh.clinichistory.application.isolationalerts;

import java.util.List;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.ports.IsolationAlertStorage;
import ar.lamansys.sgh.clinichistory.domain.isolation.IsolationAlertForPdfDocumentBo;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FetchIsolationAlertsForPdfDocument {
	private final IsolationAlertStorage isolationAlertStorage;
	public List<IsolationAlertForPdfDocumentBo> run(Long documentId) {
		return isolationAlertStorage.findByDocumentIdForDocumentPdf(documentId);
	}
}