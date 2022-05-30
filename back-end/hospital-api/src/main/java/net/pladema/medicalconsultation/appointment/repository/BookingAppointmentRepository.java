package net.pladema.medicalconsultation.appointment.repository;

import net.pladema.medicalconsultation.appointment.repository.entity.BookingAppointment;
import net.pladema.medicalconsultation.appointment.repository.entity.BookingAppointmentPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingAppointmentRepository extends JpaRepository<BookingAppointment, BookingAppointmentPK> {
}
