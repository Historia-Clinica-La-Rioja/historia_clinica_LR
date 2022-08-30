package net.pladema.medicalconsultation.appointment.repository.storage.appointmentsummary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VAppointmentSummaryRepository extends JpaRepository<VAppointmentSummary, Integer> {

}
