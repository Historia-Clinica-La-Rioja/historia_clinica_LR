package net.pladema.medicalconsultation.appointment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.pladema.medicalconsultation.appointment.repository.entity.BookingPerson;

@Repository
public interface BookingPersonRepository extends JpaRepository<BookingPerson, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT CASE WHEN COUNT(bp.email) > 0 THEN true ELSE false END " +
            "FROM BookingPerson bp " +
            "WHERE bp.email LIKE :email")
    boolean exists(@Param("email") String email);

    @Transactional(readOnly = true)
    @Query("SELECT bp " +
            "FROM Appointment a " +
            "JOIN BookingAppointment ba on ba.pk.appointmentId = a.id " +
            "join BookingPerson bp on ba.pk.bookingPersonId = bp.id " +
            "where a.id = :appointmentId")
    Optional<BookingPerson> getBookingPerson(@Param("appointmentId") Integer appointmentId);

    @Transactional(readOnly = true)
    @Query("SELECT bp " +
            "FROM BookingPerson bp " +
            "WHERE bp.email LIKE :email")
    Optional<BookingPerson> findByEmail(@Param("email") String email);

    @Transactional(readOnly = true)
    @Query("SELECT a.id " +
            "FROM Appointment a " +
            "JOIN BookingAppointment ba ON a.id = ba.pk.appointmentId " +
            "WHERE ba.pk.uuid LIKE :uuid")
    Optional<Integer> findByUuid(@Param("uuid") String uuid);

    @Transactional(readOnly = true)
    @Query("SELECT bp " +
    "FROM BookingAppointment ba " +
    "JOIN BookingPerson bp ON ba.pk.bookingPersonId = bp.id " +
    "WHERE ba.pk.uuid LIKE :uuid")
    Optional<BookingPerson> findPatientByUuid(@Param("uuid") String uuid);
}