package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ImmunizationVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.InmunizationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HCHImmunizationRepositoryImpl implements HCHImmunizationRepository {

    private static final Logger LOG = LoggerFactory.getLogger(HCHImmunizationRepositoryImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final EntityManager entityManager;

    public HCHImmunizationRepositoryImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<ImmunizationVo> findGeneralState(Integer internmentEpisodeId) {

        String sqlString = "with temporal as (" +
                "select distinct " +
                "i.id, " +
                "i.snomed_id, " +
                "i.status_id, " +
                "i.note_id, " +
                "i.administration_date, " +
                "i.updated_on, " +
                "row_number() over (partition by i.snomed_id, i.administration_date order by i.updated_on desc) as rw " +
                "from {h-schema}document d " +
                "join {h-schema}document_inmunization di on (d.id = di.document_id) " +
                "join {h-schema}inmunization i on (di.inmunization_id = i.id) " +
                "where d.source_id = :internmentEpisodeId " +
                "and d.source_type_id = " + SourceType.HOSPITALIZATION +" "+
                "and d.status_id IN (:documentStatusId) " +
                ") " +
                "select t.id as id, s.sctid as sctid, s.pt, t.status_id, t.administration_date, " +
                "n.id as note_id, n.description as note " +
                "from temporal t " +
                "left join {h-schema}note n on t.note_id = n.id " +
                "join {h-schema}snomed s on t.snomed_id = s.id " +
                "where rw = 1 and not status_id = :immunizationStatusId " +
                "order by t.updated_on";

        List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
                .setParameter("internmentEpisodeId", internmentEpisodeId)
                .setParameter("documentStatusId", List.of(DocumentStatus.FINAL, DocumentStatus.DRAFT))
                .setParameter("immunizationStatusId", InmunizationStatus.ERROR)
                .getResultList();
        List<ImmunizationVo> result = new ArrayList<>();
        queryResult.forEach(i -> {
            Date date = (Date) i[4];
            result.add(new ImmunizationVo(
                    (Integer)i[0],
                    new Snomed((String)i[1], (String)i[2], null, null),
                    (String)i[3],
                    date != null ? date.toLocalDate() : null,
                    i[5] != null ? ((BigInteger)i[5]).longValue() : null,
                    (String)i[6]
            ));
        });
        LOG.debug(OUTPUT, result);
        return result;
    }
}

