package net.pladema.medicalconsultation.appointment.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class AppointmentUpdateRepositoryImpl implements AppointmentUpdateRepository {

	@PersistenceContext
	private final EntityManager entityManager;

	public AppointmentUpdateRepositoryImpl(EntityManager entityManager){
		this.entityManager = entityManager;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Integer> getAppointmentsBeforeDateByStates(List<Short> stateIds, LocalDateTime maxAppointmentDate, Short limit) {

		String sqlQuery =
				"SELECT a.id FROM Appointment a " +
				"WHERE a.appointmentStateId IN (:stateIds) " +
				"AND (a.dateTypeId < :maxAppointmentDate OR (a.dateTypeId = :maxAppointmentDate AND a.hour < :maxAppointmentTime)) " +
				"AND NOT EXISTS (SELECT 1 FROM HistoricAppointmentState has WHERE has.pk.appointmentId = a.id AND has.creationable.createdOn >= CURRENT_DATE - 1) ";

		List<Integer> result = entityManager.createQuery(sqlQuery)
				.setParameter("stateIds", stateIds)
				.setParameter("maxAppointmentDate", maxAppointmentDate.toLocalDate())
				.setParameter("maxAppointmentTime", maxAppointmentDate.toLocalTime())
				.setMaxResults(limit)
				.getResultList();

		return result;
	}

}