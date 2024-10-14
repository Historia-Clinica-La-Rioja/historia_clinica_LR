package ar.lamansys.online.infraestructure.output.repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedPort;

import org.springframework.stereotype.Service;

import ar.lamansys.online.BookingAutoConfiguration;
import ar.lamansys.online.application.booking.BookingAppointmentStorage;
import ar.lamansys.online.application.booking.BookingConfirmationMailSender;
import ar.lamansys.online.domain.booking.BookingBo;
import ar.lamansys.online.infraestructure.notification.message.ConfirmarReservaNotificationArgs;
import ar.lamansys.online.infraestructure.notification.message.ConfirmarReservaTemplateInput;
import ar.lamansys.online.infraestructure.output.BookingNotificationSender;
import ar.lamansys.sgx.shared.notifications.domain.RecipientBo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class BookingConfirmationMailSenderImpl implements BookingConfirmationMailSender {
	private static final String RECOMMENDATION = "- Presentarse 15 min antes con el carnet de la Obra Social.\n";
	private static final String ROUTE = "home/cancelacion?code=";

	private final BookingAutoConfiguration bookingAutoConfiguration;

	private final BookingClinicalSpecialtyMandatoryMedicalPracticeRepository repository;

	private final BookingAppointmentStorage bookingAppointmentStorage;

	private final BookingPracticeRepository bookingPracticeRepository;

	private final BookingClinicalSpecialtyJpaRepository bookingClinicalSpecialtyJpaRepository;

	private final BookingNotificationSender bookingNotificationSender;

	private final SharedSnomedPort sharedSnomedPort;

    @Override
    public void sendEmail(BookingBo bookingBo, String uuid) {
		String cancelationLink = bookingAutoConfiguration.getApiBase()
				.concat(ROUTE)
				.concat(uuid);

		ConfirmarReservaTemplateInput message = loadDataEmail(bookingBo, cancelationLink, uuid);

		this.bookingNotificationSender.send(
				new RecipientBo(
						bookingBo.bookingPerson.getFirstName(),
						bookingBo.bookingPerson.getLastName(),
						bookingBo.appointmentDataEmail,
						bookingBo.bookingAppointment.getPhoneNumber()
				),
				message
		);

    }

    protected ConfirmarReservaTemplateInput loadDataEmail(BookingBo bookingBo, String cancelationLink, String uuid) {
		var args = ConfirmarReservaNotificationArgs.builder()
				.cancelationLink(cancelationLink)
				.namePatient(getPatienName(bookingBo, uuid))
				.date(getDate(bookingBo))
				.nameProfessional(getProfessionalName(bookingBo))
				.specialty(getSpecialty(bookingBo) + " - " + getPractice(bookingBo))
				.institution(getInstitution(bookingBo))
				.recomendation(getRecommendations(bookingBo));
        return new ConfirmarReservaTemplateInput(args.build());
    }

	private String getSpecialty(BookingBo bookingBo) {
		if (bookingBo.getBookingAppointment().getSpecialtyId() != null) {
			var name = bookingClinicalSpecialtyJpaRepository.findById(bookingBo.getBookingAppointment().getSpecialtyId());
			if (name.isPresent()) return name.get().getDescription();
		}
		return "Especialidad";
	}

	private String getInstitution(BookingBo bookingBo) {
		return bookingAppointmentStorage.getInstitutionAddress(bookingBo.bookingAppointment.getDiaryId());
	}

	private String getPractice(BookingBo bookingBo) {
		if (!bookingBo.isOnlineBooking() && bookingBo.getBookingAppointment().getSnomedId() != null)
			return sharedSnomedPort.getSnomed(bookingBo.bookingAppointment.getSnomedId()).getPt();
		var a = bookingPracticeRepository.
				findBySnomedId(bookingBo.bookingAppointment.getSnomedId());
		if(a.isPresent())
			return a.get().getDescription();
		return "Práctica no encontrada";
	}

	private String getRecommendations(BookingBo bookingBo) {
		if (!bookingBo.isOnlineBooking())
			return RECOMMENDATION;

		var a =
				repository.findBySnomedIdAndSpecialtyId(
					bookingBo.bookingAppointment.getSnomedId(),
					bookingBo.bookingAppointment.getSpecialtyId()
				).stream().findFirst();

		return a.map(clinicalSpecialtyMandatoryMedicalPractice ->
						clinicalSpecialtyMandatoryMedicalPractice.getPracticeRecommendations() != null ?
				RECOMMENDATION + clinicalSpecialtyMandatoryMedicalPractice.getPracticeRecommendations() :
				RECOMMENDATION)
				.orElse(RECOMMENDATION);
	}

	private String getProfessionalName(BookingBo bookingBo) {
		return bookingAppointmentStorage
				.getProfessionalName(bookingBo.bookingAppointment.getDiaryId())
				.orElse("Profesional no encontrado");
	}


	private String getDate(BookingBo bookingBo) {
		return LocalDate.parse(bookingBo.bookingAppointment.getDay())
				.format(DateTimeFormatter.ofPattern("d/MM/yyyy"))
				+ " a las " + bookingBo.bookingAppointment.getHour() + " h";
	}

    private String getPatienName(BookingBo bookingBo, String uuid) {
        var bookingPatient = bookingBo.bookingPerson;
        if(bookingPatient != null)
            return bookingPatient.getFirstName() + " " + bookingPatient.getLastName();
        return bookingAppointmentStorage.getPatientName(uuid).orElse("");
    }
}
