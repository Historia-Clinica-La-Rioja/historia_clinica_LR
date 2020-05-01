package net.pladema.internation.repository.core;

import net.pladema.internation.repository.core.entity.DocumentMedicamentionStatement;
import net.pladema.internation.repository.core.entity.DocumentMedicamentionStatementPK;
import net.pladema.internation.repository.ips.generalstate.MedicationVo;
import net.pladema.internation.repository.masterdata.entity.MedicationStatementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DocumentMedicamentionStatementRepository extends JpaRepository<DocumentMedicamentionStatement, DocumentMedicamentionStatementPK> {


    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.internation.repository.ips.generalstate.MedicationVo(" +
            "ms.id, s, ms.statusId, " +
            "n.id as noteId, n.description as note) " +
            "FROM DocumentMedicamentionStatement dm " +
            "JOIN MedicationStatement ms ON (dm.pk.medicationStatementId = ms.id) " +
            "JOIN Snomed s ON (s.id = ms.sctidCode) " +
            "LEFT JOIN Note n ON (n.id = ms.noteId) " +
            "WHERE dm.pk.documentId = :documentId " +
            "AND ms.statusId NOT IN ('"+ MedicationStatementStatus.ERROR+"')")
    List<MedicationVo> getMedicationStateFromDocument(@Param("documentId") Long documentId);
}
