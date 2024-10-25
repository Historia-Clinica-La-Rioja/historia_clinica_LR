package ar.lamansys.sgh.clinichistory.application.isolationalerts;

import java.util.List;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.ports.IsolationAlertStorage;
import ar.lamansys.sgh.clinichistory.domain.isolation.IsolationAlertBo;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FetchDocumentIsolationAlerts {
	private final IsolationAlertStorage isolationAlertStorage;
	public List<IsolationAlertBo> run(Long documentId) {
		return isolationAlertStorage.fetchByDocumentId(documentId);
	}
}
