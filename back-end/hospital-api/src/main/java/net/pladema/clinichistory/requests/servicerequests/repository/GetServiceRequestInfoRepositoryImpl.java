package net.pladema.clinichistory.requests.servicerequests.repository;

import net.pladema.clinichistory.outpatient.repository.domain.SourceType;
import net.pladema.clinichistory.requests.medicationrequests.repository.GetMedicationRequestInfoRepositoryImpl;
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
                "SELECT sr.id AS sr_id, sr.doctor_id AS doctor_id, sr.request_date AS request_date, sr.medical_coverage_id AS medical_coverage_id " +
                ", dr.id AS dr_id, s.id AS m_s_id, s.sctid AS m_s_sctid, s.pt AS m_s_pt, n.description AS note, drs.id AS statusId, drs.description AS status " +
                ", h.id AS hid, h.s_id AS h_s_id, h.sctid_id AS h_sctid, h.pt AS h_pt, h.cie10_codes AS cie10_codes " +
                "FROM service_request sr " +
                "JOIN document d ON (sr.id = d.source_id AND d.source_type_id = "+ SourceType.ORDER + ") " +
                "JOIN document_diagnostic_report ddr ON (d.id = ddr.document_id) " +
                "JOIN diagnostic_report dr ON (ddr.diagnostic_report_id = dr.id)" +
                "JOIN snomed s ON (dr.snomed_id = s.id) " +
                "JOIN diagnostic_report_status drs ON (drs.id = dr.status_id) " +
                "LEFT JOIN note n ON (dr.note_id = n.id) " +
                "JOIN ( SELECT h1.id, s1.id as s_id, s1.sctid as sctid_id, s1.pt, h1.cie10_codes " +
                "            FROM health_condition h1 " +
                "            JOIN snomed s1 ON (h1.snomed_id = s1.id) " +
                "          ) AS h ON (h.id = dr.health_condition_id) " +
                "WHERE sr.id = :serviceRequestId ";
        Query query = entityManager.createNativeQuery(sqlString);

        query.setParameter("serviceRequestId", serviceRequestId);

        List<Object[]> result = query.getResultList();
        LOG.trace("execute result query -> {}", result);
        return result;
    }
}
