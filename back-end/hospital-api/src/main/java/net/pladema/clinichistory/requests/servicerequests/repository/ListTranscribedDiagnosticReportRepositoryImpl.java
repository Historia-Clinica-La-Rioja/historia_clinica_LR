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
}
