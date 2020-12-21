package net.pladema.clinichistory.requests.servicerequests.repository;

import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentType;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.DiagnosticReportFilterVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class ListDiagnosticReportRepositoryImpl implements ListDiagnosticReportRepository {
    private static final Logger LOG = LoggerFactory.getLogger(ListDiagnosticReportRepositoryImpl.class);
    private final EntityManager entityManager;

    public ListDiagnosticReportRepositoryImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public List<Object[]> execute(DiagnosticReportFilterVo filter) {
        LOG.debug("Input parameters -> filter {}", filter);

        String sqlString = "with temporal as (" +
                "SELECT DISTINCT " +
                "dr.id, dr.snomed_id, dr.status_id, dr.health_condition_id, dr.note_id, d.source_id, d.created_by, dr.updated_on, " +
                "row_number() OVER (PARTITION by dr.snomed_id, dr.health_condition_id ORDER BY dr.updated_on desc) AS rw " +
                "FROM document d " +
                "JOIN document_diagnostic_report ddr ON d.id = ddr.document_id " +
                "JOIN diagnostic_report dr ON ddr.diagnostic_report_id = dr.id " +
                "WHERE dr.patient_id = :patientId  " +
                "AND d.type_id = :documentType " +
                "AND d.status_id = :documentStatusId " +
                ") " +
                "SELECT t.id AS id, s.id AS d_id, s.pt AS m_pt " +
                ", drs.id AS statusId, drs.description AS status " +
                ", h.id AS hid, h.s_id AS h_id, h.pt AS h_pt, n.description AS note " +
                ", t.source_id AS sr_id, t.created_by AS user_id, s.sctid AS d_sctid, h.sctid AS h_sctid " +
                "FROM temporal t " +
                "JOIN snomed s ON (t.snomed_id = s.id) " +
                "JOIN diagnostic_report_status drs ON (drs.id = t.status_id) " +
                "LEFT JOIN note n ON (t.note_id = n.id) " +
                "JOIN ( SELECT h1.id, s1.id as s_id, s1.pt, s1.sctid " +
                "            FROM health_condition h1 " +
                "            JOIN snomed s1 ON (h1.snomed_id = s1.id) " +
                "          ) AS h ON (h.id = t.health_condition_id) " +
                "WHERE rw = 1 " +
                "ORDER BY t.updated_on";

        List<Object[]> result = entityManager.createNativeQuery(sqlString)
                .setParameter("documentStatusId", DocumentStatus.FINAL)
                .setParameter("patientId", filter.getPatientId())
                .setParameter("documentType", DocumentType.ORDER)
                .getResultList();
        return result;
    }
}
