package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState;

import ar.lamansys.sgh.clinichistory.domain.ips.MapClinicalObservationVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ClinicalObservationVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HCHClinicalObservationRepositoryImpl implements HCHClinicalObservationRepository {

    private static final Logger LOG = LoggerFactory.getLogger(HCHClinicalObservationRepositoryImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final EntityManager entityManager;

    public HCHClinicalObservationRepositoryImpl(EntityManager entityManager){
        super();
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    @Override
    public MapClinicalObservationVo getGeneralState(Integer internmentEpisodeId) {
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        Query query = entityManager.createNativeQuery(
                "   (SELECT  ovs.id, " +
                "            s.sctid, " +
                "            ovs.status_id, " +
                "            ovs.value, " +
                "            ovs.effective_time " +
                "    FROM {h-schema}document d " +
                "    JOIN {h-schema}document_vital_sign dvs ON (d.id = dvs.document_id) " +
                "    JOIN {h-schema}observation_vital_sign ovs ON (dvs.observation_vital_sign_id = ovs.id) " +
                "    JOIN {h-schema}snomed s ON (ovs.snomed_id = s.id) " +
                "    WHERE source_id = :internmentEpisodeId " +
                "          AND source_type_id = " + SourceType.HOSPITALIZATION +" "+
                "          AND d.status_id IN (:statusId) " +
                " )UNION( " +
                "   SELECT  ovs.id, " +
                "            s.sctid, " +
                "            ovs.status_id, " +
                "            ovs.value, " +
                "            ovs.effective_time " +
                "    FROM {h-schema}document d " +
                "    JOIN {h-schema}document_lab dl ON (d.id = dl.document_id) " +
                "    JOIN {h-schema}observation_lab ovs ON (dl.observation_lab_id = ovs.id) " +
                "    JOIN {h-schema}snomed s ON (ovs.snomed_id = s.id) " +
                "    WHERE source_id = :internmentEpisodeId " +
                "          AND source_type_id = " + SourceType.HOSPITALIZATION +" "+
                "          AND d.status_id IN (:statusId) " +
                ")" +
                "    ORDER BY effective_time DESC " );
        query.setParameter("internmentEpisodeId", internmentEpisodeId);
        query.setParameter("statusId", List.of(DocumentStatus.FINAL, DocumentStatus.DRAFT));
        List<Object[]> queryResult = query.getResultList();
        List<ClinicalObservationVo> clinicalObservationVos = new ArrayList<>();
        for (Object[] o : queryResult) {
            Timestamp timeStamp = (Timestamp) o[4];
            clinicalObservationVos.add(new ClinicalObservationVo((int) o[0], (String) o[1], (String) o[2],
                    (String) o[3], timeStamp != null ? timeStamp.toLocalDateTime() : null));
        }
        MapClinicalObservationVo result = new MapClinicalObservationVo(clinicalObservationVos);
        LOG.debug(OUTPUT, result);
        return result;
    }
}
