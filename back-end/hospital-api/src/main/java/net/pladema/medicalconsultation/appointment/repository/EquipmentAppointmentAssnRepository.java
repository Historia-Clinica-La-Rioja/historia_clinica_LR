package net.pladema.medicalconsultation.appointment.repository;

import net.pladema.medicalconsultation.appointment.repository.entity.EquipmentAppointmentAssn;

import net.pladema.medicalconsultation.appointment.repository.entity.EquipmentAppointmentAssnPK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface EquipmentAppointmentAssnRepository extends JpaRepository<EquipmentAppointmentAssn, EquipmentAppointmentAssnPK> {
	
    @Transactional
    @Modifying
    @Query("UPDATE EquipmentAppointmentAssn AS aassn " +
            "SET aassn.pk.openingHoursId = :openingHoursId " +
            "WHERE aassn.pk.appointmentId = :appointmentId")
    void updateOpeningHoursId(@Param("openingHoursId") Integer openingHoursId,
                                   @Param("appointmentId") Integer appointmentId);

	@Transactional
	@Modifying
	@Query("UPDATE EquipmentAppointmentAssn AS assn " +
			"SET assn.pk.openingHoursId = :newOpeningHoursId " +
			"WHERE assn.pk.openingHoursId = :oldOpeningHoursId")
	void updateOldWithNewOpeningHoursId(@Param("oldOpeningHoursId") Integer oldOpeningHoursId, @Param("newOpeningHoursId") Integer newOpeningHoursId);

}
