package net.pladema.clinichistory.documents.repository.ips;

import net.pladema.clinichistory.documents.repository.ips.entity.HealthCondition;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DiagnosticReportStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentType;
import net.pladema.clinichistory.requests.servicerequests.repository.ListDiagnosticReportRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class GetLastHealthConditionRepositotyImpl implements GetLastHealthConditionRepository {

    private static final Logger LOG = LoggerFactory.getLogger(ListDiagnosticReportRepositoryImpl.class);
    private final EntityManager entityManager;

    public GetLastHealthConditionRepositotyImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Object[]> run(Integer patientId, List<Integer> hcIds) {
        LOG.debug("Input parameters -> patientId {} hcIds {} ", patientId, hcIds);

        String sqlString = "WITH temporal AS ( " +
                "SELECT hc.id, hc.status_id, " +
                "row_number() OVER (PARTITION by hc.snomed_id ORDER BY hc.updated_on desc) AS rw " +
                "FROM health_condition as hc " +
                "WHERE hc.patient_id = :patientId " +
                "AND hc.snomed_id IN ( " +
                "SELECT hcInner.snomed_id " +
                "FROM health_condition hcInner " +
                "WHERE hcInner.id IN :hcIds ) " +
                ") " +
                "SELECT t.* " +
                "FROM temporal AS t " +
                "WHERE rw = 1 ";

        Query query = entityManager.createNativeQuery(sqlString);
        query.setParameter("patientId", patientId)
                .setParameter("hcIds", hcIds);

        List<Object[]> result = query.getResultList();

        return result;
    }
}
