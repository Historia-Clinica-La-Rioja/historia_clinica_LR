package net.pladema.medicalconsultation.appointment.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.pladema.medicalconsultation.appointment.repository.entity.DetailsOrderImage;

@Repository
public interface DetailsOrderImageRepository extends JpaRepository<DetailsOrderImage, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT doi.completedOn "+
			"FROM DetailsOrderImage AS doi "+
			"WHERE doi.appointmentId = :appointmentId")
	LocalDateTime getCompletedDateByAppointmentId(@Param("appointmentId") Integer appointmentId);


}
