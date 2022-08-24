package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionVerificationStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HCHHealthConditionRepositoryImpl implements HCHHealthConditionRepository {

    private final EntityManager entityManager;

    public HCHHealthConditionRepositoryImpl(EntityManager entityManager){
        super();
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<HealthConditionVo> findGeneralState(Integer internmentEpisodeId) {

        String sqlString = "with t as (" +
                "select hc.id, snomed_id, hc.status_id, hc.main, verification_status_id, problem_id, start_date, hc.note_id, hc.updated_on, " +
                "row_number() over (partition by snomed_id, problem_id order by hc.updated_on desc) as rw " +
                "from {h-schema}document d " +
                "join {h-schema}document_health_condition dhc on d.id = dhc.document_id " +
                "join {h-schema}health_condition hc on dhc.health_condition_id = hc.id " +
                "where d.source_id = :internmentEpisodeId " +
                "and d.source_type_id = " + SourceType.HOSPITALIZATION +" "+
                "and d.status_id IN (:statusId) )" +
                "select t.id as id, s.sctid as sctid, s.pt, status_id, t.main, verification_status_id, problem_id, " +
                "start_date, n.id note_id, n.description as note " +
                "from t " +
                "left join {h-schema}note n on note_id = n.id " +
                "join {h-schema}snomed s on snomed_id = s.id " +
                "where rw = 1 and not verification_status_id = :verificationId " +
                "order by t.updated_on desc";

        List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
				.setParameter("statusId", List.of(DocumentStatus.FINAL, DocumentStatus.DRAFT))
                .setParameter("verificationId", ConditionVerificationStatus.ERROR)
                .setParameter("internmentEpisodeId", internmentEpisodeId)
                .getResultList();

        List<HealthConditionVo> result = new ArrayList<>();

        queryResult.forEach(h ->
            result.add(new HealthConditionVo((Integer)h[0],
                    new Snomed((String)h[1], (String)h[2], null, null),
                    (String)h[3],
                    (boolean)h[4],
                    (String)h[5],
                    (String)h[6],
                     h[7] != null ? ((Date)h[7]).toLocalDate() : null,
                    h[8] != null ? ((BigInteger)h[8]).longValue() : null,
                    (String)h[9]))
        );
        return result;
    }
}
