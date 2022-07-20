package net.pladema.medicalconsultation.appointment.repository;

import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentObservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentObservationRepository extends JpaRepository<AppointmentObservation, Integer> {

}
