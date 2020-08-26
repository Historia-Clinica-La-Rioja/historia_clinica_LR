package net.pladema.medicalconsultation.appointment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.pladema.medicalconsultation.appointment.repository.entity.HistoricAppointmentState;
import net.pladema.medicalconsultation.appointment.repository.entity.HistoricAppointmentStatePK;

@Repository
public interface HistoricAppointmentStateRepository extends JpaRepository<HistoricAppointmentState, HistoricAppointmentStatePK> {
	
}
