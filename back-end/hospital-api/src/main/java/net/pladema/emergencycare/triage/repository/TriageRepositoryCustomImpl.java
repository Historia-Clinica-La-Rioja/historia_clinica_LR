package net.pladema.emergencycare.triage.repository;

import net.pladema.emergencycare.triage.repository.domain.TriageVo;
import net.pladema.emergencycare.triage.repository.entity.Triage;
import net.pladema.emergencycare.triage.repository.entity.TriageDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TriageRepositoryCustomImpl implements TriageRepositoryCustom {

    private static final Logger LOG = LoggerFactory.getLogger(TriageRepositoryCustomImpl.class);

    private final EntityManager entityManager;

    public TriageRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TriageVo> getAllByEpisodeId(Integer episodeId) {
        LOG.debug("Input parameter -> episodeId {}", episodeId);

        String sqlQuery =
                "SELECT t.id, t.emergency_care_episode_id, t.notes, t.triage_category_id, " +
                        " t.doctors_office_id, t.created_on, t.created_by," +
                        " td.body_temperature_id, td.crying_excessive, td.muscle_hypertonia_id, " +
                        " td.respiratory_retraction_id,  td.stridor, td.perfusion_id, " +
                        " ece.emergency_care_type_id, " +
                        " array_agg( tvs.observation_vital_sign_id ) " +
                        "FROM {h-schema}triage AS t " +
                        "JOIN {h-schema}emergency_care_episode AS ece ON (t.emergency_care_episode_id = ece.id) " +
                        "LEFT JOIN {h-schema}triage_details AS td ON (t.id = td.triage_id) " +
                        "LEFT JOIN {h-schema}triage_vital_signs AS tvs on ( t.id = tvs.triage_id ) " +
                        "WHERE t.emergency_care_episode_id = :episodeId " +
                        "GROUP BY t.id, t.emergency_care_episode_id, t.triage_category_id, t.created_by, " +
                        " t.doctors_office_id, t.notes, t.created_on, " +
                        " td.body_temperature_id, td.crying_excessive, td.muscle_hypertonia_id, " +
                        " td.respiratory_retraction_id, td.stridor, td.perfusion_id, " +
                        " ece.emergency_care_type_id " +
                        "ORDER BY t.created_on DESC ";

        List<Object[]> queryResult = entityManager.createNativeQuery(sqlQuery)
                .setParameter("episodeId", episodeId)
                .getResultList();

        List<TriageVo> result = new ArrayList<>();
        queryResult.forEach(r ->
                result.add(new TriageVo(triageFromGetAllByEpisodeIdResult(r),
                        triageDetailsFromGetAllByEpisodeIdResult(r),
                        (Short) r[13],
                        toIntegerList((Object[]) r[14])))
        );

        LOG.debug("Output size -> {}", result.size());
        LOG.trace("Output -> {}", result);
        return result;
    }

    private Triage triageFromGetAllByEpisodeIdResult(Object[] r) {
        Triage result = new Triage((Integer) r[0], (Integer) r[1], (String) r[2], (Short) r[3], (Integer) r[4]);
        result.setCreatedOn(((java.sql.Timestamp) r[5]).toLocalDateTime());
        result.setCreatedBy((Integer) r[6]);
        return result;
    }

    private TriageDetails triageDetailsFromGetAllByEpisodeIdResult(Object[] r) {
        TriageDetails result;
        result = new TriageDetails((Integer) r[0], (Short) r[7], (Boolean) r[8], (Short) r[9], (Short) r[10], (Boolean) r[11], (Short) r[12]);
        return result;
    }

    private List<Integer> toIntegerList(Object[] array) {
        if (array == null || array[0] == null)
            return new ArrayList<>();
        List<Integer> resultList = Arrays.stream(array)
                .map(Object::toString)
                .map(Integer::valueOf)
                .collect(Collectors.toList());
        return resultList;
    }

}
