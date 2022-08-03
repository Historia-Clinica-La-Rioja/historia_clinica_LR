package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.AllergyConditionVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.AllergyIntoleranceVerificationStatus;
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
                "ai.criticality, " +
                "ai.start_date, " +
                "ai.updated_on, " +
                "row_number() over (partition by ai.snomed_id order by ai.updated_on desc) as rw " +
                "from {h-schema}document d " +
                "join {h-schema}document_allergy_intolerance dai on d.id = dai.document_id " +
                "join {h-schema}allergy_intolerance ai on dai.allergy_intolerance_id = ai.id " +
                "where d.source_id IN (:internmentEpisodeId) " +
                "and d.source_type_id = " + SourceType.HOSPITALIZATION +" "+
                "and d.status_id IN (:documentStatusId) " +
                ") " +
                "select t.id as id, s.sctid as sctid, s.pt, t.status_id, t.verification_status_id, t.category_id, t.criticality, t.start_date " +
                "from temporal t " +
                "join {h-schema}snomed s on t.snomed_id = s.id " +
                "where rw = 1 and not verification_status_id = :allergyIntoleranceStatus " +
                "order by t.updated_on desc ";

        List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
                .setParameter("internmentEpisodeId", internmentEpisodeId)
                .setParameter("documentStatusId", List.of(DocumentStatus.FINAL, DocumentStatus.DRAFT))
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
                    (Short)a[6],
                    a[7] != null ? ((Date)a[7]).toLocalDate() : null
            ))
        );
        return result;
    }
}
