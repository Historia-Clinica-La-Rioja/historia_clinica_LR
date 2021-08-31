package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentHealthCondition;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentHealthConditionPK;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionVerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DocumentHealthConditionRepository extends JpaRepository<DocumentHealthCondition, DocumentHealthConditionPK> {

    @Transactional(readOnly = true)
    @Query("SELECT NEW ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionVo(" +
            "hc.id, s, hc.statusId, hc.main, hc.verificationStatusId, " +
            "hc.problemId, hc.startDate, " +
            "n.id as noteId, n.description as note) " +
            "FROM DocumentHealthCondition dh " +
            "JOIN HealthCondition hc ON (dh.pk.healthConditionId = hc.id) " +
            "JOIN Snomed s ON (s.id = hc.snomedId) " +
            "LEFT JOIN Note n ON (n.id = hc.noteId) " +
            "WHERE dh.pk.documentId = :documentId " +
            "AND NOT hc.verificationStatusId = ('" + ConditionVerificationStatus.ERROR + "')")
    List<HealthConditionVo> getHealthConditionFromDocument(@Param("documentId") Long documentId);

    @Transactional(readOnly = true)
    @Query("SELECT NEW ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionVo(" +
            "hc.id, s, hc.statusId, ccs.description as status, hc.main, " +
            "hc.verificationStatusId, cvs.description as verification, " +
            "hc.problemId, hc.startDate, " +
            "n.id as noteId, n.description as note) " +
            "FROM DocumentHealthCondition dh " +
            "JOIN HealthCondition hc ON (dh.pk.healthConditionId = hc.id) " +
            "JOIN Snomed s ON (s.id = hc.snomedId) " +
            "JOIN ConditionClinicalStatus ccs ON (ccs.id = hc.statusId) " +
            "JOIN ConditionVerificationStatus cvs ON (cvs.id = hc.verificationStatusId) " +
            "LEFT JOIN Note n ON (n.id = hc.noteId) " +
            "WHERE dh.pk.documentId = :documentId ")
    List<HealthConditionVo> getHealthConditionFromDocumentToReport(@Param("documentId") Long documentId);

}
