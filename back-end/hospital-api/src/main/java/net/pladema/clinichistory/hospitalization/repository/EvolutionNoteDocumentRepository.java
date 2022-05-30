package net.pladema.clinichistory.hospitalization.repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import net.pladema.clinichistory.hospitalization.repository.domain.EvolutionNoteDocument;
import net.pladema.clinichistory.hospitalization.repository.domain.EvolutionNoteDocumentPK;
import net.pladema.clinichistory.hospitalization.repository.domain.summary.EvaluationNoteSummaryVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface EvolutionNoteDocumentRepository extends JpaRepository<EvolutionNoteDocument, EvolutionNoteDocumentPK> {

    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.clinichistory.hospitalization.repository.domain.summary.EvaluationNoteSummaryVo(d.id, d.statusId) " +
            "FROM EvolutionNoteDocument evnd " +
            "JOIN Document d ON (d.id = evnd.pk.documentId) " +
            "WHERE evnd.pk.internmentEpisodeId = :internmentEpisodeId " +
			"AND d.statusId = '" + DocumentStatus.FINAL + "' " +
            "ORDER BY d.updateable.updatedOn DESC")
    Page<EvaluationNoteSummaryVo> getLastEvaluationNoteSummary(@Param("internmentEpisodeId") Integer internmentEpisodeId, Pageable pageable);
}
