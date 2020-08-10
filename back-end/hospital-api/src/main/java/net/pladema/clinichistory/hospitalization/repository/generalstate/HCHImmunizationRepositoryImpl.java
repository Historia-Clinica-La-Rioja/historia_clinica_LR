package net.pladema.clinichistory.hospitalization.repository.generalstate;

import net.pladema.clinichistory.hospitalization.repository.generalstate.domain.ImmunizationVo;
import net.pladema.clinichistory.ips.repository.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.ips.repository.masterdata.entity.InmunizationStatus;
import net.pladema.clinichistory.ips.repository.masterdata.entity.Snomed;
import net.pladema.clinichistory.outpatient.repository.domain.SourceType;
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
                "i.sctid_code, " +
                "i.status_id, " +
                "i.note_id, " +
                "i.administration_date, " +
                "i.updated_on, " +
                "row_number() over (partition by i.sctid_code, i.administration_date order by i.updated_on desc) as rw " +
                "from document d " +
                "join document_inmunization di on (d.id = di.document_id) " +
                "join inmunization i on (di.inmunization_id = i.id) " +
                "where d.source_id = :internmentEpisodeId " +
                "and d.source_type_id = " + SourceType.HOSPITALIZATION +" "+
                "and d.status_id = :documentStatusId " +
                ") " +
                "select t.id as id, s.id as sctid, s.pt, t.status_id, t.administration_date, " +
                "n.id as note_id, n.description as note " +
                "from temporal t " +
                "left join note n on t.note_id = n.id " +
                "join snomed s on t.sctid_code = s.id " +
                "where rw = 1 and not status_id = :immunizationStatusId " +
                "order by t.updated_on";

        List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
                .setParameter("internmentEpisodeId", internmentEpisodeId)
                .setParameter("documentStatusId", DocumentStatus.FINAL)
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

