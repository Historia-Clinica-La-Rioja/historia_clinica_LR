package ar.lamansys.sgh.clinichistory.application.isolationalerts;

import ar.lamansys.sgh.clinichistory.application.isolationalerts.exceptions.IsolationAlertException;
import ar.lamansys.sgh.clinichistory.application.ports.IsolationAlertStorage;
import ar.lamansys.sgh.clinichistory.domain.isolation.IsolationAlertBo;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedHealthConditionPort;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SaveDocumentIsolationAlerts {
	private final IsolationAlertStorage isolationAlertStorage;
	private final SharedHealthConditionPort sharedHealthConditionPort;

	@Transactional
	public void run(Long documentId, List<IsolationAlertBo> isolationAlerts) {

		//Find the actual health condition ids for each alert
		//The only valid conditions for each alert are the ones attached to
		//this document
		for (var alert: isolationAlerts) {
			var healthConditionId = sharedHealthConditionPort
				.getHealthConditionIdByDocumentIdAndSnomedConcept(
					documentId,
					(int) SourceType.EMERGENCY_CARE,
					alert.getHealthConditionSctid(),
					alert.getHealthConditionPt())
				.orElseThrow(() -> conditionNotFound(documentId, alert));
			alert.setHealthConditionId(healthConditionId);
		}

		isolationAlertStorage.save(documentId, isolationAlerts);
	}

	private IsolationAlertException conditionNotFound(Long documentId, IsolationAlertBo alert) {
		return IsolationAlertException.healthConditionNotFound(documentId, alert.getHealthConditionSctid(),
			alert.getHealthConditionPt());
	}
}
