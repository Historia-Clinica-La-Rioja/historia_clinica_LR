package net.pladema.internation.repository.ips;

import net.pladema.internation.repository.ips.generalstate.ClinicalObservationVo;
import net.pladema.internation.repository.masterdata.entity.DocumentStatus;
import net.pladema.internation.service.domain.ips.MapClinicalObservationVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ObservationVitalSignRepositoryImpl implements ObservationVitalSignRepositoryCustom {

    private static final Logger LOG = LoggerFactory.getLogger(ObservationVitalSignRepositoryImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final EntityManager entityManager;

    public ObservationVitalSignRepositoryImpl(EntityManager entityManager){
        super();
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    @Override
    public MapClinicalObservationVo getVitalSignsGeneralStateLastSevenDays(Integer internmentEpisodeId) {
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        Query query = entityManager.createQuery(
                "   SELECT  ovs.id, " +
                "            ovs.sctidCode, " +
                "            ovs.statusId, " +
                "            ovs.value, " +
                "            ovs.effectiveTime " +
                "    FROM Document d " +
                "    JOIN DocumentVitalSign dvs ON (d.id = dvs.pk.documentId) " +
                "    JOIN ObservationVitalSign ovs ON (dvs.pk.observationVitalSignId = ovs.id) " +
                "    WHERE d.internmentEpisodeId = :internmentEpisodeId " +
                "          AND d.statusId = :statusId " +
                "          AND ovs.effectiveTime BETWEEN :sevenDaysBefore AND :today " +
                "    ORDER BY ovs.effectiveTime DESC" );
        query.setParameter("internmentEpisodeId", internmentEpisodeId);
        query.setParameter("sevenDaysBefore", LocalDateTime.now().minusDays(7));
        query.setParameter("today", LocalDateTime.now());
        query.setParameter("statusId", DocumentStatus.FINAL);
        List<Object[]> queryResult = query.getResultList();
        List<ClinicalObservationVo> clinicalObservationVos = new ArrayList<>();
        for (Object[] o : queryResult)
            clinicalObservationVos.add(new ClinicalObservationVo((int) o[0], (String) o[1], (String) o[2], (String) o[3], (LocalDateTime) o[4]));
        MapClinicalObservationVo result = new MapClinicalObservationVo(clinicalObservationVos);
        LOG.debug(OUTPUT, result);
        return result;
    }
}
