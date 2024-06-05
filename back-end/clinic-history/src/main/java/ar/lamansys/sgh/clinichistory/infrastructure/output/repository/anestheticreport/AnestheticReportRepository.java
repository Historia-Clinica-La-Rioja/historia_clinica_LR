package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.anestheticreport;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
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
    @Query("SELECT d.id " +
            "FROM Document d " +
            "WHERE d.sourceId = :internmentEpisodeId " +
            "AND d.sourceTypeId = '" + SourceType.HOSPITALIZATION + "' " +
            "AND d.typeId = '" + DocumentType.ANESTHETIC_REPORT + "' " +
            "AND d.statusId = '" + DocumentStatus.DRAFT + "' ")
    Optional<Long> getDocumentIdFromLastAnestheticReportDraft(@Param("internmentEpisodeId") Integer internmentEpisodeId);
}
