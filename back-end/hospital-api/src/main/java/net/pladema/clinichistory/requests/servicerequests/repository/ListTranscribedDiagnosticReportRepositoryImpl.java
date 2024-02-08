package net.pladema.clinichistory.requests.servicerequests.repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ListTranscribedDiagnosticReportRepositoryImpl implements ListTranscribedDiagnosticReportRepository {

    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<Integer> execute(Integer patientId) {
        log.debug("Input parameters -> patientId {}", patientId);

        String sqlString = "SELECT DISTINCT tsr.id " +
                "FROM {h-schema}transcribed_service_request tsr " +
                "LEFT JOIN {h-schema}appointment_order_image aoi ON (aoi.transcribed_order_id = tsr.id) " +
                "WHERE tsr.patient_id = :patientId " +
                "AND tsr.creation_date >= CURRENT_DATE - INTERVAL '1 month' " +
                "AND aoi.active IS FALSE " +
                "AND tsr.id NOT IN (SELECT transcribed_order_id " +
					"FROM appointment_order_image aoii " +
					"JOIN appointment app ON (aoii.appointment_id = app.id) " +
					"WHERE app.patient_id = :patientId AND aoii.active IS TRUE AND aoii.transcribed_order_id IS NOT NULL)";
        Query query = entityManager.createNativeQuery(sqlString);
        query.setParameter("patientId", patientId);
        List<Integer> result = query.getResultList();
        return result;
    }

    @Transactional(readOnly = true)
    public List<Integer> getByAppointmentId(Integer appointmentId) {
        log.debug("Input parameters -> appointmentId {}", appointmentId);

        String sqlString = "SELECT tsr.id " +
                "FROM {h-schema}transcribed_service_request tsr " +
                "JOIN {h-schema}appointment_order_image aoi ON (aoi.transcribed_order_id = tsr.id) " +
                "WHERE aoi.appointment_id = :appointmentId " +
                "AND creation_date >= CURRENT_DATE - INTERVAL '1 month' ";

        Query query = entityManager.createNativeQuery(sqlString);
        query.setParameter("appointmentId", appointmentId);
        List<Integer> result = query.getResultList();
        return result;
    }

    @Transactional(readOnly = true)
    public List<Object[]> getListTranscribedOrder(Integer patientId) {
        log.debug("Input parameters -> patientId {}", patientId);

        String sqlString = "SELECT DISTINCT aoi.completed, tsr.healthcare_professional_name, tsr.creation_date, " +
                "aoi.image_id, aoi.document_id, df.file_name, d.status_id, tsr.id " +
                "FROM {h-schema}appointment_order_image aoi " +
                "JOIN {h-schema}transcribed_service_request tsr on tsr.id = aoi.transcribed_order_id " +
                "LEFT JOIN {h-schema}document_file df ON df.id = aoi.document_id " +
                "LEFT JOIN {h-schema}document d ON d.id = aoi.document_id " +
                "WHERE aoi.order_id IS NULL " +
                "AND tsr.patient_id = :patientId " +
                "AND (aoi.transcribed_order_id NOT IN (SELECT aoii.transcribed_order_id " +
					"FROM appointment_order_image aoii " +
					"JOIN appointment app ON (aoii.appointment_id = app.id) " +
					"WHERE app.patient_id <> :patientId AND aoii.active IS TRUE AND aoii.transcribed_order_id IS NOT NULL) " +
                "OR aoi.transcribed_order_id IS NULL)";
        Query query = entityManager.createNativeQuery(sqlString);
        query.setParameter("patientId", patientId);
        List<Object[]> result = query.getResultList();
        return result;
    }
}
