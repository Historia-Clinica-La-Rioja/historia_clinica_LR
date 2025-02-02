package ar.lamansys.online.infraestructure.input.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import ar.lamansys.online.application.FetchIfAppointmentCanBeAssignedAsOverturn;
import ar.lamansys.online.application.FetchIfAppointmentWereAlreadyAssigned;
import ar.lamansys.online.application.FetchIfOpeningHoursAllowsWebAppointments;
import ar.lamansys.online.application.booking.CheckIfMailExists;
import ar.lamansys.online.application.integration.FetchBookingInstitutionsExtended;
import ar.lamansys.online.application.specialty.FetchSpecialtiesByProfessionals;
import ar.lamansys.online.domain.integration.BookingInstitutionExtendedBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.BookingCannotSendEmailException;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.SaveExternalBookingException;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.SaveExternalBookingExceptionEnum;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.BookingPersonMailNotExistsException;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.ProfessionalAlreadyBookedException;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingInstitutionExtendedDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SavedBookingAppointmentDto;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;

import org.springframework.stereotype.Service;

import ar.lamansys.online.application.booking.BookAppointment;
import ar.lamansys.online.application.booking.CancelBooking;
import ar.lamansys.online.application.insurance.FetchHealthcareInsurances;
import ar.lamansys.online.application.integration.FetchBookingInstitutions;
import ar.lamansys.online.application.professional.FetchAvailabilityByPractice;
import ar.lamansys.online.application.professional.FetchAvailabilityByPracticeAndProfessional;
import ar.lamansys.online.application.professional.FetchBookingProfessionals;
import ar.lamansys.online.application.specialty.FetchPracticesByProfessionalAndHealthInsurance;
import ar.lamansys.online.application.specialty.FetchPracticesBySpecialtyAndHealthInsurance;
import ar.lamansys.online.application.specialty.FetchSpecialties;
import ar.lamansys.online.application.specialty.FetchSpecialtiesByProfessional;
import ar.lamansys.online.domain.booking.BookingAppointmentBo;
import ar.lamansys.online.domain.booking.BookingBo;
import ar.lamansys.online.domain.booking.BookingPersonBo;
import ar.lamansys.online.domain.professional.AvailabilityBo;
import ar.lamansys.online.domain.professional.BookingProfessionalBo;
import ar.lamansys.online.domain.professional.DiaryAvailabilityBo;
import ar.lamansys.online.domain.professional.DiaryListBo;
import ar.lamansys.online.domain.professional.ProfessionalAvailabilityBo;
import ar.lamansys.online.domain.specialty.BookingSpecialtyBo;
import ar.lamansys.online.domain.specialty.PracticeBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.AvailabilityDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingAppointmentDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingDiaryDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingHealthInsuranceDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingInstitutionDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingPersonDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingProfessionalDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingSpecialtyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.DiaryAvailabilityDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.PracticeDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.ProfessionalAvailabilityDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SharedBookingPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class BookingExternalService implements SharedBookingPort {
	private final BookAppointment bookAppointment;
	private final CancelBooking cancelBooking;
	private final FetchBookingInstitutions fetchBookingInstitutions;
	private final FetchBookingInstitutionsExtended fetchBookingInstitutionsExtended;
	private final FetchHealthcareInsurances fetchHealthcareInsurances;
	private final FetchPracticesBySpecialtyAndHealthInsurance fetchPracticesBySpecialtyAndHealthInsurance;
	private final FetchPracticesByProfessionalAndHealthInsurance fetchPracticesByProfessionalAndHealthInsurance;
	private final FetchSpecialties fetchSpecialties;
	private final FetchSpecialtiesByProfessional fetchSpecialtiesByProfessional;
	private final FetchBookingProfessionals fetchBookingProfessionals;
	private final FetchAvailabilityByPracticeAndProfessional fetchAvailabilityByPracticeAndProfessional;
	private final FetchAvailabilityByPractice fetchAvailabilityByPractice;
	private final FetchIfOpeningHoursAllowsWebAppointments fetchIfOpeningHoursAllowsWebAppointments;
	private final FetchIfAppointmentWereAlreadyAssigned fetchIfAppointmentWereAlreadyAssigned;
	private final FetchIfAppointmentCanBeAssignedAsOverturn fetchIfAppointmentCanBeAssignedAsOverturn;
	private final CheckIfMailExists checkIfMailExists;
	private final FetchSpecialtiesByProfessionals fetchSpecialtiesByProfessionals;

	private final FeatureFlagsService featureFlagsService;

	public SavedBookingAppointmentDto makeBooking(BookingDto bookingDto, boolean onlineBooking) throws ProfessionalAlreadyBookedException, BookingPersonMailNotExistsException, SaveExternalBookingException, BookingCannotSendEmailException {
		assertValidAppointment(bookingDto.getBookingAppointmentDto());
		BookingBo bookingBo = new BookingBo(
				bookingDto.getAppointmentDataEmail(),
				mapToAppointment(bookingDto.getBookingAppointmentDto()),
				mapToPerson(bookingDto.getBookingPersonDto()),
				onlineBooking
		);
		return bookAppointment.run(bookingBo);
	}

	private void assertValidAppointment(BookingAppointmentDto bookingAppointment) throws SaveExternalBookingException {
		Boolean openingHoursAllowWebAppointments = fetchIfOpeningHoursAllowsWebAppointments.run(bookingAppointment.getDiaryId(), bookingAppointment.getOpeningHoursId());
		if (openingHoursAllowWebAppointments == null || !openingHoursAllowWebAppointments)
			throw new SaveExternalBookingException(SaveExternalBookingExceptionEnum.OPENING_HOURS_DOES_NOT_ALLOW_EXTERNAL_APPOINTMENTS, "La franja horaria no admite turnos web");
		LocalTime appointmentTime = LocalTime.parse(bookingAppointment.getHour());
		LocalDate appointmentDate = LocalDate.parse(bookingAppointment.getDay());
		Boolean appointmentAlreadyAssigned = fetchIfAppointmentWereAlreadyAssigned.run(bookingAppointment.getDiaryId(), bookingAppointment.getOpeningHoursId(), appointmentDate, appointmentTime);
		if (!appointmentAlreadyAssigned)
			return;
		if (!featureFlagsService.isOn(AppFeature.HABILITAR_SOBRETURNOS_API_PUBLICA))
			throw new SaveExternalBookingException(SaveExternalBookingExceptionEnum.OVERTURN_CANNOT_BE_CREATED, "No puede asignarse como sobreturno");
		boolean canBeOverturn = fetchIfAppointmentCanBeAssignedAsOverturn.run(bookingAppointment.getDiaryId(), bookingAppointment.getOpeningHoursId(), appointmentDate);
		if (!canBeOverturn)
			throw new SaveExternalBookingException(SaveExternalBookingExceptionEnum.APPOINTMENT_CANNOT_BE_CREATED, "El turno no puede ser asignado");
	}

	public void cancelBooking(String uuid) {
		cancelBooking.run(uuid);
		log.debug("cancel booking {}", uuid);
	}

	@Override
	public List<BookingInstitutionDto> fetchAllBookingInstitutions() {
		return fetchBookingInstitutions.run().stream()
				.map(institution -> new BookingInstitutionDto(institution.getId(), institution.getDescription()))
				.collect(Collectors.toList());
	}

	@Override
	public List<BookingInstitutionExtendedDto> fetchAllBookingInstitutionsExtended() {
		return fetchBookingInstitutionsExtended.run().stream()
				.map(this::buildExtendedDto)
				.collect(Collectors.toList());
	}

	private BookingInstitutionExtendedDto buildExtendedDto(BookingInstitutionExtendedBo institution) {
		return BookingInstitutionExtendedDto.builder()
				.id(institution.getId())
				.aliases(institution.getAliases())
				.city(institution.getCity())
				.department(institution.getDepartment())
				.address(institution.getAddress())
				.clinicalSpecialtiesNames(institution.getClinicalSpecialtiesNames())
				.sisaCode(institution.getSisaCode())
				.description(institution.getDescription())
				.dependency(institution.getDependency())
				.build();
	}

	@Override
	public List<BookingHealthInsuranceDto> fetchMedicalCoverages() {
		List<BookingHealthInsuranceDto> result = fetchHealthcareInsurances.run().stream()
				.map(insurance -> new BookingHealthInsuranceDto(insurance.getId(), insurance.getName()))
				.collect(Collectors.toList());
		log.debug("Get all booking institutions => {}", result);
		return result;
	}

	@Override
	public List<PracticeDto> fetchBookingPracticesBySpecialtyAndHealthInsurance(Integer clinicalSpecialtyId, Integer medicalCoverageId, boolean all) {
		var practiceBo= fetchPracticesBySpecialtyAndHealthInsurance.run(clinicalSpecialtyId, medicalCoverageId, all);
		List<PracticeDto> result = practiceBo.stream()
				.map((PracticeBo t) -> new PracticeDto(t.getId(), t.getDescription(), t.getCoverage(), t.getCoverageText(), t.getSnomedId()))
				.collect(Collectors.toList());
		log.debug("Get all practices by Clinical Specialty {} and by HealthInsurance {} => {}",
				clinicalSpecialtyId, medicalCoverageId,
				result);
		return result;
	}

	@Override
	public List<PracticeDto> fetchBookingPracticesByProfessionalAndHealthInsurance(Integer healthcareProfessionalId, Integer medicalCoverageId, Integer clinicalSpecialtyId, boolean all) {
		var practiceBo= fetchPracticesByProfessionalAndHealthInsurance.run(healthcareProfessionalId, medicalCoverageId, clinicalSpecialtyId, all);
		List<PracticeDto> result = practiceBo.stream()
				.map((PracticeBo t) -> new PracticeDto(t.getId(), t.getDescription(), t.getCoverage(), t.getCoverageText(), t.getSnomedId()))
				.collect(Collectors.toList());
		log.debug("Get all practices by healthcareProfessionalId {} and by HealthInsurance {} and by clinical specialty {} => {}", healthcareProfessionalId, medicalCoverageId, clinicalSpecialtyId,
				result);
		return result;
	}

	@Override
	public List<BookingSpecialtyDto> fetchBookingSpecialties() {
		List<BookingSpecialtyBo> practices = fetchSpecialties.run();
		List<BookingSpecialtyDto> result = practices.stream()
				.map(b -> new BookingSpecialtyDto(b.getId(), b.getDescription()))
				.collect(Collectors.toList());
		log.debug("Get all practices => {}", result);
		return result;
	}

	@Override
	public List<BookingSpecialtyDto> fetchBookingSpecialtiesByProfessionals() {
		List<BookingSpecialtyBo> specialties = fetchSpecialtiesByProfessionals.run();
		List<BookingSpecialtyDto> result = specialties.stream()
				.map(b -> new BookingSpecialtyDto(b.getId(), b.getDescription()))
				.collect(Collectors.toList());
		log.debug("Get all specialties => {}", result);
		return result;
	}

	@Override
	public List<BookingSpecialtyDto> fetchBookingSpecialtiesByProfessional(Integer healthcareProfessionalId) {
		List<BookingSpecialtyBo> practices = fetchSpecialtiesByProfessional.run(healthcareProfessionalId);
		List<BookingSpecialtyDto> result = practices.stream()
				.map(b -> new BookingSpecialtyDto(b.getId(), b.getDescription()))
				.collect(Collectors.toList());
		log.debug("Get all practices for professional {} => {}", healthcareProfessionalId, result);
		return result;
	}

	@Override
	public List<BookingProfessionalDto> fetchBookingProfessionals(Integer institutionId, Integer medicalCoverageId, boolean all) {
		var professionalsBo = fetchBookingProfessionals.run(institutionId,medicalCoverageId, all);
		var result = professionalsBo.stream()
				.map((BookingProfessionalBo t) -> new BookingProfessionalDto(t.getId(), t.getName(), t.getCoverage()))
				.collect(Collectors.toList());
		log.debug("Get all booking institutions => {}", result);
		return result;
	}

	@Override
	public ProfessionalAvailabilityDto fetchAvailabilityByPracticeAndProfessional(
		Integer institutionId,
		Integer professionalId,
		Integer clinicalSpecialtyId,
		Integer practiceId
	) {
		var professionalAvailabilityBo = fetchAvailabilityByPracticeAndProfessional.run(
				institutionId,
				professionalId,
				clinicalSpecialtyId,
				practiceId
		);
		var result = buildProfessionalAvailabilityDto(professionalAvailabilityBo);
		log.debug("Get availability by professionalId {} and practiceId{} => {}", professionalId, practiceId, result);
		return result;
	}

	@Override
	public ProfessionalAvailabilityDto fetchAvailabilityByPracticeAndProfessional(
			Integer institutionId,
			Integer professionalId,
			Integer clinicalSpecialtyId,
			Integer practiceId,
			Integer medicalCoverageId,
			String maxDate
	) {
		var professionalAvailabilityBo = fetchAvailabilityByPracticeAndProfessional.run(
				institutionId,
				professionalId,
				clinicalSpecialtyId,
				practiceId
		);
		var result = buildProfessionalAvailabilityDto(professionalAvailabilityBo);
		log.debug("Get availability by professionalId {} and practiceId{} => {}", professionalId, practiceId, result);
		return result;
	}

	private ProfessionalAvailabilityDto buildProfessionalAvailabilityDto(ProfessionalAvailabilityBo professionalAvailabilityBo) {
		if(professionalAvailabilityBo == null || professionalAvailabilityBo.getAvailability() == null)
			return null;
		return new ProfessionalAvailabilityDto(
				professionalAvailabilityBo.getAvailability().stream()
						.map(this::buildDiaryAvailabilityDto)
						.collect(Collectors.toList()),
				buildBookingProfessionalDto(professionalAvailabilityBo.getProfessional()));
	}

	private DiaryAvailabilityDto buildDiaryAvailabilityDto(DiaryAvailabilityBo diaryAvailabilityBo){
		return new DiaryAvailabilityDto(buildBookingDiaryDto(diaryAvailabilityBo.getDiary()),
				buildAvailabilityDto(diaryAvailabilityBo.getSlots()));
	}
	private AvailabilityDto buildAvailabilityDto(AvailabilityBo slots) {
		return new AvailabilityDto(slots.getDate(), slots.getSlots());
	}
	private BookingDiaryDto buildBookingDiaryDto(DiaryListBo diary) {
		return new BookingDiaryDto(diary.getId(), diary.getDoctorsOfficeId(), diary.getDoctorsOfficeDescription(),
			diary.getStartDate(), diary.getEndDate(), diary.getAppointmentDuration(), diary.getFrom(), diary.getTo(),
				diary.getOpeningHoursId());
	}

	private BookingProfessionalDto buildBookingProfessionalDto(BookingProfessionalBo professional){
		return new BookingProfessionalDto(professional.getId(), professional.getName(), professional.getCoverage());
	}

	@Override
	public List<ProfessionalAvailabilityDto> fetchAvailabilityByPractice(Integer institutionId, Integer clinicalSpecialtyId, Integer practiceId, Integer medicalCoverageId) {
		var professionalAvailabilitiesBo = fetchAvailabilityByPractice.run(institutionId,
				clinicalSpecialtyId, practiceId, medicalCoverageId);
		var result = professionalAvailabilitiesBo.stream()
				.map(this::buildProfessionalAvailabilityDto)
				.collect(Collectors.toList());
		log.debug("Get availability by practiceId{} => {}", practiceId, result);
		return result;
	}

	@Override
	public boolean checkMailExists(String email) {
		log.debug("Input {} =>", email);
		boolean exists = checkIfMailExists.run(email);
		log.debug("Output {} =>", exists);
		return exists;
	}

	private static BookingAppointmentBo mapToAppointment(BookingAppointmentDto bookingAppointmentDto) {
		return BookingAppointmentBo.builder()
				.diaryId(bookingAppointmentDto.getDiaryId())
				.day(bookingAppointmentDto.getDay())
				.hour(bookingAppointmentDto.getHour())
				.openingHoursId(bookingAppointmentDto.getOpeningHoursId())
				.phoneNumber(bookingAppointmentDto.getPhoneNumber())
				.phonePrefix(bookingAppointmentDto.getPhonePrefix())
				.coverageId(bookingAppointmentDto.getCoverageId())
				.snomedId(bookingAppointmentDto.getSnomedId())
				.specialtyId(bookingAppointmentDto.getSpecialtyId())
				.build();
	}

	private static BookingPersonBo mapToPerson(BookingPersonDto bookingPersonDto) {
		if(bookingPersonDto == null) {
			return null;
		}
		return BookingPersonBo.builder()
				.email(bookingPersonDto.getEmail())
				.firstName(bookingPersonDto.getFirstName())
				.lastName(bookingPersonDto.getLastName())
				.idNumber(bookingPersonDto.getIdNumber())
				.genderId(bookingPersonDto.getGenderId())
				.birthDate(bookingPersonDto.getBirthDate())
				.phoneNumber(bookingPersonDto.getPhoneNumber())
				.phonePrefix(bookingPersonDto.getPhonePrefix())
				.build();

	}
}
