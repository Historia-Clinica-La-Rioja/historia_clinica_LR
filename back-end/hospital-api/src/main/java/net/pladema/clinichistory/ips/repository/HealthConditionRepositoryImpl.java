package net.pladema.clinichistory.ips.repository;

import net.pladema.clinichistory.ips.repository.generalstate.HealthConditionVo;
import net.pladema.clinichistory.ips.repository.masterdata.entity.ConditionVerificationStatus;
import net.pladema.clinichistory.ips.repository.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.ips.repository.masterdata.entity.Snomed;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HealthConditionRepositoryImpl implements HealthConditionRepositoryCustom {

    private final EntityManager entityManager;

    public HealthConditionRepositoryImpl(EntityManager entityManager){
        super();
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<HealthConditionVo> findGeneralState(Integer internmentEpisodeId) {

        String sqlString = "with t as (" +
                "select hc.id, sctid_code, hc.status_id, hc.main, verification_status_id, problem_id, start_date, hc.note_id, hc.updated_on, " +
                "row_number() over (partition by sctid_code, problem_id order by hc.updated_on desc) as rw " +
                "from document d " +
                "join document_health_condition dhc on d.id = dhc.document_id " +
                "join health_condition hc on dhc.health_condition_id = hc.id " +
                "where internment_episode_id = :internmentEpisodeId " +
                "and d.status_id = :statusId )" +
                "select t.id as id, s.id as sctid, s.pt, status_id, t.main, verification_status_id, problem_id, " +
                "start_date, n.id note_id, n.description as note " +
                "from t " +
                "left join note n on note_id = n.id " +
                "join snomed s on sctid_code = s.id " +
                "where rw = 1 and not verification_status_id = :verificationId " +
                "order by t.updated_on desc";

        List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
                .setParameter("statusId", DocumentStatus.FINAL)
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
