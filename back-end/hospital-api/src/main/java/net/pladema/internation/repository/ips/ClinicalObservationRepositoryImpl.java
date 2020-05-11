package net.pladema.internation.repository.ips;

import net.pladema.dates.configuration.DateTimeProvider;
import net.pladema.internation.repository.ips.generalstate.ClinicalObservationVo;
import net.pladema.internation.repository.masterdata.entity.DocumentStatus;
import net.pladema.internation.service.ips.domain.MapClinicalObservationVo;
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
public class ClinicalObservationRepositoryImpl implements ClinicalObservationRepository {

    private static final Logger LOG = LoggerFactory.getLogger(ClinicalObservationRepositoryImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final EntityManager entityManager;

    private final DateTimeProvider dateTimeProvider;

    public ClinicalObservationRepositoryImpl(EntityManager entityManager, DateTimeProvider dateTimeProvider){
        super();
        this.entityManager = entityManager;
        this.dateTimeProvider = dateTimeProvider;
    }

    @Transactional(readOnly = true)
    @Override
    public MapClinicalObservationVo getGeneralStateLastSevenDays(Integer internmentEpisodeId) {
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        Query query = entityManager.createNativeQuery(
                "   (SELECT  ovs.id, " +
                "            ovs.sctid_code, " +
                "            ovs.status_id, " +
                "            ovs.value, " +
                "            ovs.effective_time " +
                "    FROM document d " +
                "    JOIN document_vital_sign dvs ON (d.id = dvs.document_id) " +
                "    JOIN observation_vital_sign ovs ON (dvs.observation_vital_sign_id = ovs.id) " +
                "    WHERE d.internment_episode_id = :internmentEpisodeId " +
                "          AND d.status_id = :statusId " +
                "          AND ovs.effective_time BETWEEN :sevenDaysBefore AND :today " +
                " )UNION( " +
                "   SELECT  ovs.id, " +
                "            ovs.sctid_code, " +
                "            ovs.status_id, " +
                "            ovs.value, " +
                "            ovs.effective_time " +
                "    FROM document d " +
                "    JOIN document_lab dl ON (d.id = dl.document_id) " +
                "    JOIN observation_lab ovs ON (dl.observation_lab_id = ovs.id) " +
                "    WHERE d.internment_episode_id = :internmentEpisodeId " +
                "          AND d.status_id = :statusId " +
                "          AND ovs.effective_time BETWEEN :sevenDaysBefore AND :today " +
                ")" +
                "    ORDER BY effective_time DESC " );
        query.setParameter("internmentEpisodeId", internmentEpisodeId);
        query.setParameter("sevenDaysBefore", dateTimeProvider.nowDateTime().minusDays(7));
        query.setParameter("today", dateTimeProvider.nowDateTime());
        query.setParameter("statusId", DocumentStatus.FINAL);
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
