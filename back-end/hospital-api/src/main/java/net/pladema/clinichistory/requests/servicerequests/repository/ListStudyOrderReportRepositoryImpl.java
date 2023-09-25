package net.pladema.clinichistory.requests.servicerequests.repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
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

        String sqlString = "SELECT aoi.completed, " +
                "sr.created_by, " +
                "sr.request_date, " +
                "aoi.image_id, " +
                "d.id, " +
                "s.pt AS study, " +
                "s2.pt AS problem, " +
                "df.file_name, " +
                "d.status_id " +
                "FROM {h-schema}document d " +
                "JOIN {h-schema}document_diagnostic_report ddr ON d.id = ddr.document_id " +
                "JOIN {h-schema}diagnostic_report dr ON ddr.diagnostic_report_id = dr.id " +
                "JOIN {h-schema}service_request sr ON d.source_id = sr.id " +
                "JOIN {h-schema}health_condition hc ON dr.health_condition_id = hc.id " +
                "JOIN {h-schema}snomed s ON dr.snomed_id = s.id " +
                "JOIN {h-schema}snomed s2 ON hc.snomed_id = s2.id " +
                "LEFT JOIN {h-schema}appointment_order_image aoi ON sr.id = aoi.order_id " +
                "LEFT JOIN {h-schema}document_file df ON aoi.document_id = df.id " +
                "WHERE dr.patient_id = :patientId " +
                "AND d.type_id = :documentType " +
                "AND sr.category_id = :categoryId ";

        Query query = entityManager.createNativeQuery(sqlString);
        query.setParameter("patientId", patientId)
                .setParameter("documentType", DocumentType.ORDER)
                .setParameter("categoryId", ServiceRequestCategory.DIAGNOSTIC_IMAGING);

        return (List<Object[]>) query.getResultList();
    }
}
