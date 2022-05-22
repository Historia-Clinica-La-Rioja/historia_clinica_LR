package net.pladema.clinichistory.requests.servicerequests.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class GetDiagnosticReportInfoRepositoryImpl implements GetDiagnosticReportInfoRepository{

    private static final Logger LOG = LoggerFactory.getLogger(GetDiagnosticReportInfoRepositoryImpl.class);
    private final EntityManager entityManager;

    public GetDiagnosticReportInfoRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public Object[] execute(Integer drId) {
        LOG.debug("Input parameters -> drId {}", drId);

        String sqlString = "SELECT dr.id AS dr_id, s.id, s.sctid, s.pt, " +
                "h.id AS hid, h.s_id AS h_id, h.pt AS h_pt, h.sctid AS h_sctid, " +
                "n.description, dr.status_id, d.source_id, dr.effective_time," +
                "d.created_by " +
                "FROM {h-schema}diagnostic_report dr " +
                "JOIN {h-schema}snomed s ON (dr.snomed_id = s.id) " +
                "LEFT JOIN {h-schema}note n ON (dr.note_id = n.id) " +
                "JOIN ( SELECT h1.id, s1.id as s_id, s1.pt, s1.sctid " +
                "            FROM {h-schema}health_condition h1 " +
                "            JOIN {h-schema}snomed s1 ON (h1.snomed_id = s1.id) " +
                "          ) AS h ON (h.id = dr.health_condition_id) " +
                "JOIN {h-schema}document_diagnostic_report ddr ON (dr.id = ddr.diagnostic_report_id) " +
                "JOIN {h-schema}document d ON (d.id = ddr.document_id) " +
                "WHERE dr.id = :drId ";


        Query query = entityManager.createNativeQuery(sqlString);
        query.setParameter("drId", drId);
        List<Object[]> result = query.getResultList();
        return result.get(0);
    }
}
