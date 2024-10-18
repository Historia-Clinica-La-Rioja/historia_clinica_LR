package ar.lamansys.sgh.clinichistory.application.ports;

import ar.lamansys.sgh.clinichistory.domain.ips.IsolationAlertBo;

import java.util.List;

public interface IsolationAlertStorage {
	void save(Long documentId, List<IsolationAlertBo> isolationAlerts);
	List<IsolationAlertBo> fetch(Long documentId);
}
