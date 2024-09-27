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
	public List<StudyOrderWorkListVo> execute(Integer institutionId, List<String> categories, List<Short> sourceTypeIds, String statusId, Short documentType) {

		log.debug("Input parameters -> institutionId: {}, categories: {}, sourceTypeIds: {}, statusId: {}, documentType: {}", institutionId, categories, sourceTypeIds, statusId, documentType);

		String sqlString = "SELECT NEW net.pladema.clinichistory.requests.servicerequests.repository.domain.StudyOrderWorkListVo( "
				+ "sr.id, sr.patientId, pe.firstName, pe.middleNames, pe.lastName, pe.otherLastNames, pex.nameSelfDetermination, pe.identificationNumber, pe.identificationTypeId, pe.genderId, g.description, pe.birthDate, "
				+ "s.sctid, s.pt, sr.studyType, sr.requiresTransfer, sr.sourceTypeId, sr.deferredDate, sr.creationable.createdOn) "
				+ "FROM ServiceRequest sr "
				+ "JOIN Document d ON sr.id = d.sourceId "
				+ "JOIN DocumentDiagnosticReport ddr ON d.id = ddr.id.documentId "
				+ "JOIN DiagnosticReport dr ON ddr.id.diagnosticReportId = dr.id "
				+ "JOIN Snomed s ON dr.snomedId = s.id "
				+ "JOIN Patient AS p ON (sr.patientId = p.id) "
				+ "JOIN Person AS pe ON (pe.id = p.personId) "
				+ "JOIN PersonExtended pex ON (pe.id = pex.id) "
				+ "LEFT JOIN Gender g ON (pe.genderId = g.id) "
				+ "WHERE sr.institutionId = :institutionId "
				+ "AND dr.statusId = :statusId "
				+ "AND sr.categoryId IN :categories "
				+ "AND sr.sourceTypeId IN :sourceTypeIds "
				+ "AND d.typeId = :documentType "
				+ "AND d.id IN ( "
				+ "    SELECT ddr1.id.documentId "
				+ "    FROM DocumentDiagnosticReport ddr1 "
				+ "    GROUP BY ddr1.id.documentId "
				+ "    HAVING COUNT(ddr1.id.documentId) = 1 "
				+ ") ";

		Query query = entityManager.createQuery(sqlString);
		query.setParameter("institutionId", institutionId)
				.setParameter("categories", categories)
				.setParameter("sourceTypeIds", sourceTypeIds)
				.setParameter("statusId", statusId)
				.setParameter("documentType", documentType);

		return query.getResultList();

	}


}
