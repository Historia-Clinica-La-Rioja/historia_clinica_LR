package ar.lamansys.online.infraestructure.output.repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import ar.lamansys.online.BookingAutoConfiguration;
import ar.lamansys.online.application.booking.BookingAppointmentStorage;
import ar.lamansys.online.application.booking.BookingConfirmationMailSender;
import ar.lamansys.online.domain.booking.BookingBo;
import ar.lamansys.sgx.shared.emails.domain.Mail;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookingConfirmationMailSenderImpl implements BookingConfirmationMailSender {

    private final BookingAutoConfiguration bookingAutoConfiguration;

    private final BookingClinicalSpecialtyMandatoryMedicalPracticeRepository repository;

    public final JavaMailSender emailSender;

    public final TemplateEngine templateEngine;

    private final BookingAppointmentStorage bookingAppointmentStorage;

    private final BookingPracticeRepository bookingPracticeRepository;

    private final BookingClinicalSpecialtyJpaRepository bookingClinicalSpecialtyJpaRepository;

    private static final String RECOMMENDATION = "- Presentarse 15 min antes con el carnet de la Obra Social.\n";
    private static final String SUBJECT = "Confirmación de turno online";
    private static final String ROUTE = "home/cancelacion?code=";

    public BookingConfirmationMailSenderImpl(
            TemplateEngine templateEngine,
            JavaMailSender emailSender,
            BookingAutoConfiguration bookingAutoConfiguration,
            BookingClinicalSpecialtyMandatoryMedicalPracticeRepository repository,
            BookingAppointmentStorage bookingAppointmentStorage,
            BookingPracticeRepository bookingPracticeRepository,
			BookingClinicalSpecialtyJpaRepository bookingClinicalSpecialtyJpaRepository
    ) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
        this.bookingAutoConfiguration = bookingAutoConfiguration;
        this.repository = repository;
        this.bookingAppointmentStorage = bookingAppointmentStorage;
        this.bookingPracticeRepository = bookingPracticeRepository;
        this.bookingClinicalSpecialtyJpaRepository = bookingClinicalSpecialtyJpaRepository;
    }

    @Override
    public void sendEmail(BookingBo bookingBo, String uuid) {
        String template = "email-confirmar-reserva";
        String cancelationLink = bookingAutoConfiguration.getApiBase()
                .concat(ROUTE)
                .concat(uuid);

        Mail mail = loadDataEmail(bookingBo, cancelationLink, uuid);
        try {
            sendTemplatedEmail(mail, template);
        } catch (MailException | MessagingException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    protected Mail loadDataEmail(BookingBo bookingBo, String cancelationLink, String uuid) {
        Mail mail = new Mail();
        mail.setTo(bookingBo.getAppointmentDataEmail());
        mail.setSubject(SUBJECT);
        Map<String, Object> model = new HashMap<>();

        model.put("cancelationLink", cancelationLink);
        model.put("namePatient",getPatienName(bookingBo, uuid));
        model.put("date", getDate(bookingBo));
        model.put("nameProfessional",getProfessionalName(bookingBo));
        model.put("specialty",getSpecialty(bookingBo) + " - " + getPractice(bookingBo));
        model.put("institution",getInstitution());
        model.put("recomendation", getRecommendations(bookingBo));
        mail.setModel(model);
        return mail;
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
        var a = repository
				.findBySnomedIdAndSpecialtyId(bookingBo.bookingAppointment.getSnomedId(), bookingBo.bookingAppointment.getSpecialtyId());
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

    private void sendTemplatedEmail(Mail mail, String template) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.host", bookingAutoConfiguration.getHost());
        props.put("mail.port", bookingAutoConfiguration.getPort());
        props.put("mail.transport.protocol",bookingAutoConfiguration.getProtocol());
        props.put("mail.smtp.auth", bookingAutoConfiguration.getAuth());
        props.put("mail.smtp.starttls.enable", bookingAutoConfiguration.getEnable());
        props.put("mail.smtp.ssl.enable", bookingAutoConfiguration.getEnable());

        // Get the Session object.
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(bookingAutoConfiguration.getFrom(), bookingAutoConfiguration.getPassword());
                    }
                });

        try {
            Context context = new Context();
            context.setVariables(mail.getModel());
            String html = templateEngine.process(template, context);

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(bookingAutoConfiguration.getFrom()));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(mail.getTo()));
            message.setSubject(mail.getSubject());
            message.setContent(html,
                    "text/html");
            Transport.send(message);

        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
