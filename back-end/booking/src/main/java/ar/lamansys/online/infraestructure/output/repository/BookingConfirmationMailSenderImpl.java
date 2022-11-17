package ar.lamansys.online.infraestructure.output.repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import ar.lamansys.online.BookingAutoConfiguration;
import ar.lamansys.online.application.booking.BookingAppointmentStorage;
import ar.lamansys.online.application.booking.BookingConfirmationMailSender;
import ar.lamansys.online.domain.booking.BookingBo;
import ar.lamansys.sgx.shared.emails.application.EmailNotificationChannel;
import ar.lamansys.sgx.shared.emails.domain.MailMessageBo;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.sgx.shared.notifications.domain.RecipientBo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class BookingConfirmationMailSenderImpl implements BookingConfirmationMailSender {
	private static final String RECOMMENDATION = "- Presentarse 15 min antes con el carnet de la Obra Social.\n";
	private static final String SUBJECT = "Confirmación de turno online";
	private static final String ROUTE = "home/cancelacion?code=";

	private final BookingAutoConfiguration bookingAutoConfiguration;

	private final BookingClinicalSpecialtyMandatoryMedicalPracticeRepository repository;

	private final EmailNotificationChannel emailSender;

	public final TemplateEngine templateEngine;

	private final BookingAppointmentStorage bookingAppointmentStorage;

	private final BookingPracticeRepository bookingPracticeRepository;

	private final BookingClinicalSpecialtyJpaRepository bookingClinicalSpecialtyJpaRepository;

	private final FeatureFlagsService featureFlagsService;

	@Override
	public void sendEmail(
		BookingBo bookingBo, 
		String uuid
	) {
		String template = "email-confirmar-reserva";
		String cancelationLink = bookingAutoConfiguration.getApiBase()
				.concat(ROUTE)
				.concat(uuid);

		var model = loadDataEmail(bookingBo, cancelationLink, uuid);
		try {
			sendTemplatedEmail(
					recipient(bookingBo),
					SUBJECT,
					template,
					model
			);
		} catch (MailException | MessagingException e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	private RecipientBo recipient(
		BookingBo bookingBo
	) {
		var bookingPerson = bookingBo.bookingPerson;
		return new RecipientBo(
				bookingPerson.getFirstName(),
				bookingPerson.getLastName(),
				bookingBo.getAppointmentDataEmail(),
				bookingBo.bookingAppointment.getPhoneNumber()
		);
	}

	protected Map<String, Object> loadDataEmail(
		BookingBo bookingBo, 
		String cancelationLink, 
		String uuid
	) {

		Map<String, Object> model = new HashMap<>();
		model.put("cancelationLink", cancelationLink);
		model.put("namePatient",getPatientName(bookingBo, uuid));
		model.put("date", getDate(bookingBo));
		model.put("nameProfessional",getProfessionalName(bookingBo));
		model.put("specialty",getSpecialty(bookingBo) + " - " + getPractice(bookingBo));
		model.put("institution",getInstitution());
		model.put("recomendation", getRecommendations(bookingBo));

		return model;
	}

	private String getSpecialty(BookingBo bookingBo) {
		if (bookingBo.getBookingAppointment().getSpecialtyId() != null) {
			var name = bookingClinicalSpecialtyJpaRepository.findById(bookingBo.getBookingAppointment().getSpecialtyId());
			if (name.isPresent()) return name.get().getDescription();
		}
		return "Especialidad";
	}

	private String getInstitution() {
		return "Sanatorio Tandil - Sarmiento 770";
	}

	private String getPractice(BookingBo bookingBo) {
		var a = bookingPracticeRepository.
				findBySnomedId(bookingBo.bookingAppointment.getSnomedId());
		if(a.isPresent())
			return a.get().getDescription();
		return "Práctica no encontrada";
	}

	private String getRecommendations(BookingBo bookingBo) {
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

	private String getPatientName(BookingBo bookingBo, String uuid) {
		var bookingPatient = bookingBo.bookingPerson;
		if(bookingPatient != null)
			return bookingPatient.getFirstName() + " " + bookingPatient.getLastName();
		return bookingAppointmentStorage.getPatientName(uuid).orElse("");
	}

	private void sendTemplatedEmail(RecipientBo recipient, String subject, String template, Map<String,Object> variables) throws MessagingException {
		try {
			Context context = new Context();
			context.setVariables(variables);
			String html = templateEngine.process(template, context);

			if (featureFlagsService.isOn(AppFeature.HABILITAR_MAIL_RESERVA_TURNO)) {
				emailSender.send(recipient, new MailMessageBo(subject, html));
			} else {
				log.info("Disabled sending email to <{}> '{}'", recipient.email, subject);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
