package ar.lamansys.sgh.clinichistory.application.isolationalerts.fetch;

import java.util.List;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.ports.IsolationAlertStorage;
import ar.lamansys.sgh.clinichistory.domain.ips.IsolationAlertBo;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FetchIsolationAlerts {
	private final IsolationAlertStorage isolationAlertStorage;
	public List<IsolationAlertBo> run(Long documentId, Integer evolutionNoteId) {
		return isolationAlertStorage.fetch(documentId);
	}
}
