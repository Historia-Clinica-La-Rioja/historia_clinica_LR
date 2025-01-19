package ar.lamansys.sgh.clinichistory.infrastructure.output;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EIsolationCriticality;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EIsolationStatus;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EIsolationType;

import ar.lamansys.sgh.clinichistory.domain.isolation.IsolationAlertForPdfDocumentBo;

import lombok.AllArgsConstructor;
import lombok.Getter;

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

import static ar.lamansys.sgx.shared.security.UserInfo.getCurrentAuditor;

@RequiredArgsConstructor
@Service
public class IsolationAlertStorageImpl implements IsolationAlertStorage {
	private final IsolationAlertIsolationTypeRepository isolationAlertIsolationTypeRepository;
	private final IsolationAlertRepository isolationAlertRepository;
	private final DocumentIsolationAlertRepository documentIsolationAlertRepository;
	private final DocumentRepository documentRepository;
	private final EntityManager entityManager;

	@Getter
	@AllArgsConstructor
	private class CreationData {
		Integer createdById;
		LocalDateTime createdOn;
	}

	/**
	 * When an evolution note is modified, its alerts, sent in the alerts array by the frontend, can be in one of
	 * these states:
	 * 1. The alert is missing from the array. This means that the alert was deleted. There's nothing to be done.
	 * The old evolution note will be marked as deleted and all of its alerts will be considered deleted as well.
	 * 2. The alert is present in the array and has a null id. This alert is new and was added during the evolution
	 * note's edit. A new row for this alert is created and linked to the corresponding document.
	 * 3. The alert is present in the array and has a non-null id. This alert is being modified. Instead of modifying
	 * the existing row, a new one is created with the modified values. This new rows parentId stores the id of the
	 * modified alert. This allows to keep track of an alert's history.
	 *
	 * See {@link IsolationAlert} parentId field.
	 */
	@Override
	public void save(Long documentId, List<IsolationAlertBo> isolationAlerts) {
		if (!documentRepository.existsById(documentId)) throw documentNotFound(documentId);
		isolationAlerts.forEach(alertBo -> {
			saveOne(
				documentId,
				alertBo.getId(),
				alertBo.getHealthConditionId(),
				alertBo.getTypeIds(),
				alertBo.getCriticalityId(),
				alertBo.getEndDate(),
				alertBo.getObservations(),
				alertBo.getStatusId()
			);
		});
	}

	private IsolationAlert saveOne(
		Long documentId,
		Integer id,
		Integer healthConditionId,
		List<Short> typeIds,
		Short criticalityId,
		LocalDate endDate,
		String observations,
		Short statusId
	) {
		var newAlert = isolationAlertRepository.save(
			IsolationAlert.withoutId(
				healthConditionId,
				criticalityId,
				endDate,
				observations,
				statusId,
				id
			)
		);
		typeIds.forEach(
			typeId -> isolationAlertIsolationTypeRepository.save(
				new IsolationAlertIsolationType(newAlert.getId(), typeId)
			));
		documentIsolationAlertRepository.save(
			new DocumentIsolationAlert(documentId, newAlert.getId())
		);
		return newAlert;
	}

	/**
	 * How isModified is computed:
	 * When true it means that the the alert, that was created in an evolution note, was modified outside the evolution
	 * note. The frontend, when showing the alert, uses isModified to show that the alert is not as it was originally
	 * created.
	 * If the parent alert belongs to the same document (via document_isolation_alert)
	 * and is marked as deleted it means that the alert was modified outside the evolution note.
	 * This happens because when an alert is edited (in the problems tab > patient alerts > finalize) a
	 * new row is created with the new values and the original one is marked as deleted.
	 */
	@Override
	public List<IsolationAlertBo> findByDocumentId(Long documentId) {
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
		"	isolation_alert.isolation_status_id, " +//10
		"	isolation_alert.parent_id, " +//11
		"	parent_document_isolation_alert.document_id " +//12
		" " +
		"FROM " +
		"	isolation_alert " +
		"	JOIN document_isolation_alert " +
		"		ON (document_isolation_alert.isolation_alert_id = isolation_alert.id) " +
		"	JOIN health_condition " +
		"		ON (health_condition.id = isolation_alert.health_condition_id) " +
		"	JOIN snomed " +
		"		ON (health_condition.snomed_id = snomed.id) " +
		"	LEFT JOIN document_isolation_alert parent_document_isolation_alert " +
		"		ON (parent_document_isolation_alert.isolation_alert_id = isolation_alert.parent_id) " +
		"WHERE  "  +
		"	document_isolation_alert.document_id = :documentId" +
		"	AND isolation_alert.deleted = false"
		;

		List<Object[]> rows = entityManager
			.createNativeQuery(sqlString)
			.setParameter("documentId", documentId)
			.getResultList();
		return rows.stream().map(row -> mapToIsolationAlertBo(documentId, row)).collect(Collectors.toList());
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
		"	isolation_alert.created_on, " + //9
		"	isolation_alert.isolation_status_id, " + //10
		"	isolation_alert.parent_id, " + //11
		"	document_isolation_alert.document_id " + //12
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
		"	document.patient_id = :patientId " +
		"	AND document.deleted = false " +
		"	AND isolation_alert.deleted = false " +
		"ORDER BY isolation_alert.created_on DESC";


		List<Object[]> rows = entityManager
				.createNativeQuery(sqlString)
				.setParameter("patientId", patientId)
				.getResultList();

		var patientAlerts = rows.stream()
			.map(row ->  toFetchPatientIsolationAlertBo(row))
			.collect(Collectors.toList());

		return patientAlerts;
		
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
				"	isolation_alert.created_on, " + //9
				"	isolation_alert.isolation_status_id, " + //10
				"	isolation_alert.parent_id, " + //11
				"	document_isolation_alert.document_id " + //12
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
				"	isolation_alert.id = :alertId " +
				"	AND document.deleted = false " +
				"	AND isolation_alert.deleted = false";

		List<Object[]> rows = entityManager
				.createNativeQuery(sqlString)
				.setParameter("alertId", alertId)
				.getResultList();
		return rows
			.stream()
			.findFirst()
			.map(alert -> toFetchPatientIsolationAlertBo(alert));
	}

	@Override
	public List<IsolationAlertForPdfDocumentBo> findByDocumentIdForDocumentPdf(Long documentId) {
		return this.findByDocumentId(documentId)
			.stream()
			.map(alert -> new IsolationAlertForPdfDocumentBo(alert))
			.collect(Collectors.toList());
	}

	/**
	 * 1. Marks the alert with id = {@alertId} as deleted
	 * 2. Creates a new alert with status cancelled, endDate=today and parentId = {@alertId}
	 */
	@Override
	public Optional<Integer> cancel(Integer alertId) {
		var fullSourceAlert = findByAlertId(alertId).orElseThrow(() -> alertNotFound(alertId));
		var documentId = documentIsolationAlertRepository.findByPk_isolationAlertId(alertId)
			.orElseThrow(() -> alertNotFound(alertId))
			.getPk().getDocumentId();

		//Create a new alert with status cancelled and endDate=now
		var savedFinalizedAlert = this.saveOne(
			documentId,
			alertId, //The new one will have this as the parentId
			fullSourceAlert.getHealthConditionId(),
			fullSourceAlert.getTypeIds(),
			fullSourceAlert.getCriticalityId(),
			LocalDate.now(),
			fullSourceAlert.getObservations(),
			EIsolationStatus.CANCELLED.getId()
		);

		//Mark the old one as deleted
		markDeleted(alertId);

		return Optional.of(savedFinalizedAlert.getId());
	}

	/**
	 * Same procedure as cancel but update criticalityId, endDate and observations fields
	 */
	@Override
	public Optional<Integer> update(Integer alertId, Short criticalityId, LocalDate endDate, String observations) {
		var fullSourceAlert = findByAlertId(alertId).orElseThrow(() -> alertNotFound(alertId));
		var documentId = documentIsolationAlertRepository.findByPk_isolationAlertId(alertId)
				.orElseThrow(() -> alertNotFound(alertId))
				.getPk().getDocumentId();

		//Create a new alert with the updatedValues
		var savedFinalizedAlert = this.saveOne(
				documentId,
				alertId, //The new one will have this as the parentId
				fullSourceAlert.getHealthConditionId(),
				fullSourceAlert.getTypeIds(),
				criticalityId,
				endDate,
				observations,
				fullSourceAlert.getStatusId()
		);

		//Mark the old one as deleted
		markDeleted(alertId);

		return Optional.of(savedFinalizedAlert.getId());
	}

	private void markDeleted(Integer alertId) {
		var sourceAlert = isolationAlertRepository.findById(alertId).orElseThrow(() -> alertNotFound(alertId));
		sourceAlert.setDeleted(true);
		sourceAlert.setDeletedBy(getCurrentAuditor());
		sourceAlert.setDeletedOn(LocalDateTime.now());
		isolationAlertRepository.save(sourceAlert);
	}

	private IsolationAlertBo mapToIsolationAlertBo(Long documentId, Object[] row) {
		IsolationAlertBo newAlert = new IsolationAlertBo();
		newAlert.setId((Integer)row[0]);
		newAlert.setHealthConditionId((Integer)row[1]);
		newAlert.setHealthConditionSctid((String)row[2]);
		newAlert.setHealthConditionPt((String)row[3]);
		newAlert.setTypeIds(toShortList((Object[]) row[4]));
		newAlert.setCriticalityId((Short)row[5]);
		newAlert.setEndDate(((Date) row[6]).toLocalDate());
		newAlert.setObservations(row[7] == null ? null : (String) row[7]);
		newAlert.setUpdatedById((Integer) row[8]);
		newAlert.setUpdatedOn(row[9] != null ? ((Timestamp) row[9]).toLocalDateTime() : null);
		newAlert.setStatusId((Short) row[10]);
		newAlert.setIsModified(
			row[11] != null && //parent_id
			row[12] != null && //parent_id -> document_id
			documentId.equals(((BigInteger)row[12]).longValue())
		);

		return newAlert;
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
		newAlert.setStatus(EIsolationStatus.map((Short) row[10]));
		newAlert.setIsModified(row[11] != null);
		newAlert.setDocumentId(((BigInteger)row[12]).longValue());

		/**
		 * Audit data
		 * Alerts are never modified. A new one is created on each edit.
		 * Author and start date must be computed. The createdBy/On of its oldest parent is the
		 * actual creation data of the alert.
		 * UpdatedBy/On is equal to createdBy/On
		 */
		CreationData creationData = findAlertCreationData((Integer) row[0]);
		newAlert.setCreatedById(creationData.getCreatedById());
		newAlert.setStartDate(creationData.getCreatedOn());
		newAlert.setUpdatedById((Integer) row[8]);
		newAlert.setUpdatedOn(row[9] != null ? ((Timestamp) row[9]).toLocalDateTime() : null);

		return newAlert;
	}

	/**
	 * Traverses the parent_id chain to get to the original/root alert
	 */
	private CreationData findAlertCreationData(Integer alertId) {
		String sqlString = "" +
				"WITH RECURSIVE children AS ( " +
				"  SELECT " +
				"    id, parent_id, created_by, created_on " +
				"  FROM " +
				"    isolation_alert " +
				"  WHERE " +
				"    id = :alertId " +
				"  UNION " +
				"  SELECT " +
				"    ia.id, ia.parent_id, ia.created_by, ia.created_on " +
				"  FROM " +
				"    isolation_alert ia " +
				"    INNER JOIN children c ON ia.id = c.parent_id " +
				") " +
				"SELECT id, created_by, created_on " +
				"FROM children " +
				"WHERE children.parent_id is NULL;";

		List<Object[]> rows = entityManager
				.createNativeQuery(sqlString)
				.setParameter("alertId", alertId)
				.getResultList();
		if (rows.isEmpty()) return null;
		Object[] row = rows.get(0);
		return new CreationData(
				(Integer) row[1],
				((Timestamp) row[2]).toLocalDateTime()
		);
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

	private IsolationAlertException alertNotFound(Integer alertId) {
		return IsolationAlertException.alertNotFound(alertId);
	}

}
