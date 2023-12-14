package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.surgicalreport.SurgicalReport;

import java.time.LocalDateTime;

@Repository
public interface SurgicalReportRepository extends JpaRepository<SurgicalReport, Integer> {

	@Query(value = "SELECT s.startDateTime " +
			"FROM SurgicalReport s " +
			"WHERE s.documentId = :documentId " )
	LocalDateTime getStartDateTime(@Param("documentId") Long documentId);

	@Query(value = "SELECT s.endDateTime " +
			"FROM SurgicalReport s " +
			"WHERE s.documentId = :documentId " )
	LocalDateTime getEndDateTime(@Param("documentId") Long documentId);
}
