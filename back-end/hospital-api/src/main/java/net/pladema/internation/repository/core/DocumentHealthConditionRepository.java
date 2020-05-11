package net.pladema.internation.repository.core;

import net.pladema.internation.repository.core.entity.DocumentHealthCondition;
import net.pladema.internation.repository.core.entity.DocumentHealthConditionPK;
import net.pladema.internation.repository.ips.generalstate.HealthConditionVo;
import net.pladema.internation.repository.masterdata.entity.ConditionVerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DocumentHealthConditionRepository extends JpaRepository<DocumentHealthCondition, DocumentHealthConditionPK> {

    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.internation.repository.ips.generalstate.HealthConditionVo(" +
            "hc.id, s, hc.statusId, hc.main, hc.verificationStatusId, " +
            "hc.problemId, hc.startDate, " +
            "n.id as noteId, n.description as note) " +
            "FROM DocumentHealthCondition dh " +
            "JOIN HealthCondition hc ON (dh.pk.healthConditionId = hc.id) " +
            "JOIN Snomed s ON (s.id = hc.sctidCode) " +
            "LEFT JOIN Note n ON (n.id = hc.noteId) " +
            "WHERE dh.pk.documentId = :documentId " +
            "AND hc.verificationStatusId NOT IN ('"+ ConditionVerificationStatus.ERROR +"','"+ConditionVerificationStatus.DISCARDED+"')")
    List<HealthConditionVo> getHealthConditionFromDocument(@Param("documentId") Long documentId);

}
