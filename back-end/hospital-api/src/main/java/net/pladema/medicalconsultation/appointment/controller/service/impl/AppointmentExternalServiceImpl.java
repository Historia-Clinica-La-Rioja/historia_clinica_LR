package net.pladema.medicalconsultation.appointment.controller.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.AppointmentDataDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.BookingPersonMailNotExistsException;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.ProfessionalAlreadyBookedException;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.InstitutionInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceAppointmentStateDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SavedBookingAppointmentDto;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.medicalconsultation.appointment.repository.entity.BookingPerson;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentSummaryBo;

import net.pladema.medicalconsultation.diary.service.DiaryService;

import net.pladema.person.repository.domain.CompletePersonNameBo;
import net.pladema.person.service.PersonService;

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

	private final DiaryService diaryService;

	private final AppointmentValidatorService appointmentValidatorService;
	private final CreateAppointmentService createAppointmentService;
	private final BookingPersonService bookingPersonService;
	private final CreateBookingAppointmentService createBookingAppointmentService;
	private final DocumentAppointmentService documentAppointmentService;
	private final FetchAppointments fetchAppointments;
	private final LocalDateMapper localDateMapper;
	private final SharedPersonPort sharedPersonPort;
	private final PersonService personService;
	private final FeatureFlagsService featureFlagsService;

	public AppointmentExternalServiceImpl(AppointmentService appointmentService, AppointmentValidatorService appointmentValidatorService,
										  CreateAppointmentService createAppointmentService, BookingPersonService bookingPersonService,
										  CreateBookingAppointmentService createBookingAppointmentService, DocumentAppointmentService documentAppointmentService,
										  FetchAppointments fetchAppointments, LocalDateMapper localDateMapper,
										  SharedPersonPort sharedPersonPort, DiaryService diaryService, PersonService personService, FeatureFlagsService featureFlagsService) {
		this.appointmentService = appointmentService;
		this.appointmentValidatorService = appointmentValidatorService;
		this.createAppointmentService = createAppointmentService;
		this.bookingPersonService = bookingPersonService;
		this.createBookingAppointmentService = createBookingAppointmentService;
		this.documentAppointmentService = documentAppointmentService;
		this.fetchAppointments = fetchAppointments;
		this.localDateMapper = localDateMapper;
		this.sharedPersonPort = sharedPersonPort;
		this.diaryService = diaryService;
		this.personService = personService;
		this.featureFlagsService = featureFlagsService;
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
	public SavedBookingAppointmentDto saveBooking(
			BookingAppointmentDto bookingAppointmentDto,
			BookingPersonDto bookingPersonDto,
			String email
	) throws ProfessionalAlreadyBookedException, BookingPersonMailNotExistsException {
		log.debug("saveBooking -> bookingAppointmentDto {}, bookingPersonDto {}", bookingAppointmentDto, bookingPersonDto);
		BookingPerson bookingPerson = getBookingPerson(bookingPersonDto, email);
		assertProfessionalNotAlreadyBooked(bookingPerson, bookingAppointmentDto);
		AppointmentBo newAppointmentBo = mapTo(bookingAppointmentDto);
		newAppointmentBo.setPhoneNumber(bookingAppointmentDto.getPhoneNumber());
		newAppointmentBo.setPhonePrefix(bookingAppointmentDto.getPhonePrefix());

		newAppointmentBo = createAppointmentService.execute(newAppointmentBo);
		Integer appointmentId = newAppointmentBo.getId();

		BookingAppointmentBo bookingAppointmentBo = getBookingAppointmentBo(appointmentId, bookingPerson.getId());
		createBookingAppointmentService.execute(bookingAppointmentBo);

		log.debug(OUTPUT,bookingAppointmentBo);
		return new SavedBookingAppointmentDto(
				bookingAppointmentBo.getBookingPersonId(),
				bookingAppointmentBo.getAppointmentId(),
				bookingAppointmentBo.getUuid());
	}

	private void assertProfessionalNotAlreadyBooked(BookingPerson bookingPerson, BookingAppointmentDto bookingAppointmentDto) throws ProfessionalAlreadyBookedException {
		if (featureFlagsService.isOn(AppFeature.HABILITAR_LIMITE_TURNOS_PERSONA_PROFESIONAL)) {
			Optional<CompletePersonNameBo> completePersonNameBo = personService.findByHealthcareProfessionalPersonDataByDiaryId(bookingAppointmentDto.getDiaryId());
			if (completePersonNameBo.isPresent()) {
				Collection<AppointmentBo> appointments = appointmentService.hasAppointment(
						bookingPerson.getIdentificationNumber(),
						completePersonNameBo.get().getHealthcareProfessionalId()
				);
				if ( ! appointments.isEmpty())
					throw new ProfessionalAlreadyBookedException();
			}
		}
	}

	private BookingPerson getBookingPerson(BookingPersonDto bookingPersonDto, String requestingBookingPersonEmail) throws BookingPersonMailNotExistsException {
		BookingPerson bookingPerson;
		if(bookingPersonDto == null)
			bookingPerson = bookingPersonService.findByEmail(requestingBookingPersonEmail)
					.orElseThrow(BookingPersonMailNotExistsException::new);
		else
			bookingPerson = searchOrSaveBookingPerson(bookingPersonDto);
		return bookingPerson;
	}

	private BookingPerson searchOrSaveBookingPerson(BookingPersonDto bookingPersonDto) {
		BookingPerson bookingPerson = null;
		if(bookingPersonDto.getEmail() != null && bookingPersonDto.getIdNumber() != null)
			bookingPerson = bookingPersonService.findByEmailAndIdentificationNumber(bookingPersonDto.getEmail(), bookingPersonDto.getIdNumber())
					.stream().findFirst().orElse(null);
		if(bookingPerson == null)
			return bookingPersonService.save(mapToBookingPerson(bookingPersonDto));
		return bookingPerson;
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

	@Override
	public List<ReferenceAppointmentStateDto> getReferencesAppointmentState(Map<Integer, List<Integer>> referenceAppointments) {
		List<ReferenceAppointmentStateDto> result = new ArrayList<>();
		for (Map.Entry<Integer, List<Integer>> e : referenceAppointments.entrySet()) {
			Optional<AppointmentSummaryBo> appointment = getNearestAppointment(e.getValue());
            appointment.ifPresent(appointmentSummaryBo -> result.add(new ReferenceAppointmentStateDto(e.getKey(), appointmentSummaryBo.getStateId())));
		}
		return result;
	}

	@Override
	public Optional<AppointmentDataDto> getNearestAppointmentData(List<Integer> appointments) {
		log.debug("Get nearest appointment by appointmentsIds {}", appointments);
		Optional<AppointmentSummaryBo> appointment = getNearestAppointment(appointments);
		return appointment.map(this::mapFromAppointmentSummaryBo);
	}

	@Override
	public Integer getDiaryId(Integer appointmentId) {
		return diaryService.getDiaryIdByAppointment(appointmentId);
	}

	@Override
	public Integer getInstitutionId(Integer diaryId){
		log.debug("Input parameters -> diaryId {}", diaryId);
		Integer result = diaryService.getInstitution(diaryId);
		log.debug("Output -> {}", result);
		return result;
	}

	private Optional<AppointmentSummaryBo> getNearestAppointment(List<Integer> appointmentIds) {
		List<AppointmentSummaryBo> appointments = this.appointmentService.getAppointmentDataByAppointmentIds(appointmentIds);
		List<AppointmentSummaryBo> futureAppointments = appointments.stream()
				.filter(a -> a.getDate().equals(LocalDate.now()) || a.getDate().isAfter(LocalDate.now()))
				.sorted(Comparator.comparing(AppointmentSummaryBo::getDate).thenComparing(AppointmentSummaryBo::getHour))
				.collect(Collectors.toList());
		return !futureAppointments.isEmpty() ? determineNearestAppointmentByStateId(futureAppointments) : !appointments.isEmpty() ? determineNearestAppointmentByStateId(appointments) : Optional.empty();
	}

	private Optional<AppointmentSummaryBo> determineNearestAppointmentByStateId(List<AppointmentSummaryBo> appointments) {
		var appointmentsNonCancelled = appointments.stream()
				.filter(a -> !a.getStateId().equals(AppointmentState.CANCELLED))
				.collect(Collectors.toList());
		if (!appointmentsNonCancelled.isEmpty())
			return Optional.of(appointmentsNonCancelled.get(0));
		return Optional.of(appointments.get(0));
	}

	private AppointmentDataDto mapFromAppointmentSummaryBo(AppointmentSummaryBo appointment) {
		return AppointmentDataDto.builder()
				.appointmentId(appointment.getId())
				.state(appointment.getStateId())
				.institution(new InstitutionInfoDto(appointment.getInstitution().getId(), appointment.getInstitution().getName()))
				.date(localDateMapper.toDateDto(appointment.getDate()))
				.hour(localDateMapper.toTimeDto(appointment.getHour()))
				.phonePrefix(appointment.getPhonePrefix())
				.phoneNumber(appointment.getPhoneNumber())
				.professionalFullName(getCompletePersonNameByParams(appointment.getProfessionalFirstName(), appointment.getProfessionalMiddleNames(),
						appointment.getProfessionalLastName(), appointment.getProfessionalOtherLastNames(), appointment.getProfessionalNameSelfDetermination()))
				.patientEmail(appointment.getPatientEmail())
				.authorFullName(getCompletePersonNameById(appointment.getAuthorPersonId()))
				.createdOn(localDateMapper.toDateTimeDto(appointment.getCreatedOn()))
				.build();
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
		return new PublicAppointmentClinicalSpecialty(clinicalSpecialty.getSctid(), clinicalSpecialty.getId(), clinicalSpecialty.getName());
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
				.phonePrefix(bookingPersonDto.getPhonePrefix())
				.phoneNumber(bookingPersonDto.getPhoneNumber())
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
		appointmentBo.setPhonePrefix(bookingAppointmentDto.getPhonePrefix());
		appointmentBo.setOpeningHoursId(bookingAppointmentDto.getOpeningHoursId());
		appointmentBo.setSnomedId(bookingAppointmentDto.getSnomedId());
		appointmentBo.setModalityId((short)1);
		return appointmentBo;
	}
	private String getCompletePersonNameByParams(String firstName, String middleNames, String lastName,
												 String otherLastNames, String nameSelfDetermination) {
		return sharedPersonPort.parseCompletePersonName(firstName, middleNames, lastName, otherLastNames, nameSelfDetermination);
	}

	private String getCompletePersonNameById(Integer personId) {
		return sharedPersonPort.getCompletePersonNameById(personId);
	}

}
