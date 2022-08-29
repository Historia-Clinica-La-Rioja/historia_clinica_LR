package net.pladema.medicalconsultation.appointment.controller.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.SharedAppointmentPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.DocumentAppointmentDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.PublicAppointmentClinicalSpecialty;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.PublicAppointmentDoctorDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.PublicAppointmentInstitution;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.PublicAppointmentListDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.PublicAppointmentMedicalCoverage;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.PublicAppointmentPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.PublicAppointmentPersonDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.PublicAppointmentStatus;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingAppointmentDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingPersonDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.controller.service.AppointmentExternalService;
import net.pladema.medicalconsultation.appointment.repository.domain.BookingAppointmentBo;
import net.pladema.medicalconsultation.appointment.repository.domain.BookingPersonBo;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.AppointmentValidatorService;
import net.pladema.medicalconsultation.appointment.service.CreateAppointmentService;
import net.pladema.medicalconsultation.appointment.service.DocumentAppointmentService;
import net.pladema.medicalconsultation.appointment.service.booking.BookingPersonService;
import net.pladema.medicalconsultation.appointment.service.booking.CreateBookingAppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.appointment.service.domain.DocumentAppointmentBo;
import net.pladema.medicalconsultation.appointment.service.fetchappointments.FetchAppointments;
import net.pladema.medicalconsultation.appointment.service.fetchappointments.domain.AppointmentFilterBo;
import net.pladema.medicalconsultation.appointment.service.fetchappointments.domain.AppointmentInfoBo;
import net.pladema.medicalconsultation.appointment.service.fetchappointments.domain.AppointmentStatusBo;
import net.pladema.medicalconsultation.appointment.service.fetchappointments.domain.ClinicalSpecialtyBo;
import net.pladema.medicalconsultation.appointment.service.fetchappointments.domain.DoctorBo;
import net.pladema.medicalconsultation.appointment.service.fetchappointments.domain.InstitutionBo;
import net.pladema.medicalconsultation.appointment.service.fetchappointments.domain.MedicalCoverageBo;
import net.pladema.medicalconsultation.appointment.service.fetchappointments.domain.PatientBo;

@Slf4j
@Service
public class AppointmentExternalServiceImpl implements AppointmentExternalService, SharedAppointmentPort {

	private static final String OUTPUT = "Output -> {}";

	private final AppointmentService appointmentService;
	private final AppointmentValidatorService appointmentValidatorService;
	private final CreateAppointmentService createAppointmentService;
	private final BookingPersonService bookingPersonService;
	private final CreateBookingAppointmentService createBookingAppointmentService;
	private final DocumentAppointmentService documentAppointmentService;
	private final FetchAppointments fetchAppointments;
	private final LocalDateMapper localDateMapper;

	public AppointmentExternalServiceImpl(AppointmentService appointmentService, AppointmentValidatorService appointmentValidatorService, CreateAppointmentService createAppointmentService, BookingPersonService bookingPersonService, CreateBookingAppointmentService createBookingAppointmentService, DocumentAppointmentService documentAppointmentService, FetchAppointments fetchAppointments, LocalDateMapper localDateMapper) {
		this.appointmentService = appointmentService;
		this.appointmentValidatorService = appointmentValidatorService;
		this.createAppointmentService = createAppointmentService;
		this.bookingPersonService = bookingPersonService;
		this.createBookingAppointmentService = createBookingAppointmentService;
		this.documentAppointmentService = documentAppointmentService;
		this.fetchAppointments = fetchAppointments;
		this.localDateMapper = localDateMapper;
	}

	@Override
	public boolean hasCurrentAppointment(Integer patientId, Integer healthProfessionalId, LocalDate date) {
		log.debug("Input parameters -> patientId {}, healthProfessionalId {}, date {}", patientId, healthProfessionalId, date);
		boolean result = appointmentService.hasCurrentAppointment(patientId, healthProfessionalId, date);
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public boolean hasOldAppointment(Integer patientId, Integer healthProfessionalId) {
		log.debug("Input parameters -> patientId {}, healthProfessionalId {}", patientId, healthProfessionalId);
		boolean result = appointmentService.hasOldAppointment(patientId, healthProfessionalId);
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public Integer serveAppointment(Integer patientId, Integer healthcareProfessionalId, LocalDate date) {
		log.debug("Input parameters -> patientId {}, healthcareProfessionalId {}, date {}", patientId, healthcareProfessionalId, date);
		Integer appointmentId;
		List<Integer> currentsAppointments = appointmentService.getAppointmentsId(patientId, healthcareProfessionalId, date);
			if(!currentsAppointments.isEmpty())
				appointmentId = currentsAppointments.get(0);
			else
				appointmentId = appointmentService.getOldAppointments(patientId, healthcareProfessionalId).get(0);
		appointmentService.updateState(appointmentId, AppointmentState.SERVED, UserInfo.getCurrentAuditor(), null);
		log.debug(OUTPUT, Boolean.TRUE);
		return appointmentId;
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
	public void cancelAppointment(Integer institutionId, Integer appointmentId, String reason) {
		log.debug("CancelAppointment -> institutionId {}, appointmentId {}", institutionId, appointmentId);
		appointmentValidatorService.validateStateUpdate(institutionId, appointmentId, AppointmentState.CANCELLED, reason);
		appointmentService.updateState(appointmentId, AppointmentState.CANCELLED, UserInfo.getCurrentAuditor(), reason);
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

	@Override
	public List<PublicAppointmentListDto> fetchAppointments(Integer institutionId, String identificationNumber,
															List<Short> includeAppointmentStatus,
															LocalDate startDate, LocalDate endDate) {
		return fetchAppointments.run(new AppointmentFilterBo(institutionId, identificationNumber, includeAppointmentStatus, startDate, endDate))
				.stream()
				.map(this::mapToFromAppointmentInfoBo)
				.collect(Collectors.toList());
	}

	@Override
	public void saveDocumentAppointment(DocumentAppointmentDto documentAppointmentDto) {
		this.documentAppointmentService.save(DocumentAppointmentBo.makeTo(documentAppointmentDto));
	}

	@Override
	public void deleteDocumentAppointment(DocumentAppointmentDto documentAppointmentDto) {
		this.documentAppointmentService.delete(DocumentAppointmentBo.makeTo(documentAppointmentDto));
	}

	private PublicAppointmentListDto mapToFromAppointmentInfoBo(AppointmentInfoBo appointmentInfoBo) {
		return new PublicAppointmentListDto(appointmentInfoBo.getId(),
				localDateMapper.fromLocalDateToString(appointmentInfoBo.getDateTypeId()),
				localDateMapper.fromLocalTimeToString(appointmentInfoBo.getHour()),
				appointmentInfoBo.isOverturn(), appointmentInfoBo.getPhone(),
				buildPatientDto(appointmentInfoBo.getPatient()),
				buildDoctorDto(appointmentInfoBo.getDoctor()),
				buildStatus(appointmentInfoBo.getStatus()),
				buildMedicalCoverage(appointmentInfoBo.getMedicalCoverage()),
				buildInstitution(appointmentInfoBo.getInstitution()),
				buildClinicalSpecialty(appointmentInfoBo.getClinicalSpecialty()));
	}

	private PublicAppointmentDoctorDto buildDoctorDto(DoctorBo doctor) {
		return new PublicAppointmentDoctorDto(
				doctor.getLicenseNumber(),
				PublicAppointmentPersonDto.builder()
						.firstName(doctor.getFirstName())
						.lastName(doctor.getLastName())
						.identificationNumber(doctor.getIdentificationNumber())
						.build()
		);
	}

	private PublicAppointmentPatientDto buildPatientDto(PatientBo patient) {
		return new PublicAppointmentPatientDto(
				patient.getId(),
				PublicAppointmentPersonDto.builder()
						.firstName(patient.getFirstName())
						.lastName(patient.getLastName())
						.identificationNumber(patient.getIdentificationNumber())
						.genderId(patient.getGenderId())
					.build()
		);
	}
	private PublicAppointmentClinicalSpecialty buildClinicalSpecialty(ClinicalSpecialtyBo clinicalSpecialty) {
		return new PublicAppointmentClinicalSpecialty(clinicalSpecialty.getSctid(), clinicalSpecialty.getName());
	}
	private PublicAppointmentInstitution buildInstitution(InstitutionBo institution) {
		return new PublicAppointmentInstitution(institution.getId(), institution.getCuit(), institution.getSisaCode());
	}
	private PublicAppointmentStatus buildStatus(AppointmentStatusBo status) {
		return new PublicAppointmentStatus(status.getId(), status.getDescription());
	}
	private PublicAppointmentMedicalCoverage buildMedicalCoverage(MedicalCoverageBo medicalCoverage) {
		return medicalCoverage != null ?
				new PublicAppointmentMedicalCoverage(medicalCoverage.getCuit(), medicalCoverage.getName(), medicalCoverage.getAffiliateNumber())
				: null ;
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
		return BookingPersonBo.builder()
				.birthDate(localDateMapper.fromStringToLocalDate(bookingPersonDto.getBirthDate()))
				.email(bookingPersonDto.getEmail())
				.firstName(bookingPersonDto.getFirstName())
				.genderId(bookingPersonDto.getGenderId())
				.idNumber(bookingPersonDto.getIdNumber())
				.lastName(bookingPersonDto.getLastName())
				.build();
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
