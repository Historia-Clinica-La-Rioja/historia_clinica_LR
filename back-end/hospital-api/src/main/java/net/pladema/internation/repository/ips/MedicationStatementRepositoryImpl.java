package net.pladema.internation.repository.ips;

import net.pladema.internation.repository.ips.generalstate.MedicationVo;
import net.pladema.internation.repository.masterdata.entity.DocumentStatus;
import net.pladema.internation.repository.masterdata.entity.MedicationStatementStatus;
import net.pladema.internation.repository.masterdata.entity.Snomed;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MedicationStatementRepositoryImpl implements MedicationStatementRepositoryCustom {

    private final EntityManager entityManager;

    public MedicationStatementRepositoryImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<MedicationVo> findGeneralState(Integer internmentEpisodeId) {

        String sqlString = "with temporal as (" +
                "select distinct " +
                "ms.id, " +
                "ms.sctid_code, " +
                "ms.status_id, " +
                "ms.note_id, " +
                "ms.updated_on, " +
                "row_number() over (partition by ms.sctid_code order by ms.updated_on desc) as rw " +
                "from document d " +
                "join document_medicamention_statement dms on d.id = dms.document_id " +
                "join medication_statement ms on dms.medication_statement_id = ms.id " +
                "where internment_episode_id = 1 " +
                "and d.status_id = :documentStatusId " +
                ") " +
                "select t.id as id, s.id as sctid, s.fsn, status_id, n.description as note " +
                "from temporal t " +
                "left join note n on t.note_id = n.id " +
                "join snomed s on t.sctid_code = s.id " +
                "where rw = 1 and not status_id = :medicationStatusId " +
                "order by t.updated_on";

        List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
                .setParameter("documentStatusId", DocumentStatus.FINAL)
                .setParameter("medicationStatusId", MedicationStatementStatus.ERROR)
                .getResultList();
        List<MedicationVo> result = new ArrayList<>();
        queryResult.forEach(m -> {
            result.add(new MedicationVo(
                    (Integer)m[0],
                    (String)m[1],
                    new Snomed((String)m[2], (String)m[3], null, null),
                    (String)m[4]
            ));
        });
        return result;
    }
}
