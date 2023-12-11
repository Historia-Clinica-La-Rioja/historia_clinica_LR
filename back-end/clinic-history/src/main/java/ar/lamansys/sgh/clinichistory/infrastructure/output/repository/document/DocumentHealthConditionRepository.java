package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentHealthCondition;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentHealthConditionPK;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentHealthConditionRepository extends JpaRepository<DocumentHealthCondition, DocumentHealthConditionPK> {

    @Transactional(readOnly = true)
    @Query("SELECT NEW ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionVo(" +
            "hc.id, s, hc.statusId, ccs.description, hc.main, " +
            "hc.verificationStatusId, cvs.description, " +
            "hc.problemId, hc.startDate, hc.inactivationDate, " +
            "n.id as noteId, n.description as note, per.reasonId as errorReasonId, " +
            "ph.typeId as specificTypeIfPersonalHistory) " +
            "FROM DocumentHealthCondition dh " +
            "JOIN HealthCondition hc ON (dh.pk.healthConditionId = hc.id) " +
            "JOIN Snomed s ON (s.id = hc.snomedId) " +
            "LEFT JOIN Note n ON (n.id = hc.noteId) " +
            "JOIN ConditionClinicalStatus ccs ON (hc.statusId = ccs.id) " +
            "JOIN ConditionVerificationStatus cvs ON (hc.verificationStatusId = cvs.id) " +
            "LEFT JOIN ProblemErrorReason per ON (hc.id = per.healthConditionId) " +
            "LEFT JOIN PersonalHistory ph ON (hc.id = ph.healthConditionId) " +
            "WHERE dh.pk.documentId = :documentId")
    List<HealthConditionVo> getHealthConditionFromDocument(@Param("documentId") Long documentId);

    @Transactional(readOnly = true)
    @Query("SELECT d.creationable.createdBy " +
            "FROM DocumentHealthCondition dhc " +
            "JOIN Document d ON dhc.pk.documentId = d.id " +
            "WHERE dhc.pk.healthConditionId = :healthConditionId")
    Optional<Integer> getUserId(@Param("healthConditionId") Integer healthConditionId);

    @Transactional(readOnly = true)
    @Query("SELECT d.creationable.createdOn " +
            "FROM DocumentHealthCondition dhc " +
            "JOIN Document d ON dhc.pk.documentId = d.id " +
            "WHERE dhc.pk.healthConditionId = :healthConditionId")
    Optional<LocalDateTime> getCreatedOn(@Param("healthConditionId") Integer healthConditionId);

}
