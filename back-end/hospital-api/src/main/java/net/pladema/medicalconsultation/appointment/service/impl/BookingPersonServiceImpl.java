package net.pladema.medicalconsultation.appointment.service.impl;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.security.UserInfo;
import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;
import net.pladema.medicalconsultation.appointment.repository.BookingPersonRepository;
import net.pladema.medicalconsultation.appointment.repository.domain.BookingPersonBo;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.appointment.repository.entity.BookingPerson;
import net.pladema.medicalconsultation.appointment.service.booking.BookingPersonService;
import net.pladema.person.repository.PersonRepository;

@Service
public class BookingPersonServiceImpl implements BookingPersonService {

    private final BookingPersonRepository bookingPersonRepository;

    private final AppointmentRepository appointmentRepository;

    private final PersonRepository personRepository;

    public BookingPersonServiceImpl(
            BookingPersonRepository bookingPersonRepository,
            AppointmentRepository appointmentRepository,
            PersonRepository personRepository
    ) {
        this.bookingPersonRepository = bookingPersonRepository;
        this.appointmentRepository = appointmentRepository;
        this.personRepository = personRepository;
    }

    @Override
    public Integer save(BookingPersonBo bookingPersonBo) {
        BookingPerson bp = mapTo(bookingPersonBo);
        bp = bookingPersonRepository.save(bp);
        return bp.getId();
    }

    @Override
    public boolean exists(String email) {
        return bookingPersonRepository.exists(email);
    }

    @Override
    public Map<Integer, BookingPersonBo> getBookingPeople(Set<Integer> appointmentIds) {
        return appointmentIds.parallelStream()
                .collect(Collectors.toMap(
                        appointmentId -> appointmentId,
                        appointmentId -> bookingPersonRepository.getBookingPerson(appointmentId).map(this::mapToBookingPersonBo)
                )).entrySet().stream().
                filter(integerOptionalEntry -> integerOptionalEntry.getValue().isPresent()).
                collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().get()
                ));
    }

    @Override
    public Optional<BookingPerson> findByEmail(String email) {
        return bookingPersonRepository.findByEmail(email);
    }

    @Override
    public void deleteByUuid(String uuid) {
        bookingPersonRepository.findByUuid(uuid).ifPresent(
                id -> appointmentRepository.updateState(
                        id,
                        AppointmentState.CANCELLED,
                        UserInfo.getCurrentAuditor()
                )
        );
    }

    @Override
    public Optional<String> getPatientName(String uuid) {
        var a = bookingPersonRepository.findPatientByUuid(uuid);
        return a.map(bookingPerson ->
                bookingPerson.getLastName() + ", " + bookingPerson.getFirstName());
    }

    @Override
    public Optional<String> getProfessionalName(Integer diaryId) {
        var a = personRepository.findProfessionalNameByDiaryId(diaryId);
        return a.map(person -> person.getFirstName() + " " + person.getLastName());
    }

    private BookingPerson mapTo(BookingPersonBo bookingPersonBo) {
        return BookingPerson.fromBookingPersonBo(bookingPersonBo);
    }

    private BookingPersonBo mapToBookingPersonBo(BookingPerson bookingPerson) {
        return BookingPersonBo.builder()
				.birthDate(bookingPerson.getBirthDate())
				.email(bookingPerson.getEmail())
				.firstName(bookingPerson.getFirstName())
				.genderId(bookingPerson.getGenderId())
				.idNumber(bookingPerson.getIdentificationNumber())
				.lastName(bookingPerson.getLastName())
				.build();

    }
}
