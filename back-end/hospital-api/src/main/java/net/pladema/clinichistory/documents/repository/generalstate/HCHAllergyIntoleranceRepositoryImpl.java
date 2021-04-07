package net.pladema.clinichistory.documents.repository.generalstate;

import net.pladema.clinichistory.documents.repository.generalstate.domain.AllergyConditionVo;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.AllergyIntoleranceVerificationStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.Snomed;
import net.pladema.clinichistory.outpatient.repository.domain.SourceType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HCHAllergyIntoleranceRepositoryImpl implements HCHAllergyIntoleranceRepository {

    private final EntityManager entityManager;

    public HCHAllergyIntoleranceRepositoryImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<AllergyConditionVo> findGeneralState(Integer internmentEpisodeId) {

        String sqlString = "with temporal as (" +
                "select distinct " +
                "ai.id, " +
                "ai.snomed_id, " +
                "ai.status_id, " +
                "ai.verification_status_id, " +
                "ai.category_id, " +
                "ai.start_date, " +
                "ai.updated_on, " +
                "row_number() over (partition by ai.snomed_id order by ai.updated_on desc) as rw " +
                "from document d " +
                "join document_allergy_intolerance dai on d.id = dai.document_id " +
                "join allergy_intolerance ai on dai.allergy_intolerance_id = ai.id " +
                "where d.source_id = :internmentEpisodeId " +
                "and d.source_type_id = " + SourceType.HOSPITALIZATION +" "+
                "and d.status_id = :documentStatusId " +
                ") " +
                "select t.id as id, s.sctid as sctid, s.pt, t.status_id, t.verification_status_id, t.category_id, t.start_date " +
                "from temporal t " +
                "join snomed s on t.snomed_id = s.id " +
                "where rw = 1 and not status_id = :allergyIntoleranceStatus " +
                "order by t.updated_on desc ";

        List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
                .setParameter("internmentEpisodeId", internmentEpisodeId)
                .setParameter("documentStatusId", DocumentStatus.FINAL)
                .setParameter("allergyIntoleranceStatus", AllergyIntoleranceVerificationStatus.ERROR)
                .getResultList();
        List<AllergyConditionVo> result = new ArrayList<>();
        queryResult.forEach(a -> 
            result.add(new AllergyConditionVo(
                    (Integer)a[0],
                    new Snomed((String)a[1], (String)a[2], null, null),
                    (String)a[3],
                    (String)a[4],
                    (Short)a[5],
                    a[6] != null ? ((Date)a[6]).toLocalDate() : null
            ))
        );
        return result;
    }
}
