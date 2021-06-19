package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentInmunization;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentInmunizationPK;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ImmunizationVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.InmunizationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DocumentImmunizationRepository extends JpaRepository<DocumentInmunization, DocumentInmunizationPK> {


    @Transactional(readOnly = true)
    @Query("SELECT NEW ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ImmunizationVo(" +
            "i.id, s, i.statusId, i.administrationDate, " +
            "n.id as noteId, n.description as note) " +
            "FROM DocumentInmunization di " +
            "JOIN Inmunization i ON (di.pk.inmunizationId = i.id) " +
            "JOIN Snomed s ON (s.id = i.snomedId) " +
            "LEFT JOIN Note n ON (n.id = i.noteId) " +
            "WHERE di.pk.documentId = :documentId " +
            "AND i.statusId NOT IN ('"+ InmunizationStatus.ERROR+"')")
    List<ImmunizationVo> getImmunizationStateFromDocument(@Param("documentId") Long documentId);


    @Transactional(readOnly = true)
    @Query("SELECT NEW ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ImmunizationVo(" +
            "i.id, s, i.statusId, iss.description as status, i.administrationDate, " +
            "n.id as noteId, n.description as note) " +
            "FROM DocumentInmunization di " +
            "JOIN Inmunization i ON (di.pk.inmunizationId = i.id) " +
            "JOIN Snomed s ON (s.id = i.snomedId) " +
            "JOIN InmunizationStatus iss ON (iss.id = i.statusId) " +
            "LEFT JOIN Note n ON (n.id = i.noteId) " +
            "WHERE di.pk.documentId = :documentId ")
    List<ImmunizationVo> getImmunizationStateFromDocumentToReport(@Param("documentId") Long documentId);
}
