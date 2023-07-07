package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentReportSnomedConcept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DocumentReportSnomedConceptRepository extends JpaRepository<DocumentReportSnomedConcept, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT rsc.pk.snomedId " +
			"FROM DocumentReportSnomedConcept rsc " +
			"WHERE rsc.pk.documentId = :documentId ")
	List<Integer> getSnomedConceptsByReportDocumentId(@Param("documentId") Long documentId);

	@Transactional
	@Modifying
	@Query("DELETE FROM DocumentReportSnomedConcept rsc WHERE rsc.pk.documentId = :documentId ")
	void deleteByReportDocumentId(@Param("documentId") Long documentId);
}
