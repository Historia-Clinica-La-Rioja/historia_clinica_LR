package net.pladema.medicalconsultation.appointment.infraestructure.output.repository;

import net.pladema.medicalconsultation.appointment.infraestructure.output.repository.entity.AppointmentPatientIdentityAccreditationStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentPatientIdentityAccreditationStatusRepository extends JpaRepository<AppointmentPatientIdentityAccreditationStatus, Integer> {
}
