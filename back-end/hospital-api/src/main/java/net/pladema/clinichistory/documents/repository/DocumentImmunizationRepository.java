package net.pladema.clinichistory.documents.repository;

import net.pladema.clinichistory.hospitalization.repository.generalstate.domain.InmunizationVo;
import net.pladema.clinichistory.documents.repository.entity.DocumentInmunization;
import net.pladema.clinichistory.documents.repository.entity.DocumentInmunizationPK;
import net.pladema.clinichistory.ips.repository.masterdata.entity.InmunizationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DocumentImmunizationRepository extends JpaRepository<DocumentInmunization, DocumentInmunizationPK> {


    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.clinichistory.hospitalization.repository.generalstate.domain.InmunizationVo(" +
            "i.id, s, i.statusId, i.administrationDate, " +
            "n.id as noteId, n.description as note) " +
            "FROM DocumentInmunization di " +
            "JOIN Inmunization i ON (di.pk.inmunizationId = i.id) " +
            "JOIN Snomed s ON (s.id = i.sctidCode) " +
            "LEFT JOIN Note n ON (n.id = i.noteId) " +
            "WHERE di.pk.documentId = :documentId " +
            "AND i.statusId NOT IN ('"+ InmunizationStatus.ERROR+"')")
    List<InmunizationVo> getInmunizationStateFromDocument(@Param("documentId") Long documentId);


    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.clinichistory.hospitalization.repository.generalstate.domain.InmunizationVo(" +
            "i.id, s, i.statusId, iss.description as status, i.administrationDate, " +
            "n.id as noteId, n.description as note) " +
            "FROM DocumentInmunization di " +
            "JOIN Inmunization i ON (di.pk.inmunizationId = i.id) " +
            "JOIN Snomed s ON (s.id = i.sctidCode) " +
            "JOIN InmunizationStatus iss ON (iss.id = i.statusId) " +
            "LEFT JOIN Note n ON (n.id = i.noteId) " +
            "WHERE di.pk.documentId = :documentId ")
    List<InmunizationVo> getInmunizationStateFromDocumentToReport(@Param("documentId") Long documentId);
}
