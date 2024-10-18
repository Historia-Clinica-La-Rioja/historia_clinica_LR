package ar.lamansys.sgh.clinichistory.infrastructure.output;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.isolationalerts.exceptions.IsolationAlertException;
import ar.lamansys.sgh.clinichistory.application.ports.IsolationAlertStorage;
import ar.lamansys.sgh.clinichistory.domain.ips.IsolationAlertBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentIsolationAlertRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentIsolationAlert;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.IsolationAlertIsolationTypeRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.IsolationAlertRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.isolation.IsolationAlert;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.isolation.IsolationAlertIsolationType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class IsolationAlertStorageImpl implements IsolationAlertStorage {
	private final IsolationAlertIsolationTypeRepository isolationAlertIsolationTypeRepository;
	private final IsolationAlertRepository isolationAlertRepository;
	private final DocumentIsolationAlertRepository documentIsolationAlertRepository;
	private final DocumentRepository documentRepository;
	private final EntityManager entityManager;

	@Override
	public void save(Long documentId, List<IsolationAlertBo> isolationAlerts) {
		if (!documentRepository.existsById(documentId)) throw documentNotFound(documentId);
		isolationAlerts.forEach(alertBo -> {
			var newAlert = isolationAlertRepository.save(
				new IsolationAlert(
					alertBo.getHealthConditionId(),
					alertBo.getHealthConditionId(),
					alertBo.getCriticalityId(),
					alertBo.getEndDate(),
					alertBo.getObservations()
				)
			);
			alertBo.getTypeIds().forEach(
				typeId -> isolationAlertIsolationTypeRepository.save(
					new IsolationAlertIsolationType(newAlert.getId(), typeId)
				));
			documentIsolationAlertRepository.save(
				new DocumentIsolationAlert(documentId, newAlert.getId())
			);
		});
	}

	@Override
	public List<IsolationAlertBo> fetch(Long documentId) {

		String sqlString = "" +
		"SELECT " +
		"	isolation_alert.id, " +
		"	isolation_alert.health_condition_id, " +
		"	snomed.sctid, " +
		"	snomed.pt, " +
		"	(SELECT array_agg(isolation_alert_isolation_type.isolation_type_id) " +
		"		FROM isolation_alert_isolation_type " +
		"		WHERE (isolation_alert_isolation_type.isolation_alert_id = isolation_alert.id) " +
		"	), " +
		"	isolation_alert.isolation_criticality_id, " +
		"	isolation_alert.end_date, " +
		"	isolation_alert.observations " +
		" " +
		"FROM " +
		"	isolation_alert " +
		"	JOIN document_isolation_alert " +
		"		ON (document_isolation_alert.isolation_alert_id = isolation_alert.id) " +
		"	JOIN health_condition " +
		"		ON (health_condition.id = isolation_alert.health_condition_id) " +
		"	JOIN snomed " +
		"		ON (health_condition.snomed_id = snomed.id) " +
		"WHERE  "  +
		"	document_isolation_alert.document_id = :documentId";

		List<Object[]> rows = entityManager
			.createNativeQuery(sqlString)
			.setParameter("documentId", documentId)
			.getResultList();
		return mapAlerts(rows);
	}

	private List<IsolationAlertBo> mapAlerts(List<Object[]> rows) {
		List<IsolationAlertBo> result = new ArrayList<>();
		for (var row : rows) {
			IsolationAlertBo newAlert = new IsolationAlertBo();
			newAlert.setId((Integer)row[0]);
			newAlert.setHealthConditionId((Integer)row[1]);
			newAlert.setHealthConditionSctid((String)row[2]);
			newAlert.setHealthConditionPt((String)row[3]);
			newAlert.setTypeIds(toShortList((Object[]) row[4]));
			newAlert.setCriticalityId((Short)row[5]);
			newAlert.setEndDate(((Date) row[6]).toLocalDate());
			newAlert.setObservations(row[7] == null ? null : (String) row[7]);
			result.add(newAlert);
		}

		return result;
	}

	private List<Short> toShortList(Object[] array) {
		if (array == null || array[0] == null)
			return new ArrayList<>();
		List<Short> resultList = Arrays.stream(array)
				.map(x -> (Short)x)
				.collect(Collectors.toList());
		return resultList;
	}

	private IsolationAlertException documentNotFound(Long documentId) {
		return IsolationAlertException.documentNotFound(documentId);
	}
}
