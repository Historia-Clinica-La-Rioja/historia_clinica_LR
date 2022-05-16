package ar.lamansys.online.infraestructure.input.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ar.lamansys.online.application.booking.BookAppointment;
import ar.lamansys.online.application.booking.CancelBooking;
import ar.lamansys.online.application.booking.CheckIfMailExists;
import ar.lamansys.online.application.insurance.FetchHealthcareInsurances;
import ar.lamansys.online.application.integration.FetchBookingInstitutions;
import ar.lamansys.online.application.specialty.FetchPracticesByProfessionalAndHealthInsurance;
import ar.lamansys.online.application.specialty.FetchPracticesBySpecialtyAndHealthInsurance;
import ar.lamansys.online.application.specialty.FetchSpecialties;
import ar.lamansys.online.application.specialty.FetchSpecialtiesByProfessional;
import ar.lamansys.online.domain.booking.BookingAppointmentBo;
import ar.lamansys.online.domain.booking.BookingBo;
import ar.lamansys.online.domain.booking.BookingPersonBo;
import ar.lamansys.online.domain.specialty.BookingSpecialtyBo;
import ar.lamansys.online.domain.specialty.PracticeBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingAppointmentDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingHealthInsuranceDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingInstitutionDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingPersonDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingSpecialtyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.PracticeDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SharedBookingPort;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class BookingExternalService implements SharedBookingPort {
	private final BookAppointment bookAppointment;
	private final CancelBooking cancelBooking;
	private final FetchBookingInstitutions fetchBookingInstitutions;
	private final FetchHealthcareInsurances fetchHealthcareInsurances;

	private final FetchPracticesBySpecialtyAndHealthInsurance fetchPracticesBySpecialtyAndHealthInsurance;

	private final FetchPracticesByProfessionalAndHealthInsurance fetchPracticesByProfessionalAndHealthInsurance;

	private final FetchSpecialties fetchSpecialties;

	private final FetchSpecialtiesByProfessional fetchSpecialtiesByProfessional;

	public BookingExternalService(
			BookAppointment bookAppointment,
			CheckIfMailExists checkIfMailExists,
			CancelBooking cancelBooking,
			FetchBookingInstitutions fetchBookingInstitutions,
			FetchHealthcareInsurances fetchHealthcareInsurances,
			FetchPracticesBySpecialtyAndHealthInsurance fetchPracticesBySpecialtyAndHealthInsurance,
			FetchPracticesByProfessionalAndHealthInsurance fetchPracticesByProfessionalAndHealthInsurance,
			FetchSpecialties fetchSpecialties, FetchSpecialtiesByProfessional fetchSpecialtiesByProfessional) {
		this.bookAppointment = bookAppointment;
		this.cancelBooking = cancelBooking;
		this.fetchBookingInstitutions = fetchBookingInstitutions;
		this.fetchHealthcareInsurances = fetchHealthcareInsurances;
		this.fetchPracticesBySpecialtyAndHealthInsurance = fetchPracticesBySpecialtyAndHealthInsurance;
		this.fetchPracticesByProfessionalAndHealthInsurance = fetchPracticesByProfessionalAndHealthInsurance;
		this.fetchSpecialties = fetchSpecialties;
		this.fetchSpecialtiesByProfessional = fetchSpecialtiesByProfessional;
	}

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

	private static BookingAppointmentBo mapToAppointment(BookingAppointmentDto bookingAppointmentDto) {
		return new BookingAppointmentBo(
				bookingAppointmentDto.getDiaryId(),
				bookingAppointmentDto.getDay(),
				bookingAppointmentDto.getHour(),
				bookingAppointmentDto.getOpeningHoursId(),
				bookingAppointmentDto.getPhoneNumber(),
				bookingAppointmentDto.getCoverageId(),
				bookingAppointmentDto.getSnomedId(),
				bookingAppointmentDto.getSpecialtyId()
		);
	}

	private static BookingPersonBo mapToPerson(BookingPersonDto bookingPersonDto) {
		if(bookingPersonDto == null) {
			return null;
		}
		return new BookingPersonBo(
				bookingPersonDto.getEmail(),
				bookingPersonDto.getFirstName(),
				bookingPersonDto.getLastName(),
				bookingPersonDto.getIdNumber(),
				bookingPersonDto.getGenderId(),
				bookingPersonDto.getBirthDate()
		);
	}
}
