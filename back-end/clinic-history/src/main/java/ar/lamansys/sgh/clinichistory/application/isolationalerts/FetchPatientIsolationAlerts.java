package ar.lamansys.sgh.clinichistory.application.isolationalerts;

import ar.lamansys.sgh.clinichistory.application.isolationalerts.exceptions.IsolationAlertException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.ports.IsolationAlertStorage;
import ar.lamansys.sgh.clinichistory.domain.isolation.FetchPatientIsolationAlertBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.ProfessionalInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FetchPatientIsolationAlerts {

	private final IsolationAlertStorage isolationAlertStorage;
	private final SharedStaffPort sharedStaffPort;

	public List<FetchPatientIsolationAlertBo> findByPatientId(Integer patientId) {
		var alerts = isolationAlertStorage.findByPatientId(patientId);
		alerts.stream().forEach(
			alert -> alert.setAuthor(getAuthor(alert.getCreatedBy()))
		);
		return alerts.stream().sorted(this::expiredLast).collect(Collectors.toList());
	}

	private int expiredLast(FetchPatientIsolationAlertBo left, FetchPatientIsolationAlertBo right) {
		if (
			(left.getStatus().isExpired() || left.getStatus().isCancelled()) &&
			!(right.getStatus().isExpired() || right.getStatus().isCancelled())
		)
			return 1;
		else if (
			!(left.getStatus().isExpired() || left.getStatus().isCancelled()) &&
			(right.getStatus().isExpired() || right.getStatus().isCancelled())
		)
			return -1;
		return 0;
	}

	public FetchPatientIsolationAlertBo findByAlertId(Integer alertId) {
		var alert = isolationAlertStorage.findByAlertId(alertId).orElseThrow(() -> alertNotFound(alertId));
		alert.setAuthor(getAuthor(alert.getCreatedBy()));
		return alert;
	}

	private ProfessionalInfoDto getAuthor(Integer createdBy) {
		return sharedStaffPort.getProfessionalCompleteInfo(createdBy);
	}

	private IsolationAlertException alertNotFound(Integer alertId) {
		return IsolationAlertException.alertNotFound(alertId);
	}
}
