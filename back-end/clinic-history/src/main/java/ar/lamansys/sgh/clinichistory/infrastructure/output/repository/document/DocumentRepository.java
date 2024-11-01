package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Document;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentDownloadDataVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments.DocumentRepositoryCustom;
import ar.lamansys.sgx.shared.auditable.entity.Updateable;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends SGXAuditableEntityJPARepository<Document, Long>, DocumentRepositoryCustom {

    @Query(value = "SELECT d.updateable " +
            "FROM Document d " +
            "WHERE d.sourceId = :internmentEpisodeId " +
            "and d.sourceTypeId = " + SourceType.HOSPITALIZATION)
    List<Updateable> getUpdatablesDocuments(@Param("internmentEpisodeId") Integer internmentEpisodeId);

    @Transactional(readOnly = true)
    @Query(value = "SELECT d.id " +
            "FROM Document d " +
            "WHERE d.sourceId = :sourceId AND d.sourceTypeId = :sourceTypeId "+
			"ORDER BY d.id DESC ")
    List<Long> findBySourceIdAndSourceTypeId(@Param("sourceId") Integer sourceId, @Param("sourceTypeId") Short sourceTypeId );

	@Transactional(readOnly = true)
	@Query(value = "SELECT d.id " +
			"FROM Document d " +
			"WHERE d.sourceId = :sourceId AND d.typeId = :typeId")
	List<Long> findBySourceIdAndTypeId(@Param("sourceId") Integer sourceId, @Param("typeId") Short typeId);

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
	@Query(value = "SELECT DISTINCT NEW ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentDownloadDataVo(d.id, df.filename) " +
			"FROM Document d " +
			"JOIN DocumentFile df ON (df.id = d.id) " +
			"JOIN DocumentTriage dt ON (dt.pk.documentId = d.id) " +
			"JOIN Triage t ON (t.id = dt.pk.triageId) " +
			"WHERE t.id = :triageId ")
	DocumentDownloadDataVo getDocumentIdByTriageId(@Param("triageId") Integer triageId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT NEW ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentDownloadDataVo(df.id, df.filename) " +
			"FROM AppointmentOrderImage aoi " +
			"JOIN DocumentFile df ON (aoi.documentId = df.id) " +
			"WHERE aoi.pk.appointmentId = :appointmentId ")
	DocumentDownloadDataVo getImageReportByAppointmentId(@Param("appointmentId") Integer appointmentId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT d " +
			"FROM Document d " +
			"WHERE d.id = :documentId " +
			"AND d.typeId = :typeId")
	Optional<Document> getDocumentByIdAndTypeId(@Param("documentId") Long documentId, @Param("typeId") Short typeId);

}
