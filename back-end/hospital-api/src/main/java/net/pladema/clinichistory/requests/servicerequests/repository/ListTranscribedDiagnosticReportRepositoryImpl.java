package net.pladema.clinichistory.requests.servicerequests.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ListTranscribedDiagnosticReportRepositoryImpl implements ListTranscribedDiagnosticReportRepository {
    private static final Logger LOG = LoggerFactory.getLogger(ListTranscribedDiagnosticReportRepositoryImpl.class);
    private final EntityManager entityManager;

    public ListTranscribedDiagnosticReportRepositoryImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public List<Object[]> execute(Integer patientId) {
        LOG.debug("Input parameters -> patientId {}", patientId);

        String sqlString = "SELECT tsr.id, dr.id as study_id, s.pt as study_name " +
				"FROM {h-schema}transcribed_service_request tsr " +
				"JOIN {h-schema}diagnostic_report dr ON (tsr.study_id = dr.id) " +
				"JOIN {h-schema}snomed s ON (dr.snomed_id = s.id) " +
				"WHERE tsr.patient_id = :patientId " +
				"AND creation_date >= CURRENT_DATE - INTERVAL '1 month' " +
				"AND tsr.id NOT IN ( " +
					"SELECT transcribed_order_id " +
					"FROM appointment_order_image) ";

        Query query = entityManager.createNativeQuery(sqlString);
        query.setParameter("patientId", patientId);
        List<Object[]> result = query.getResultList();
        return result;
    }
	@Transactional(readOnly = true)
	public List<Object[]> getListTranscribedOrder(Integer patientId) {
		LOG.debug("Input parameters -> patientId {}", patientId);

		String sqlString = "SELECT aoi.completed, tsr.healthcare_professional_name, tsr.creation_date, aoi.image_id, aoi.document_id, s.pt AS Spt, s2.pt, df.file_name, d.status_id  \n" +
				"FROM {h-schema}appointment_order_image aoi\n" +
				"JOIN  {h-schema}transcribed_service_request tsr on tsr .id = aoi.transcribed_order_id\n" +
				"JOIN {h-schema}diagnostic_report dr ON dr.id = tsr.study_id\n" +
				"JOIN {h-schema}health_condition hc on hc.id =dr.health_condition_id \n" +
				"JOIN {h-schema}snomed s2 on s2.id =hc.snomed_id \n" +
				"JOIN {h-schema}snomed s ON s.id = dr.snomed_id\n" +
				"LEFT JOIN {h-schema}document_file df  ON df.id = aoi.document_id\n" +
				"LEFT JOIN {h-schema}document d  ON d.id = aoi.document_id\n" +
				"WHERE aoi.order_id is null\n" +
				"AND tsr.patient_id = :patientId " +
				"AND aoi.active = true";
		Query query = entityManager.createNativeQuery(sqlString);
		query.setParameter("patientId", patientId);
		List<Object[]> result = query.getResultList();
		return result;
	}
}
