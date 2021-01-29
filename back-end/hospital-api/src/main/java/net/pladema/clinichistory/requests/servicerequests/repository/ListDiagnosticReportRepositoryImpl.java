package net.pladema.clinichistory.requests.servicerequests.repository;

import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DiagnosticReportStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentType;
import net.pladema.clinichistory.requests.servicerequests.repository.domain.DiagnosticReportFilterVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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
                "dr.id, dr.snomed_id, dr.status_id, dr.health_condition_id, dr.note_id, " +
                "dr.effective_time, d.source_id, d.created_by, dr.updated_on, sr.category_id AS sr_categoryId, " +
                "row_number() OVER (PARTITION by sr.id, dr.snomed_id, dr.health_condition_id ORDER BY dr.updated_on desc) AS rw " +
                "FROM document d " +
                "JOIN document_diagnostic_report ddr ON d.id = ddr.document_id " +
                "JOIN diagnostic_report dr ON ddr.diagnostic_report_id = dr.id " +
                "JOIN service_request sr ON d.source_id = sr.id " +
                "WHERE dr.patient_id = :patientId " +
                "AND d.type_id = :documentType " +
                "AND d.status_id = :documentStatusId " +
                ") " +
                "SELECT t.id AS id, s.id AS d_id, s.pt AS m_pt " +
                ", drs.id AS statusId, drs.description AS status " +
                ", h.id AS hid, h.s_id AS h_id, h.pt AS h_pt, n.description AS note " +
                ", t.source_id AS sr_id, t.created_by AS user_id, s.sctid AS d_sctid, " +
                "h.sctid AS h_sctid, t.effective_time " +
                "FROM temporal t " +
                "JOIN snomed s ON (t.snomed_id = s.id) " +
                "JOIN diagnostic_report_status drs ON (drs.id = t.status_id) " +
                "LEFT JOIN note n ON (t.note_id = n.id) " +
                "JOIN ( SELECT h1.id, s1.id as s_id, s1.pt, s1.sctid " +
                "            FROM health_condition h1 " +
                "            JOIN snomed s1 ON (h1.snomed_id = s1.id) " +
                "          ) AS h ON (h.id = t.health_condition_id) " +
                "WHERE rw = 1 " +
                "AND NOT t.status_id = :invalidStatus "+
                (filter.getStatus() != null ? "AND UPPER(t.status_id) = :statusId " : "") +
                (filter.getStudy() != null ? "AND UPPER(s.pt) LIKE :study " : "") +
                (filter.getHealthCondition() != null ? "AND UPPER(h.pt) LIKE :healthCondition " : "") +
                (filter.getCategory() != null ? "AND UPPER(t.sr_categoryId) = :category " : "") +
                "ORDER BY t.updated_on";


        Query query = entityManager.createNativeQuery(sqlString);
        query.setParameter("documentStatusId", DocumentStatus.FINAL)
                .setParameter("patientId", filter.getPatientId())
                .setParameter("documentType", DocumentType.ORDER)
                .setParameter("invalidStatus", DiagnosticReportStatus.ERROR);

        if (filter.getStatus() != null)
            query.setParameter("statusId", filter.getStatus().toUpperCase());

        if (filter.getStudy() != null)
            query.setParameter("study", "%"+filter.getStudy().toUpperCase()+"%");

        if (filter.getHealthCondition() != null)
            query.setParameter("healthCondition", "%"+filter.getHealthCondition().toUpperCase()+"%");

        if (filter.getCategory() != null)
            query.setParameter("category", filter.getCategory().toUpperCase());

        List<Object[]> result = query.getResultList();
        return result;
    }
}
