package ar.lamansys.sgh.publicapi.infrastructure.input.rest.appointment;


import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.SharedAppointmentPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.PublicAppointmentListDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingHealthInsuranceDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingInstitutionDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingSpecialtyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.PracticeDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SharedBookingPort;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/public-api/institution/{sisaCode}/appointment/booking")
public class BookingPublicController {
	private final SharedBookingPort bookAppointmentPort;
	private final SharedAppointmentPort appointmentPort;
	private final LocalDateMapper localDateMapper;

	public BookingPublicController(SharedBookingPort bookAppointmentPort,
								   SharedAppointmentPort appointmentPort,
								   LocalDateMapper localDateMapper) {
		this.bookAppointmentPort = bookAppointmentPort;
		this.appointmentPort = appointmentPort;
		this.localDateMapper = localDateMapper;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public String bookPreappointment(@RequestBody BookingDto bookingDto) {
		return bookAppointmentPort.makeBooking(bookingDto);
	}


	@PutMapping("/cancel")
	public void cancelBooking(@RequestBody String uuid) {
		bookAppointmentPort.cancelBooking(uuid);
		log.debug("cancel booking {}", uuid);
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public Collection<PublicAppointmentListDto> getBookingList(
			@PathVariable(name = "sisaCode") String sisaCode,
			@RequestParam(name = "identificationNumber", required = false) String identificationNumber,
			@RequestParam(name = "startDate", required = false) String startDateStr,
			@RequestParam(name = "endDate", required = false) String endDateStr
	) {
		LocalDate startDate = localDateMapper.fromStringToLocalDate(startDateStr);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(endDateStr);
		return appointmentPort.fetchAppointments(sisaCode, identificationNumber, List.of((short)6), startDate, endDate);
	}

	@GetMapping("/institution")
	public List<BookingInstitutionDto> getAllBookingInstitutions() {
		return bookAppointmentPort.fetchAllBookingInstitutions();
	}

	@GetMapping("/medicalCoverages")
	public ResponseEntity<List<BookingHealthInsuranceDto>> fetchAllMedicalCoverages() {
		List<BookingHealthInsuranceDto> result = bookAppointmentPort.fetchAllMedicalCoverages();
		log.debug("Get all booking institutions => {}", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/specialty/{clinicalSpecialtyId}/practices/medicalCoverages/{medicalCoverageId}")
	public ResponseEntity<List<PracticeDto>> getAllPracticesBySpecialtyAndMedicalCoverage(
			@PathVariable(name = "clinicalSpecialtyId") Integer clinicalSpecialtyId,
			@PathVariable(name = "medicalCoverageId") Integer medicalCoverageId,
			@RequestParam(name = "all", required = false, defaultValue = "true") boolean all
	) {
		List<PracticeDto> result =  bookAppointmentPort.fetchPracticesBySpecialtyAndHealthInsurance(clinicalSpecialtyId, medicalCoverageId, all);
		log.debug("Get all practices by Clinical Specialty {} and by HealthInsurance {} => {}",
				clinicalSpecialtyId, medicalCoverageId,
				result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/practices/professional/{healthcareProfessionalId}/medicalCoverages/{medicalCoverageId}/specialty/{clinicalSpecialtyId}")
	public ResponseEntity<List<PracticeDto>> getAllPracticesByProfessionalAndMedicalCoverage(
			@PathVariable(name = "healthcareProfessionalId") Integer healthcareProfessionalId,
			@PathVariable(name = "medicalCoverageId") Integer medicalCoverageId,
			@PathVariable(name = "clinicalSpecialtyId") Integer clinicalSpecialtyId,
			@RequestParam(name = "all", required = false, defaultValue = "true") boolean all) {
		List<PracticeDto> result = bookAppointmentPort.fetchPracticesByProfessionalAndHealthInsurance(healthcareProfessionalId, medicalCoverageId, clinicalSpecialtyId, all);
		log.debug("Get all practices by healthcareProfessionalId {} and by HealthInsurance {} and by clinical specialty {} => {}", healthcareProfessionalId, medicalCoverageId, clinicalSpecialtyId,
				result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/specialties")
	public ResponseEntity<List<BookingSpecialtyDto>> getAllSpecialties() {
		List<BookingSpecialtyDto> result = bookAppointmentPort.fetchSpecialties();
		log.debug("Get all practices => {}", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/specialties/professional/{healthcareProfessionalId}")
	public ResponseEntity<List<BookingSpecialtyDto>> getSpecialtiesByProfessional(
			@PathVariable(name = "healthcareProfessionalId") Integer healthcareProfessionalId
	) {
		List<BookingSpecialtyDto> result = bookAppointmentPort.fetchSpecialtiesByProfessional(healthcareProfessionalId);
		log.debug("Get all practices for professional {} => {}", healthcareProfessionalId, result);
		return ResponseEntity.ok(result);
	}
}
