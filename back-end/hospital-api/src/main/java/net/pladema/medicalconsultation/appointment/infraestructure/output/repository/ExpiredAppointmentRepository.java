package net.pladema.medicalconsultation.appointment.infraestructure.output.repository;


import net.pladema.medicalconsultation.appointment.infraestructure.output.repository.entity.ExpiredAppointmentReason;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpiredAppointmentRepository extends JpaRepository<ExpiredAppointmentReason, Integer> {

}
