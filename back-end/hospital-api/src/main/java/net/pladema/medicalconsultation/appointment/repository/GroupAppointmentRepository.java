package net.pladema.medicalconsultation.appointment.repository;
import net.pladema.medicalconsultation.appointment.repository.entity.Appointment;

import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.appointment.service.domain.GroupAppointmentResponseBo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface GroupAppointmentRepository extends JpaRepository<Appointment, Integer> {

	@Transactional
	@Query("SELECT new net.pladema.medicalconsultation.appointment.service.domain.GroupAppointmentResponseBo(a.id, pe.identificationNumber, p.id, a.appointmentStateId, pe.id)" +
			" FROM Appointment a" +
			" JOIN AppointmentAssn assn ON a.id = assn.pk.appointmentId" +
			" LEFT JOIN Patient p ON a.patientId = p.id" +
			" LEFT JOIN Person pe ON p.personId = pe.id" +
			" LEFT JOIN BookingAppointment ba ON ba.pk.appointmentId = a.id" +
			" LEFT JOIN BookingPerson bp ON bp.id = ba.pk.bookingPersonId" +
			" WHERE assn.pk.diaryId = :diaryId" +
			" AND a.dateTypeId = :date" +
			" AND a.hour = :hour" +
			" AND a.appointmentStateId NOT IN (" + AppointmentState.CANCELLED_STR + "," + AppointmentState.BLOCKED +")")
	List<GroupAppointmentResponseBo> getApppointmentsFromDeterminatedDiaryDateTime(@Param("diaryId") Integer diaryId,
																				   @Param("date") LocalDate date,
																				   @Param("hour") LocalTime hour);
}
