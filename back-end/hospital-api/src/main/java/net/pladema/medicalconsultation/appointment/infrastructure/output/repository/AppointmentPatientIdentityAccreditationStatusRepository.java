package net.pladema.medicalconsultation.appointment.infrastructure.output.repository;

import net.pladema.medicalconsultation.appointment.infrastructure.output.repository.entity.AppointmentPatientIdentityAccreditationStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentPatientIdentityAccreditationStatusRepository extends JpaRepository<AppointmentPatientIdentityAccreditationStatus, Integer> {
}
