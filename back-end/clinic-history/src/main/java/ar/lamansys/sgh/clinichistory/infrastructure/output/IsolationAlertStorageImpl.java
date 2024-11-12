package ar.lamansys.sgh.clinichistory.infrastructure.output;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EIsolationCriticality;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EIsolationStatus;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EIsolationType;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.isolationalerts.exceptions.IsolationAlertException;
import ar.lamansys.sgh.clinichistory.application.ports.IsolationAlertStorage;
import ar.lamansys.sgh.clinichistory.domain.isolation.FetchPatientIsolationAlertBo;
import ar.lamansys.sgh.clinichistory.domain.isolation.IsolationAlertBo;
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

	/**
	 * When an evolution note is modified the alert bo's have an id. The modified alerts are stored
	 * as new rows that point to the alert being modified.
	 * See {@link IsolationAlert} parentId field.
	 */
	@Override
	public void save(Long documentId, List<IsolationAlertBo> isolationAlerts) {
		if (!documentRepository.existsById(documentId)) throw documentNotFound(documentId);
		isolationAlerts.forEach(alertBo -> {
			var newAlert = isolationAlertRepository.save(
				new IsolationAlert(
					null,
					alertBo.getHealthConditionId(),
					alertBo.getCriticalityId(),
					alertBo.getEndDate(),
					alertBo.getObservations(),
					EIsolationStatus.ONGOING.getId(),
					alertBo.getId()
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
	public List<IsolationAlertBo> fetchByDocumentId(Long documentId) {
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
		"	isolation_alert.observations, " +
		"	isolation_alert.isolation_status_id " +
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
		return mapToIsolationAlertBo(rows);
	}

	@Override
	public List<FetchPatientIsolationAlertBo> findByPatientId(Integer patientId) {
		String sqlString = "" +
		"SELECT " +
		"	isolation_alert.id, " + //0
		"	isolation_alert.health_condition_id, " + //1
		"	snomed.sctid, " + //2
		"	snomed.pt, " + //3
		"	(SELECT array_agg(isolation_alert_isolation_type.isolation_type_id) " +
		"		FROM isolation_alert_isolation_type " +
		"		WHERE (isolation_alert_isolation_type.isolation_alert_id = isolation_alert.id) " +
		"	), " + //4
		"	isolation_alert.isolation_criticality_id, " + //5
		"	isolation_alert.end_date, " + //6
		"	isolation_alert.observations, " + //7
		"	isolation_alert.created_by, " + //8
		"	isolation_alert.created_on, " +//9
		"	isolation_alert.isolation_status_id " +//10
		" " +
		"FROM " +
		"	isolation_alert " +
		"	JOIN document_isolation_alert " +
		"		ON (document_isolation_alert.isolation_alert_id = isolation_alert.id) " +
		"	JOIN document " +
		"		ON (document.id = document_isolation_alert.document_id) " +
		"	JOIN health_condition " +
		"		ON (health_condition.id = isolation_alert.health_condition_id) " +
		"	JOIN snomed " +
		"		ON (health_condition.snomed_id = snomed.id) " +
		"WHERE "  +
		"	document.patient_id = :patientId AND document.deleted = false " +
		"ORDER BY isolation_alert.created_on DESC";


		List<Object[]> rows = entityManager
				.createNativeQuery(sqlString)
				.setParameter("patientId", patientId)
				.getResultList();
		return mapToFetchPatientIsolationAlertBo(rows);
		
	}

	@Override
	public Optional<FetchPatientIsolationAlertBo> findByAlertId(Integer alertId) {
		String sqlString = "" +
				"SELECT " +
				"	isolation_alert.id, " + //0
				"	isolation_alert.health_condition_id, " + //1
				"	snomed.sctid, " + //2
				"	snomed.pt, " + //3
				"	(SELECT array_agg(isolation_alert_isolation_type.isolation_type_id) " +
				"		FROM isolation_alert_isolation_type " +
				"		WHERE (isolation_alert_isolation_type.isolation_alert_id = isolation_alert.id) " +
				"	), " + //4
				"	isolation_alert.isolation_criticality_id, " + //5
				"	isolation_alert.end_date, " + //6
				"	isolation_alert.observations, " + //7
				"	isolation_alert.created_by, " + //8
				"	isolation_alert.created_on, " +//9
				"	isolation_alert.isolation_status_id " +//10
				" " +
				"FROM " +
				"	isolation_alert " +
				"	JOIN document_isolation_alert " +
				"		ON (document_isolation_alert.isolation_alert_id = isolation_alert.id) " +
				"	JOIN document " +
				"		ON (document.id = document_isolation_alert.document_id) " +
				"	JOIN health_condition " +
				"		ON (health_condition.id = isolation_alert.health_condition_id) " +
				"	JOIN snomed " +
				"		ON (health_condition.snomed_id = snomed.id) " +
				"WHERE "  +
				"	isolation_alert.id = :alertId AND document.deleted = false";

		List<Object[]> rows = entityManager
				.createNativeQuery(sqlString)
				.setParameter("alertId", alertId)
				.getResultList();
		return rows
			.stream()
			.findFirst()
			.map(xx -> toFetchPatientIsolationAlertBo(xx));

	}

	private List<IsolationAlertBo> mapToIsolationAlertBo(List<Object[]> rows) {
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
			newAlert.setStatusId((Short) row[8]);
			result.add(newAlert);
		}
		return result;
	}

	private List<FetchPatientIsolationAlertBo> mapToFetchPatientIsolationAlertBo(List<Object[]> rows) {
		List<FetchPatientIsolationAlertBo> result = new ArrayList<>();
		for (var row : rows) {
			FetchPatientIsolationAlertBo newAlert = toFetchPatientIsolationAlertBo(row);
			result.add(newAlert);
		}
		return result;
	}

	private FetchPatientIsolationAlertBo toFetchPatientIsolationAlertBo(Object[] row) {
		FetchPatientIsolationAlertBo newAlert = new FetchPatientIsolationAlertBo();
		newAlert.setIsolationAlertId((Integer) row[0]);
		newAlert.setHealthConditionId((Integer) row[1]);
		newAlert.setHealthConditionSctid((String) row[2]);
		newAlert.setHealthConditionPt((String) row[3]);
		var typeIds = toShortList((Object[]) row[4]);
		newAlert.setTypes(typeIds.stream().map(EIsolationType::map).collect(Collectors.toList()));
		newAlert.setCriticality(EIsolationCriticality.map((Short) row[5]));
		newAlert.setEndDate(((Date) row[6]).toLocalDate());
		newAlert.setObservations(row[7] == null ? null : (String) row[7]);
		newAlert.setCreatedBy((Integer) row[8]);
		newAlert.setStartDate(row[9] != null ? ((Timestamp) row[9]).toLocalDateTime() : null);
		newAlert.setStatus(EIsolationStatus.map((Short) row[10]));
		return newAlert;
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
