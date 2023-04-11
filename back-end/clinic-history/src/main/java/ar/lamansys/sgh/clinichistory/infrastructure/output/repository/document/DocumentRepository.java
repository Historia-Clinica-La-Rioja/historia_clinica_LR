package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Document;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments.DocumentRepositoryCustom;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments.EmergencyCareEpisodeTriageSearchVo;
import ar.lamansys.sgx.shared.auditable.entity.Updateable;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DocumentRepository extends SGXAuditableEntityJPARepository<Document, Long>, DocumentRepositoryCustom {

    @Query(value = "SELECT d.updateable " +
            "FROM Document d " +
            "WHERE d.sourceId = :internmentEpisodeId " +
            "and d.sourceTypeId = " + SourceType.HOSPITALIZATION)
    List<Updateable> getUpdatablesDocuments(@Param("internmentEpisodeId") Integer internmentEpisodeId);

    @Query(value = "SELECT DISTINCT new  ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.ProcedureReduced(snomedPr.pt, pr.performedDate) " +
            "FROM DocumentProcedure AS dp " +
            "JOIN Procedure AS pr ON (pr.id = dp.pk.procedureId) " +
            "JOIN Snomed AS snomedPr ON (pr.snomedId = snomedPr.id) " +
            "WHERE dp.pk.documentId = :documentId " +
			"AND pr.statusId != :errorStatus")
    List<ProcedureReduced> getProceduresByDocuments(@Param("documentId") Long documentId, @Param("errorStatus") String errorStatus);

    @Transactional(readOnly = true)
    @Query(value = "SELECT d.id " +
            "FROM Document d " +
            "WHERE d.sourceId = :sourceId AND d.sourceTypeId = :sourceTypeId")
    List<Long> findBySourceIdAndSourceTypeId(@Param("sourceId") Integer sourceId, @Param("sourceTypeId") Short sourceTypeId );

	@Transactional(readOnly = true)
	@Query(value = "SELECT d.sourceTypeId " +
			"FROM Document d " +
			"WHERE d.id = :documentId ")
	Short getSourceTypeId(@Param("documentId") Long documentId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT d.institutionId " +
			"FROM Document d " +
			"WHERE d.id = :documentId ")
	Integer getInstitutionIdFromDocument(@Param("documentId") Long documentId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT d.id " +
			"FROM Document d " +
			"WHERE d.sourceId IN (:sourceIds) " +
			"AND d.sourceTypeId = :sourceTypeId")
	List<Long> getIdsBySourceIdType(@Param("sourceIds") List<Integer> sourceIds, @Param("sourceTypeId") Short sourceTypeId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT d.id " +
			"FROM Document d " +
			"WHERE d.patientId = :patientId")
	List<Long> getIdsByPatientId(@Param("patientId") Integer patientId);
	
	@Transactional(readOnly = true)
	@Query(value = "SELECT DISTINCT NEW ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments.EmergencyCareEpisodeTriageSearchVo(t.id," +
			"d.id, d.creationable.createdOn, up.pk.userId, p.firstName, p.lastName, pe.nameSelfDetermination, t.notes, dt.description) " +
			"FROM Document d " +
			"JOIN UserPerson up ON (up.pk.userId = d.creationable.createdBy) " +
			"JOIN Person p ON (p.id = up.pk.personId) " +
			"JOIN PersonExtended pe ON (pe.id = p.id) " +
			"JOIN DocumentRiskFactor drf ON (drf.pk.documentId = d.id) " +
			"JOIN ObservationRiskFactor orf ON (orf.id = drf.pk.observationRiskFactorId) " +
			"JOIN TriageRiskFactors trf ON (trf.pk.observationRiskFactorId = orf.id) " +
			"JOIN Triage t ON (t.id = trf.pk.triageId) " +
			"JOIN DocumentType dt ON (dt.id = d.typeId) " +
			"WHERE dt.id = " + DocumentType.TRIAGE + " " +
			"AND d.sourceId = :emergencyCareEpisodeId")
	List<EmergencyCareEpisodeTriageSearchVo> getTriageDocumentData(@Param("emergencyCareEpisodeId") Integer emergencyCareEpisodeId);
}
