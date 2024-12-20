package net.pladema.clinichistory.requests.servicerequests.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.List;

@Repository
public class ListStudyWithoutOrderReportRepositoryImpl implements ListStudyWithoutOrderReportRepository {
    private static final Logger LOG = LoggerFactory.getLogger(ListStudyWithoutOrderReportRepositoryImpl.class);
    private final EntityManager entityManager;

    public ListStudyWithoutOrderReportRepositoryImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public List<Object[]> execute(Integer patientId) {
        LOG.debug("Input parameters -> patientId {}", patientId);

        String sqlString = "SELECT aoi.completed, aoi.image_id, aoi.document_id, df.file_name, d.status_id, aoi.appointment_id, aoi.report_status_id, i.name \n" +
				"FROM  {h-schema}appointment a\n" +
				"JOIN  {h-schema}appointment_order_image aoi on aoi.appointment_id = a.id\n" +
				"LEFT JOIN {h-schema}document_file df  ON df.id = aoi.document_id\n" +
				"LEFT JOIN {h-schema}document d  ON d.id = aoi.document_id\n" +
				"LEFT JOIN {h-schema}institution i  ON i.id = aoi.dest_institution_id\n" +
				"WHERE aoi.order_id is NULL\n" +
				"AND aoi.transcribed_order_id is null\n" +
				"AND a.patient_id = :patientId " +
				"AND aoi.active = true";

        Query query = entityManager.createNativeQuery(sqlString);
        query.setParameter("patientId", patientId);
        List<Object[]> result = query.getResultList();
        return result;
    }
}
