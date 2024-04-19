package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.anestheticreport;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AnestheticReportRepository extends JpaRepository<AnestheticReport, Integer> {

	@Transactional(readOnly = true)
	Optional<AnestheticReport> findByDocumentId(Long documentId);

    @Transactional(readOnly = true)
    @Query("SELECT ar.id " +
            "FROM AnestheticReport ar WHERE ar.documentId = :documentId ")
    Optional<Integer> getAnestheticReportIdByDocumentId(@Param("documentId") Long documentId);
}
