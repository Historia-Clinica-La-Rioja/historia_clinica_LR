package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.surgicalreport.SurgicalReport;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SurgicalReportRepository extends JpaRepository<SurgicalReport, Integer> {

	@Transactional(readOnly = true)
	@Query(value = "SELECT s.startDateTime " +
			"FROM SurgicalReport s " +
			"WHERE s.documentId = :documentId " )
	LocalDateTime getStartDateTime(@Param("documentId") Long documentId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT s.endDateTime " +
			"FROM SurgicalReport s " +
			"WHERE s.documentId = :documentId " )
	LocalDateTime getEndDateTime(@Param("documentId") Long documentId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT n.description " +
			"FROM SurgicalReport s " +
			"JOIN Note n ON (n.id = s.noteId) " +
			"WHERE s.documentId = :documentId " )
	String getDescription(@Param("documentId") Long documentId);

	@Transactional(readOnly = true)
	@Query("SELECT (sr.noteId IS NOT NULL) " +
			"FROM SurgicalReport sr " +
			"WHERE sr.documentId = :documentId")
	Boolean surgicalReportHasNote(@Param("documentId") Long documentId);

	@Transactional
	@Modifying
	@Query("UPDATE SurgicalReport sr SET sr.documentId = :newDocumentId " +
			"WHERE sr.documentId = :documentId")
	void updateDocumentIdByDocumentId(@Param("documentId") Long documentId, @Param("newDocumentId") Long newDocumentId);

	@Transactional
	@Modifying
	@Query("UPDATE SurgicalReport sr SET sr.startDateTime = :startDateTime " +
			"WHERE sr.documentId = :documentId")
	void updateStartDateTimeIdByDocumentId(@Param("documentId") Long documentId, @Param("startDateTime") LocalDateTime startDateTime);

	@Transactional
	@Modifying
	@Query("UPDATE SurgicalReport sr SET sr.endDateTime = :endDateTime " +
			"WHERE sr.documentId = :documentId")
	void updateEndDateTimeIdByDocumentId(@Param("documentId") Long documentId, @Param("endDateTime") LocalDateTime endDateTime);

	@Transactional(readOnly = true)
	@Query(" SELECT sr " +
			"FROM SurgicalReport sr " +
			"WHERE sr.patientId IN :patientIds")
    List<SurgicalReport> getPatientsSurgicalReportIds(@Param("patientIds") List<Integer> patientIds);

	@Transactional
	@Modifying
	@Query("UPDATE SurgicalReport sr SET sr.hasProsthesis = :hasProsthesis " +
			"WHERE sr.documentId = :documentId")
	void updateHasProsthesisByDocumentId(
			@Param("documentId") Long documentId,
			@Param("hasProsthesis") Boolean hasProsthesis
	);

	@Transactional(readOnly = true)
	@Query(value = "SELECT s.hasProsthesis " +
			"FROM SurgicalReport s " +
			"WHERE s.documentId = :documentId " )
	Boolean getHasProsthesis(@Param("documentId") Long documentId);
}
