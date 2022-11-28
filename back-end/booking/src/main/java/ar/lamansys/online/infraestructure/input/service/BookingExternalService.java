package ar.lamansys.online.infraestructure.input.service;

import java.util.List;
import java.util.stream.Collectors;

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
	private final FetchHealthcareInsurances fetchHealthcareInsurances;
	private final FetchPracticesBySpecialtyAndHealthInsurance fetchPracticesBySpecialtyAndHealthInsurance;
	private final FetchPracticesByProfessionalAndHealthInsurance fetchPracticesByProfessionalAndHealthInsurance;
	private final FetchSpecialties fetchSpecialties;
	private final FetchSpecialtiesByProfessional fetchSpecialtiesByProfessional;
	private final FetchBookingProfessionals fetchBookingProfessionals;
	private final FetchAvailabilityByPracticeAndProfessional fetchAvailabilityByPracticeAndProfessional;
	private final FetchAvailabilityByPractice fetchAvailabilityByPractice;

	public String makeBooking(BookingDto bookingDto) {
		BookingBo bookingBo = new BookingBo(
				bookingDto.getAppointmentDataEmail(),
				mapToAppointment(bookingDto.getBookingAppointmentDto()),
				mapToPerson(bookingDto.getBookingPersonDto())
		);
		return bookAppointment.run(bookingBo);
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
	public List<BookingHealthInsuranceDto> fetchAllMedicalCoverages() {
		List<BookingHealthInsuranceDto> result = fetchHealthcareInsurances.run().stream()
				.map(insurance -> new BookingHealthInsuranceDto(insurance.getId(), insurance.getName()))
				.collect(Collectors.toList());
		log.debug("Get all booking institutions => {}", result);
		return result;
	}

	@Override
	public List<PracticeDto> fetchPracticesBySpecialtyAndHealthInsurance(Integer clinicalSpecialtyId, Integer medicalCoverageId, boolean all) {
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
	public List<PracticeDto> fetchPracticesByProfessionalAndHealthInsurance(Integer healthcareProfessionalId, Integer medicalCoverageId, Integer clinicalSpecialtyId, boolean all) {
		var practiceBo= fetchPracticesByProfessionalAndHealthInsurance.run(healthcareProfessionalId, medicalCoverageId, clinicalSpecialtyId, all);
		List<PracticeDto> result = practiceBo.stream()
				.map((PracticeBo t) -> new PracticeDto(t.getId(), t.getDescription(), t.getCoverage(), t.getCoverageText(), t.getSnomedId()))
				.collect(Collectors.toList());
		log.debug("Get all practices by healthcareProfessionalId {} and by HealthInsurance {} and by clinical specialty {} => {}", healthcareProfessionalId, medicalCoverageId, clinicalSpecialtyId,
				result);
		return result;
	}

	@Override
	public List<BookingSpecialtyDto> fetchSpecialties() {
		List<BookingSpecialtyBo> practices = fetchSpecialties.run();
		List<BookingSpecialtyDto> result = practices.stream()
				.map(b -> new BookingSpecialtyDto(b.getId(), b.getDescription()))
				.collect(Collectors.toList());
		log.debug("Get all practices => {}", result);
		return result;
	}

	@Override
	public List<BookingSpecialtyDto> fetchSpecialtiesByProfessional(Integer healthcareProfessionalId) {
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

	private ProfessionalAvailabilityDto buildProfessionalAvailabilityDto(ProfessionalAvailabilityBo professionalAvailabilityBo) {
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

	private static BookingAppointmentBo mapToAppointment(BookingAppointmentDto bookingAppointmentDto) {
		return BookingAppointmentBo.builder()
				.diaryId(bookingAppointmentDto.getDiaryId())
				.day(bookingAppointmentDto.getDay())
				.hour(bookingAppointmentDto.getHour())
				.openingHoursId(bookingAppointmentDto.getOpeningHoursId())
				.phoneNumber(bookingAppointmentDto.getPhoneNumber())
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
				.build();

	}
}
