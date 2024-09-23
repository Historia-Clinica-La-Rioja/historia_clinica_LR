package net.pladema.clinichistory.requests.servicerequests.repository;

import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.repository.domain.StudyOrderWorkListVo;

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
	public List<StudyOrderWorkListVo> execute(Integer institutionId, List<String> categories) {

		log.debug("Input parameters -> institutionId: {}, categories: {}", institutionId, categories);

		String sqlString = "SELECT NEW net.pladema.clinichistory.requests.servicerequests.repository.domain.StudyOrderWorkListVo( "
				+ "sr.id, sr.patientId, pe.firstName, pe.lastName, pe.identificationNumber, pe.identificationTypeId, pe.genderId, pe.birthDate, "
				+ "s.sctid, s.pt, sr.studyType, COALESCE(sr.requiresTransfer, false), sr.sourceTypeId, sr.deferredDate) "
				+ "FROM ServiceRequest sr "
				+ "JOIN Document d ON sr.id = d.sourceId "
				+ "JOIN DocumentDiagnosticReport ddr ON d.id = ddr.id.documentId "
				+ "JOIN DiagnosticReport dr ON ddr.id.diagnosticReportId = dr.id "
				+ "JOIN Snomed s ON dr.snomedId = s.id "
				+ "JOIN Patient AS p ON (sr.patientId = p.id) "
				+ "JOIN Person AS pe ON (pe.id = p.personId) "
				+ "WHERE sr.institutionId = :institutionId "
				+ "AND dr.statusId = '1' "
				+ "AND sr.categoryId IN :categories "
				+ "AND (sr.sourceTypeId = 0 OR sr.sourceTypeId = 4) "
				+ "AND (sr.deferredDate < CURRENT_TIMESTAMP OR sr.deferredDate IS NULL) "
				+ "ORDER BY dr.statusId ASC, "
				+ "CASE sr.studyType WHEN 1 THEN 0 WHEN 2 THEN 1 ELSE 2 END";

		Query query = entityManager.createQuery(sqlString, StudyOrderWorkListVo.class);
		query.setParameter("institutionId", institutionId)
				.setParameter("categories", categories);

		return query.getResultList();

	}


}
