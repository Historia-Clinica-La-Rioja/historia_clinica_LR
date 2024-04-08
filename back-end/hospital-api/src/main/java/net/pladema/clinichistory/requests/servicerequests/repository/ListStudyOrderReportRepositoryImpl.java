package net.pladema.clinichistory.requests.servicerequests.repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.ServiceRequestCategory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ListStudyOrderReportRepositoryImpl implements ListStudyOrderReportRepository {

    private final EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> execute(Integer patientId) {
        log.debug("Input parameters -> patientId {}", patientId);

        String sqlString =
          "WITH temporal AS (SELECT " +
                  "       sr.created_by AS order_doctor_id, " +
                  "       sr.request_date, " +
                  "       st.description AS source, " +
                  "       hc.snomed_id   AS healthCondition_snomed_id, " +
                  "       sr.id          AS serviceRequestId, " +
                  "       dr.snomed_id   AS diagnosticReport_snomed_id, " +
                  "       dr.id          AS diagnosticReportId, " +
				  "       dr.status_id          , " +
                  "       row_number() OVER (PARTITION by dr.snomed_id, dr.health_condition_id ORDER BY dr.updated_on DESC) AS rw " +
                  "FROM service_request sr " +
                  "         JOIN source_type st ON sr.source_type_id = st.id " +
                  "         JOIN document d ON sr.id = d.source_id " +
                  "         JOIN document_diagnostic_report ddr ON d.id = ddr.document_id " +
                  "         JOIN diagnostic_report dr ON ddr.diagnostic_report_id = dr.id " +
                  "         JOIN health_condition hc ON dr.health_condition_id = hc.id " +
                  "WHERE dr.patient_id = :patientId " +
                  "  AND d.type_id = :documentType " +
                  "  AND d.source_type_id = :sourceType " +
                  "  AND sr.category_id = :categoryId) " +
				  "SELECT COALESCE(CASE WHEN t.status_id = '261782000' THEN true ELSE aoi.completed END, false) AS completed_by_technical, " +
                  "       t.order_doctor_id, " +
                  "       t.request_date, " +
                  "       aoi.image_id, " +
                  "       df.id          AS report_image_id, " +
                  "       s.pt           AS study, " +
                  "       s2.pt          AS problem, " +
                  "       df.file_name   AS report_image_file_name, " +
                  "       t.source, " +
                  "       t.serviceRequestId, " +
                  "       t.diagnosticReportId, " +
				  "		  COALESCE(aoi.active,false) as hasActiveAppointment "+
                  "FROM temporal t " +
                  "JOIN snomed s ON t.diagnosticReport_snomed_id = s.id " +
                  "         JOIN snomed s2 ON t.healthCondition_snomed_id = s2.id " +
                  "         LEFT JOIN appointment_order_image aoi ON t.diagnosticReportId = aoi.study_id AND aoi.active = true " +
                  "         LEFT JOIN document_file df ON aoi.document_id = df.id " +
				  "WHERE t.status_id !='89925002' " +
				  "AND rw = 1";

        Query query = entityManager.createNativeQuery(sqlString);
        query.setParameter("patientId", patientId)
                .setParameter("documentType", DocumentType.ORDER)
                .setParameter("sourceType", SourceType.ORDER)
                .setParameter("categoryId", ServiceRequestCategory.DIAGNOSTIC_IMAGING);

        return (List<Object[]>) query.getResultList();
    }
}
