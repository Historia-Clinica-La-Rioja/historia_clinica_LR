package net.pladema.clinichistory.requests.servicerequests.repository;


import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class GetServiceRequestInfoRepositoryImpl implements GetServiceRequestInfoRepository {
    private static final Logger LOG = LoggerFactory.getLogger(GetServiceRequestInfoRepositoryImpl.class);

    private final EntityManager entityManager;

    public GetServiceRequestInfoRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> run(Integer serviceRequestId) {
        LOG.debug("Input parameters -> serviceRequestId {}", serviceRequestId);

        String sqlString = "" +
                "WITH temporal AS (" +
                    "SELECT sr.id AS sr_id, sr.doctor_id AS doctor_id, sr.request_date AS request_date, " +
                    "sr.medical_coverage_id AS medical_coverage_id, n.description AS note, " +
                    "row_number() OVER (PARTITION by dr.snomed_id, dr.health_condition_id ORDER BY dr.updated_on ASC) AS rw," +
                    "dr.snomed_id, dr.health_condition_id, d.id AS document_id " +
                    "FROM {h-schema}service_request sr " +
                    "JOIN {h-schema}document d ON (sr.id = d.source_id AND d.source_type_id = "+ SourceType.ORDER + ") " +
                    "JOIN {h-schema}document_diagnostic_report ddr ON (d.id = ddr.document_id) " +
                    "JOIN {h-schema}diagnostic_report dr ON (ddr.diagnostic_report_id = dr.id) " +
                    "LEFT JOIN {h-schema}note n ON (dr.note_id = n.id) " +
                    "WHERE sr.id = :serviceRequestId " +
                ")" +
                "SELECT t.sr_id, t.doctor_id, t.request_date, t.medical_coverage_id, t.note, " +
                "s.id AS d_r_id, s.sctid AS d_r_sctid, s.pt AS d_r_pt, " +
                "h.id AS hid, h.s_id AS h_s_id, h.sctid_id AS h_sctid, h.pt AS h_pt, h.cie10_codes AS cie10_codes, " +
				"t.document_id " +
                "FROM temporal t " +
                "JOIN {h-schema}snomed s ON (t.snomed_id = s.id) " +
                "LEFT JOIN ( SELECT h1.id, s1.id as s_id, s1.sctid as sctid_id, s1.pt, h1.cie10_codes  " +
                "            FROM {h-schema}health_condition h1 " +
                "            JOIN {h-schema}snomed s1 ON (h1.snomed_id = s1.id) " +
                "          ) AS h ON (h.id = t.health_condition_id) " +
                "WHERE rw = 1 " +
                "";
        Query query = entityManager.createNativeQuery(sqlString);

        query.setParameter("serviceRequestId", serviceRequestId);

        List<Object[]> result = query.getResultList();
        LOG.trace("execute result query -> {}", result);
        return result;
    }
}
