package net.pladema.medicalconsultation.appointment.controller.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedAppointmentPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingAppointmentDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingPersonDto;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.controller.service.AppointmentExternalService;
import net.pladema.medicalconsultation.appointment.repository.domain.BookingAppointmentBo;
import net.pladema.medicalconsultation.appointment.repository.domain.BookingPersonBo;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.CreateAppointmentService;
import net.pladema.medicalconsultation.appointment.service.booking.BookingPersonService;
import net.pladema.medicalconsultation.appointment.service.booking.CreateBookingAppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;

@Slf4j
@Service
public class AppointmentExternalServiceImpl implements AppointmentExternalService, SharedAppointmentPort {

	private static final String OUTPUT = "Output -> {}";

	private final AppointmentService appointmentService;

	private final CreateAppointmentService createAppointmentService;
	private final BookingPersonService bookingPersonService;
	private final CreateBookingAppointmentService createBookingAppointmentService;

	public AppointmentExternalServiceImpl(AppointmentService appointmentService,
										  CreateAppointmentService createAppointmentService,
										  BookingPersonService bookingPersonService,
										  CreateBookingAppointmentService createBookingAppointmentService) {
		this.appointmentService = appointmentService;
		this.createAppointmentService = createAppointmentService;
		this.bookingPersonService = bookingPersonService;
		this.createBookingAppointmentService = createBookingAppointmentService;
	}

	@Override
	public boolean hasConfirmedAppointment(Integer patientId, Integer healthProfessionalId, LocalDate date) {
		log.debug("Input parameters -> patientId {}, healthProfessionalId {}, date {}", patientId, healthProfessionalId, date);
		boolean result = appointmentService.hasConfirmedAppointment(patientId, healthProfessionalId, date);
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public void serveAppointment(Integer patientId, Integer healthcareProfessionalId, LocalDate date) {
		log.debug("Input parameters -> patientId {}, healthcareProfessionalId {}, date {}", patientId, healthcareProfessionalId, date);
		Integer appointmentId = appointmentService.getAppointmentsId(patientId, healthcareProfessionalId, date).get(0);
		appointmentService.updateState(appointmentId, AppointmentState.SERVED, UserInfo.getCurrentAuditor(), null);
		log.debug(OUTPUT, Boolean.TRUE);
	}

	@Override
	public Integer getMedicalCoverage(Integer patientId, Integer healthcareProfessionalId) {
		log.debug("Appointment Service -> method: {}", "getMedicalCoverage");
		log.debug("Input parameters -> patientId {}, healthcareProfessionalId {}", patientId, healthcareProfessionalId);

		LocalDate currentDate = LocalDate.now();
		Integer medicalCoverage = appointmentService
				.getMedicalCoverage(patientId, healthcareProfessionalId, currentDate);
		log.debug(OUTPUT, medicalCoverage);
		return medicalCoverage;
	}

	@Override
	public String saveBooking(
			BookingAppointmentDto bookingAppointmentDto,
			BookingPersonDto bookingPersonDto,
			String email
	) {
		log.debug("Appointment Service -> method: {}", "saveBooking");
		log.debug("Input parameters -> bookingAppointmentDto {}, bookingPersonDto {}", bookingAppointmentDto, bookingPersonDto);
		Integer bookingPersonId;
		if(bookingPersonDto == null) {
			var b = bookingPersonService.findByEmail(email);
			if(b.isPresent())
				bookingPersonId = b.get().getId();
			else
				throw new ConstraintViolationException("El mail no existe", Collections.emptySet());
		}
		else bookingPersonId = bookingPersonService.save(mapToBookingPerson(bookingPersonDto));

		AppointmentBo newAppointmentBo = mapTo(bookingAppointmentDto);
		newAppointmentBo = createAppointmentService.execute(newAppointmentBo);
		Integer appointmentId = newAppointmentBo.getId();

		BookingAppointmentBo bookingAppointmentBo = getBookingAppointmentBo(appointmentId, bookingPersonId);
		createBookingAppointmentService.execute(bookingAppointmentBo);

		log.debug(OUTPUT,bookingAppointmentBo);
		return bookingAppointmentBo.getUuid();
	}

	@Override
	public boolean existsEmail(String email) {
		return bookingPersonService.exists(email);
	}

	@Override
	public void cancelBooking(String uuid) {
		bookingPersonService.deleteByUuid(uuid);
	}

	@Override
	public Optional<String> getPatientName(String uuid) {
		return bookingPersonService.getPatientName(uuid);
	}

	@Override
	public Optional<String> getProfessionalName(Integer diaryId) {
		return bookingPersonService.getProfessionalName(diaryId);
	}


	@NotNull
	private BookingAppointmentBo getBookingAppointmentBo(Integer appointmentId, Integer bookingPersonId) {
		return new BookingAppointmentBo(
				bookingPersonId,
				appointmentId,
				UUID.randomUUID().toString()
		);
	}

	private BookingPersonBo mapToBookingPerson(BookingPersonDto bookingPersonDto) {
		return new BookingPersonBo(
				bookingPersonDto.getBirthDate(),
				bookingPersonDto.getEmail(),
				bookingPersonDto.getFirstName(),
				bookingPersonDto.getGenderId(),
				bookingPersonDto.getIdNumber(),
				bookingPersonDto.getLastName()
		);
	}

	private AppointmentBo mapTo(BookingAppointmentDto bookingAppointmentDto) {
		AppointmentBo appointmentBo = new AppointmentBo();
		appointmentBo.setDiaryId(bookingAppointmentDto.getDiaryId());
		appointmentBo.setDate(LocalDate.parse(bookingAppointmentDto.getDay()));
		appointmentBo.setHour(LocalTime.parse(bookingAppointmentDto.getHour()));
		appointmentBo.setAppointmentStateId(AppointmentState.BOOKED);
		appointmentBo.setOverturn(false);
		appointmentBo.setPhoneNumber(bookingAppointmentDto.getPhoneNumber());
		appointmentBo.setOpeningHoursId(bookingAppointmentDto.getOpeningHoursId());
		appointmentBo.setSnomedId(bookingAppointmentDto.getSnomedId());
		return appointmentBo;
	}
}
