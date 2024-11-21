package ar.lamansys.sgh.clinichistory.application.isolationalerts;

import java.util.List;

import ar.lamansys.sgh.clinichistory.domain.isolation.IsolationAlertAuthorBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.ports.IsolationAlertStorage;
import ar.lamansys.sgh.clinichistory.domain.isolation.IsolationAlertForPdfDocumentBo;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FetchIsolationAlertsForPdfDocument {
	private final IsolationAlertStorage isolationAlertStorage;
	private final SharedPersonPort sharedPersonPort;
	public List<IsolationAlertForPdfDocumentBo> run(Long documentId) {
		var ret = isolationAlertStorage.findByDocumentIdForDocumentPdf(documentId);
		ret.forEach(alert -> alert.setUpdatedBy(getAuthor(alert.getUpdatedById())));
		return ret;
	}

	private IsolationAlertAuthorBo getAuthor(Integer updatedById) {
		return new IsolationAlertAuthorBo(
			updatedById,
			sharedPersonPort.getCompletePersonNameByUserId(updatedById)
		);
	}
}