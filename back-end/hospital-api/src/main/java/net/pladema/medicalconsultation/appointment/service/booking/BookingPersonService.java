package net.pladema.medicalconsultation.appointment.service.booking;

import net.pladema.medicalconsultation.appointment.repository.domain.BookingPersonBo;
import net.pladema.medicalconsultation.appointment.repository.entity.BookingPerson;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface BookingPersonService {
    BookingPerson save(BookingPersonBo bookingPersonBo);

    boolean exists(String email);

    Map<Integer, BookingPersonBo> getBookingPeople(Set<Integer> appointmentId);

    Optional<BookingPerson> findByEmail(String email);

    void deleteByUuid(String uuid);

    Optional<String> getPatientName(String uuid);

    Optional<String> getProfessionalName(Integer diaryId);

	List<BookingPerson> findByEmailAndIdentificationNumber(String email, String identificationNumber);
}
