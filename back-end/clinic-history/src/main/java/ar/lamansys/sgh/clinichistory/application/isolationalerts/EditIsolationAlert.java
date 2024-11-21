package ar.lamansys.sgh.clinichistory.application.isolationalerts;

import ar.lamansys.sgh.clinichistory.application.isolationalerts.exceptions.IsolationAlertException;
import ar.lamansys.sgh.clinichistory.application.ports.IsolationAlertStorage;
import ar.lamansys.sgh.clinichistory.application.rebuildFile.RebuildFile;
import ar.lamansys.sgh.clinichistory.domain.isolation.FetchPatientIsolationAlertBo;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class EditIsolationAlert {
	private final IsolationAlertStorage isolationAlertStorage;
	private final FetchPatientIsolationAlerts fetchPatientIsolationAlerts;
	private final RebuildFile rebuildFile;

	@Transactional
	public FetchPatientIsolationAlertBo cancel(Integer alertId) {
		var alert = isolationAlertStorage.findByAlertId(alertId).orElseThrow(() -> alertNotFound(alertId));
		if (alert.isFinalized())
			throw IsolationAlertException.alreadyFinalized(alertId);
		var finalizedAlertId = isolationAlertStorage.cancel(alertId).orElseThrow(() -> finalizedError(alertId));
		var ret = fetchPatientIsolationAlerts.findByAlertId(finalizedAlertId);
		//Rebuild the PDF so that it shows the updated alert status
		rebuildFile.run(ret.getDocumentId());
		return ret;

	}

	private IsolationAlertException finalizedError(Integer alertId) {
		return IsolationAlertException.finalizedError(alertId);
	}
	private IsolationAlertException alertNotFound(Integer alertId) {
		return IsolationAlertException.alertNotFound(alertId);
	}
}
