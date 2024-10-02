package net.pladema.clinichistory.requests.servicerequests.repository;

import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import javax.persistence.Query;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Repository
public class StudyWorkListRepositoryImpl implements StudyWorkListRepository {

	private final EntityManager entityManager;

	@Transactional(readOnly = true)
	@Override
	public List<Object[]> execute(Integer institutionId, List<String> categories, List<Short> sourceTypeIds, String statusId, Short documentType) {

		log.debug("Input parameters -> institutionId: {}, categories: {}, sourceTypeIds: {}, statusId: {}, documentType: {}", institutionId, categories, sourceTypeIds, statusId, documentType);

		String sqlString =
			"WITH diagnostic_report_last_modification AS (SELECT DISTINCT ON " +
				"(sr.id, dr.snomed_id, dr.health_condition_id) dr.id, dr.status_id " +
				"FROM diagnostic_report dr " +
				"JOIN document_diagnostic_report ddr ON ddr.diagnostic_report_id = dr.id " +
				"JOIN document d ON d.id = ddr.document_id " +
				"JOIN service_request sr ON sr.id = d.source_id " +
				"ORDER BY sr.id, dr.snomed_id, dr.health_condition_id, dr.created_on DESC ) " +
				"SELECT " +
				"    sr.id AS studyId, " +
				"    sr.patient_id AS patientId, " +
				"    pe.first_name AS firstName, " +
				"    pe.middle_names AS middleNames, " +
				"    pe.last_name AS lastName, " +
				"    pe.other_last_names AS otherLastNames, " +
				"    pex.name_self_determination AS nameSelfDetermination, " +
				"    pe.identification_number AS identificationNumber, " +
				"    pe.identification_type_id AS identificationTypeId, " +
				"    pe.gender_id AS genderId, " +
				"    g.description AS genderDescription, " +
				"    pe.birth_date AS birthDate, " +
				"    s.sctid AS snomedSctid, " +
				"    s.pt AS snomedPt, " +
				"    sr.study_type_id AS studyType, " +
				"    sr.requires_transfer AS requiresTransfer, " +
				"    sr.source_type_id AS sourceTypeId, " +
				"    sr.deferred_date AS deferredDate, " +
				"    sr.created_on AS createdDate, " +
				"    dr.status_id AS statusId " +
				"FROM service_request sr " +
				"JOIN document d ON sr.id = d.source_id " +
				"JOIN document_diagnostic_report ddr ON d.id = ddr.document_id " +
				"JOIN diagnostic_report dr ON ddr.diagnostic_report_id = dr.id " +
				"JOIN snomed s ON dr.snomed_id = s.id " +
				"JOIN patient p ON sr.patient_id = p.id " +
				"JOIN person pe ON pe.id = p.person_id " +
				"JOIN person_extended pex ON pe.id = pex.person_id " +
				"LEFT JOIN gender g ON pe.gender_id = g.id " +
				"JOIN diagnostic_report_last_modification drlm ON drlm.id = dr.id " +
				"WHERE sr.institution_id = :institutionId " +
				"AND dr.status_id = :statusId " +
				"AND sr.category_id IN (:categories) " +
				"AND sr.source_type_id IN (:sourceTypeIds) " +
				"AND d.type_id = :documentType ";

		Query query = entityManager.createNativeQuery(sqlString);
		query.setParameter("institutionId", institutionId)
				.setParameter("categories", categories)
				.setParameter("sourceTypeIds", sourceTypeIds)
				.setParameter("statusId", statusId)
				.setParameter("documentType", documentType);

		return (List<Object[]>) query.getResultList();
	}


}
