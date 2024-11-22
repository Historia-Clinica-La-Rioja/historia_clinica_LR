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
	public List<Object[]> execute(Integer institutionId, List<String> categories, List<Short> sourceTypeIds, String statusId, Short documentType, Short emergencyCareState, Short internmentEpisodeState, List<Short> patientTypes) {

		log.debug("Input parameters -> institutionId: {}, categories: {}, sourceTypeIds: {}, statusId: {}, documentType: {}, emergencyCareState: {}, internmentEpisodeState: {}, patientTypes: {}", institutionId, categories, sourceTypeIds, statusId, documentType, emergencyCareState, internmentEpisodeState, patientTypes);

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
					"    dr.status_id AS statusId, " +
					"    CASE " +
					"        WHEN sr.source_type_id = 0 THEN ie_bed.bed_number " +
					"        ELSE ec_bed.bed_number" +
					"    END AS bedNumber, " +
					"    CASE " +
					"        WHEN sr.source_type_id = 0 THEN ie_room.description " +
					"        ELSE ec_room.description " +
					"    END AS roomNumber, " +
					"    CASE " +
					"        WHEN sr.source_type_id = 0 THEN ie_sector_room.description " +
					"        ELSE CASE " +
					"			 WHEN ec_sector_room.description is not null THEN ec_sector_room.description" +
					"			 ELSE COALESCE(ec_sector_doctorsOffice.description, ec_sector_shockroom.description) " +
					"        END       " +
					"    END AS sector, " +
					"    CASE " +
					"        WHEN sr.source_type_id = 4 AND ec_bed.bed_number is null THEN ec_office.description " +
					"        ELSE null " +
					"    END AS doctorsOffice, " +
					"    CASE " +
					"        WHEN sr.source_type_id = 4 AND ec_bed.bed_number is null THEN ec_shockroom.description " +
					"        ELSE null " +
					"    END AS shockroom, " +
					"    p.type_id AS patientType, " +
					"    ec.reason AS emergencyCareReason " +
					"FROM service_request sr " +
					"JOIN document d ON sr.id = d.source_id " +
					"JOIN document_diagnostic_report ddr ON d.id = ddr.document_id " +
					"JOIN diagnostic_report dr ON ddr.diagnostic_report_id = dr.id " +
					"JOIN snomed s ON dr.snomed_id = s.id " +
					"JOIN patient p ON sr.patient_id = p.id " +
					"JOIN patient_type pty ON p.type_id = pty.id  " +
					"LEFT JOIN person pe ON p.person_id = pe.id " +
					"LEFT JOIN person_extended pex ON pe.id = pex.person_id " +
					"LEFT JOIN gender g ON pe.gender_id = g.id " +
					"JOIN diagnostic_report_last_modification drlm ON drlm.id = dr.id " +
					"LEFT JOIN ( " +
					"    SELECT DISTINCT ON (ie.patient_id) " +
					"        ie.patient_id, ie.bed_id, ie.created_on, " +
					"        bi.bed_number, ri.room_number, " +
					"        se.description AS sector_description " +
					"    FROM internment_episode ie " +
					"    JOIN bed bi ON ie.bed_id = bi.id " +
					"    JOIN room ri ON bi.room_id = ri.id " +
					"    LEFT JOIN sector se ON ri.sector_id = se.id " +
					"    WHERE ie.institution_id = :institutionId AND ie.deleted = FALSE " +
					"	 AND ie.status_id IN (:internmentEpisodeState) " +
					"    ORDER BY ie.patient_id, ie.created_on DESC " +
					") ie ON ie.patient_id = sr.patient_id " +
					"LEFT JOIN ( " +
					"    SELECT DISTINCT ON (ec.patient_id) " +
					"        ec.patient_id, ec.bed_id, ec.created_on, ec.doctors_office_id, ec.shockroom_id, ec.reason " +
					"    FROM emergency_care_episode ec " +
					"    WHERE ec.institution_id = :institutionId AND ec.deleted = FALSE " +
					"	 AND ec.emergency_care_state_id IN (:emergencyCareState) " +
					"    ORDER BY ec.patient_id, ec.created_on DESC " +
					") ec ON ec.patient_id = sr.patient_id " +
					"LEFT JOIN bed ec_bed ON ec.bed_id = ec_bed.id " +
					"LEFT JOIN bed ie_bed ON ie.bed_id = ie_bed.id " +
					"LEFT JOIN room ec_room ON ec_bed.room_id = ec_room.id " +
					"LEFT JOIN room ie_room ON ie_bed.room_id = ie_room.id " +
					"LEFT JOIN sector ec_sector_room ON ec_room.sector_id = ec_sector_room.id " +
					"LEFT JOIN sector ie_sector_room ON ie_room.sector_id = ie_sector_room.id " +
					"LEFT JOIN doctors_office ec_office ON ec.doctors_office_id = ec_office.id " +
					"LEFT JOIN shockroom ec_shockroom ON ec.shockroom_id = ec_shockroom.id  " +
					"LEFT JOIN sector ec_sector_doctorsOffice ON ec_office.sector_id = ec_sector_doctorsOffice.id " +
					"LEFT JOIN sector ec_sector_shockroom ON ec_shockroom.sector_id = ec_sector_shockroom.id " +
					"WHERE sr.institution_id = :institutionId " +
					"AND dr.status_id = :statusId " +
					"AND sr.category_id IN (:categories) " +
					"AND sr.source_type_id IN (:sourceTypeIds) " +
					"AND pty.id IN (:patientTypes)  " +
					"AND d.type_id = :documentType ";

		Query query = entityManager.createNativeQuery(sqlString);
		query.setParameter("institutionId", institutionId)
				.setParameter("categories", categories)
				.setParameter("sourceTypeIds", sourceTypeIds)
				.setParameter("statusId", statusId)
				.setParameter("documentType", documentType)
				.setParameter("emergencyCareState", emergencyCareState)
				.setParameter("patientTypes", patientTypes)
				.setParameter("internmentEpisodeState", internmentEpisodeState);

		return (List<Object[]>) query.getResultList();

	}


}
