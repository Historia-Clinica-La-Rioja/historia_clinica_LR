package net.pladema.medicalconsultation.appointment.repository;

import net.pladema.medicalconsultation.appointment.repository.entity.Appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AppointmentLabelRepository extends JpaRepository<Appointment, Short> {

	@Transactional
	@Modifying
	@Query( "UPDATE Appointment AS a " +
			"SET a.diaryLabelId = :diaryLabelId, " +
			"a.updateable.updatedOn = CURRENT_TIMESTAMP, " +
			"a.updateable.updatedBy = :userId " +
			"WHERE a.id = :appointmentId ")
	void updateLabel(@Param("diaryLabelId") Integer diaryLabelId,
					 @Param("userId") Integer userId,
					 @Param("appointmentId") Integer appointmentId);
}
