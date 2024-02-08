package net.pladema.medicalconsultation.appointment.repository;

import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentOrderDetailImageBO;


import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.sql.Timestamp;
import java.util.List;


@Repository
public class AppointmentDetailOrderImageRepositoryImpl implements AppointmentDetailOrderImageRepository {

	@PersistenceContext
	private final EntityManager entityManager;

	public AppointmentDetailOrderImageRepositoryImpl(EntityManager entityManager){
		this.entityManager = entityManager;
	}

	@Override
	@Transactional(readOnly = true)
	public AppointmentOrderDetailImageBO getOrderDetailImage(Integer appointmentId) {

		String sqlString = "SELECT sr.created_by, sr.id, sr.observations, sr.created_on, s.pt " +
				"FROM {h-schema}appointment_order_image aoi " +
				"LEFT JOIN {h-schema}service_request sr ON aoi.order_id = sr.id " +
				"LEFT JOIN {h-schema}diagnostic_report dr ON aoi.study_id = dr.id " +
				"LEFT JOIN {h-schema}health_condition hc ON dr.health_condition_id = hc.id " +
				"LEFT JOIN {h-schema}snomed s ON hc.snomed_id = s.id " +
				"WHERE  aoi.appointment_id = :appointmentId";

		javax.persistence.Query query = entityManager.createNativeQuery(sqlString);
		query.setParameter("appointmentId", appointmentId);

		List<Object[]> queryResult = query.getResultList();
		Object[] resultSearch = queryResult.size() == 1 ? queryResult.get(0) : null;

		AppointmentOrderDetailImageBO result = this.buildOrderDetailImageBO(resultSearch);
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public AppointmentOrderDetailImageBO getOrderTranscribedDetailImage(Integer appointmentId) {

		String sqlString = "SELECT aoi.transcribed_order_id, '', tsr.creation_date, tsr.healthcare_professional_name " +
				"FROM {h-schema}appointment_order_image aoi " +
				"JOIN {h-schema}transcribed_service_request tsr ON aoi.transcribed_order_id = tsr.id " +
				"WHERE  aoi.appointment_id = :appointmentId";

		javax.persistence.Query query = entityManager.createNativeQuery(sqlString);
		query.setParameter("appointmentId", appointmentId);

		List<Object[]> queryResult = query.getResultList();
		Object[] resultSearch = queryResult.size() == 1 ? queryResult.get(0) : null;
		AppointmentOrderDetailImageBO result = this.buildTranscribedOrderDetailImageBO(resultSearch);
		return result;
	}


	public AppointmentOrderDetailImageBO buildOrderDetailImageBO(Object[] row){

		AppointmentOrderDetailImageBO result = new AppointmentOrderDetailImageBO();

		result.setIdDoctor(row[0] == null ? null : (Integer) row[0]);
		result.setIdServiceRequest(row[1] == null ? null : (Integer) row[1]);
		result.setObservations(row[2] == null ? null : (String) row[2]);
		result.setCreationDate( row[3] == null ? null :((Timestamp) row[3]).toLocalDateTime());
		result.setHealthCondition(row[4] == null ? null : (String) row[4]);
		result.setProfessionalOrderTranscribed(null);

		return result;
	}

	public AppointmentOrderDetailImageBO buildTranscribedOrderDetailImageBO(Object[] row){

		AppointmentOrderDetailImageBO result = new AppointmentOrderDetailImageBO();
		result.setIdDoctor(null);
		result.setIdServiceRequest((Integer) row[0]);
		result.setObservations((String) row[1]);
		result.setCreationDate(((Timestamp) row[2]).toLocalDateTime());
		result.setProfessionalOrderTranscribed((String) row[3]);

		return result;
	}
}

