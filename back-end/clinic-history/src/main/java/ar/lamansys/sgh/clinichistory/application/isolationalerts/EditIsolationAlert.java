package ar.lamansys.sgh.clinichistory.application.isolationalerts;

import ar.lamansys.sgh.clinichistory.application.isolationalerts.exceptions.IsolationAlertException;
import ar.lamansys.sgh.clinichistory.application.ports.IsolationAlertStorage;
import ar.lamansys.sgh.clinichistory.application.rebuildFile.RebuildFile;
import ar.lamansys.sgh.clinichistory.domain.isolation.FetchPatientIsolationAlertBo;

import ar.lamansys.sgh.clinichistory.domain.isolation.UpdateIsolationAlertBo;
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

	@Transactional
	public FetchPatientIsolationAlertBo update(Integer alertId, UpdateIsolationAlertBo update) {
		var alert = isolationAlertStorage.findByAlertId(alertId).orElseThrow(() -> alertNotFound(alertId));
		if (alert.isFinalized())
			throw IsolationAlertException.alreadyFinalized(alertId);
		checkFields(alertId, update, alert);
		var updatedAlertId = isolationAlertStorage
			.update(alertId, update.getCriticalityId(), update.getEndDate(), update.getObservations())
			.orElseThrow(() -> finalizedError(alertId));
		var ret = fetchPatientIsolationAlerts.findByAlertId(updatedAlertId);
		//Rebuild the PDF so that it shows the updated alert values
		rebuildFile.run(ret.getDocumentId());
		return ret;
	}

	private void checkFields(Integer alertId, UpdateIsolationAlertBo update, FetchPatientIsolationAlertBo alert) {
		if (alert.hasTypeOther() && !update.hasObservations())
			throw IsolationAlertException.requiredObservations(alertId);
	}
	private IsolationAlertException finalizedError(Integer alertId) {
		return IsolationAlertException.finalizedError(alertId);
	}
	private IsolationAlertException alertNotFound(Integer alertId) {
		return IsolationAlertException.alertNotFound(alertId);
	}
}
