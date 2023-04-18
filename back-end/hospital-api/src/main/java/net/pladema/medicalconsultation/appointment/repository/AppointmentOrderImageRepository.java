package net.pladema.medicalconsultation.appointment.repository;


import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentOrderImage;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentOrderImagePK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Repository
public interface AppointmentOrderImageRepository extends JpaRepository<AppointmentOrderImage, AppointmentOrderImagePK> {

    @Transactional
    @Modifying
    @Query("UPDATE AppointmentOrderImage AS aoi " +
            "SET aoi.completed = :completed " +
            "WHERE aoi.pk.appointmentId = :appointmentId")
    void updateCompleted(@Param("appointmentId") Integer appointmentId,
						 @Param("completed") Boolean completed );



	@Transactional(readOnly = true)
	@Query("SELECT aoi.imageId " +
			"FROM AppointmentOrderImage AS aoi " +
			"WHERE aoi.pk.appointmentId = :appointmentId " +
			"AND aoi.completed = TRUE")
	Optional<String>  getIdImage(@Param("appointmentId") Integer appointmentId);

}
