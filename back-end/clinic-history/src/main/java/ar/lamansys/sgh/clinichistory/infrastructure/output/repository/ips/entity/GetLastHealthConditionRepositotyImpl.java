package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.GetLastHealthConditionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class GetLastHealthConditionRepositotyImpl implements GetLastHealthConditionRepository {

    private static final Logger LOG = LoggerFactory.getLogger(GetLastHealthConditionRepositotyImpl.class);
    public static final String OUTPUT = "output {}";

    private final EntityManager entityManager;

    public GetLastHealthConditionRepositotyImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Object[]> run(Integer patientId, List<Integer> hcIds) {
        LOG.debug("Input parameters -> patientId {} hcIds {} ", patientId, hcIds);

        String sqlString = "WITH temporal AS ( " +
                "SELECT hc2.id AS originalHc, hc.id AS updatedHc, hc.status_id AS statusId, " +
                "row_number() OVER (PARTITION by hc.snomed_id ORDER BY hc.updated_on desc) AS rw " +
                "FROM {h-schema}health_condition as hc " +
                "JOIN {h-schema}health_condition hc2 ON (hc.snomed_id=hc2.snomed_id)" +
                "WHERE hc.patient_id = :patientId " +
                "AND hc2.id IN :hcIds" +
                ") " +
                "SELECT t.originalHc, t.updatedHc, t.statusId " +
                "FROM temporal AS t " +
                "WHERE rw = 1 ";

        Query query = entityManager.createNativeQuery(sqlString);
        query.setParameter("patientId", patientId)
                .setParameter("hcIds", hcIds);

        List<Object[]> result = query.getResultList();

        LOG.debug(OUTPUT, result);

        return result;
    }
}
