package net.pladema.clinichistory.requests.servicerequests.repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.DiagnosticReportStatus;
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
                "row_number() OVER (PARTITION by sr.id, dr.snomed_id, dr.health_condition_id ORDER BY dr.updated_on desc) AS rw, " +
				"src.description AS category, st.description AS source " +
                "FROM {h-schema}document d " +
                "JOIN {h-schema}document_diagnostic_report ddr ON d.id = ddr.document_id " +
                "JOIN {h-schema}diagnostic_report dr ON ddr.diagnostic_report_id = dr.id " +
                "JOIN {h-schema}service_request sr ON d.source_id = sr.id " +
				"JOIN {h-schema}service_request_category src ON sr.category_id = src.id " +
				"JOIN {h-schema}source_type st ON sr.source_type_id = st.id " +
                "WHERE dr.patient_id = :patientId " +
                "AND d.type_id = :documentType " +
                "AND d.status_id = :documentStatusId " +
                ") " +
                "SELECT t.id AS id, s.id AS d_id, s.pt AS m_pt " +
                ", drs.id AS statusId, drs.description AS status " +
                ", h.id AS hid, h.s_id AS h_id, h.pt AS h_pt, n.description AS note " +
                ", t.source_id AS sr_id, t.created_by AS user_id, s.sctid AS d_sctid, " +
                "h.sctid AS h_sctid, t.effective_time, t.category, t.source " +
                "FROM temporal t " +
                "JOIN {h-schema}snomed s ON (t.snomed_id = s.id) " +
                "JOIN {h-schema}diagnostic_report_status drs ON (drs.id = t.status_id) " +
                "LEFT JOIN {h-schema}note n ON (t.note_id = n.id) " +
                "JOIN ( SELECT h1.id, s1.id as s_id, s1.pt, s1.sctid " +
                "            FROM {h-schema}health_condition h1 " +
                "            JOIN {h-schema}snomed s1 ON (h1.snomed_id = s1.id) " +
                "          ) AS h ON (h.id = t.health_condition_id) " +
                "WHERE rw = 1 " +
				"AND drs.id != :cancelled " +
                "AND NOT t.status_id = :invalidStatus "+
                (filter.getStatus() != null ? "AND UPPER(t.status_id) = :statusId " : "") +
                (filter.getStudy() != null ? "AND ( UPPER(s.pt) LIKE :study OR t.id IN (SELECT t2.id FROM temporal t1 JOIN {h-schema}snomed s1 ON (t1.snomed_id = s1.id), temporal t2 WHERE t2.source_id = t1.source_id AND UPPER(s1.pt) LIKE :study) ) " : "") +
                (filter.getHealthCondition() != null ? "AND UPPER(h.pt) LIKE :healthCondition " : "") +
                (filter.getCategory() != null ? "AND UPPER(t.sr_categoryId) = :category " : "") +
                "ORDER BY t.updated_on";

        Query query = entityManager.createNativeQuery(sqlString);
        query.setParameter("documentStatusId", DocumentStatus.FINAL)
                .setParameter("patientId", filter.getPatientId())
                .setParameter("documentType", DocumentType.ORDER)
                .setParameter("invalidStatus", DiagnosticReportStatus.ERROR)
				.setParameter("cancelled", DiagnosticReportStatus.CANCELLED);

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
