package ar.lamansys.sgh.clinichistory.application.isolationalerts;

import java.util.List;

import ar.lamansys.sgh.clinichistory.domain.isolation.IsolationAlertAuthorBo;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.ports.IsolationAlertStorage;
import ar.lamansys.sgh.clinichistory.domain.isolation.IsolationAlertBo;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FetchDocumentIsolationAlerts {
	private final IsolationAlertStorage isolationAlertStorage;
	private final SharedPersonPort sharedPersonPort;
	public List<IsolationAlertBo> run(Long documentId) {
		var ret = isolationAlertStorage.findByDocumentId(documentId);
		ret.forEach(alert -> alert.setUpdatedBy(getAuthor(alert.getUpdatedById())));
		return ret;
	}

	private IsolationAlertAuthorBo getAuthor(Integer authorId) {
		var fullName = sharedPersonPort.getCompletePersonNameByUserId(authorId);
		return new IsolationAlertAuthorBo(authorId, fullName);
	}
}
