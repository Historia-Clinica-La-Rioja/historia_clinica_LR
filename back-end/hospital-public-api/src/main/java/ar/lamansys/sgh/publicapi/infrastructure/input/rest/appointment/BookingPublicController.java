package ar.lamansys.sgh.publicapi.infrastructure.input.rest.appointment;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingHealthInsuranceDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingInstitutionDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingSpecialtyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.PracticeDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SharedBookingPort;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/public-api/appointment/booking")
@Tag(name = "Public Api", description = "Bookings")
public class BookingPublicController {
	private final SharedBookingPort bookAppointmentPort;
	public BookingPublicController(SharedBookingPort bookAppointmentPort) {
		this.bookAppointmentPort = bookAppointmentPort;
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

	@GetMapping("/specialties")
	public ResponseEntity<List<BookingSpecialtyDto>> getAllSpecialties() {
		List<BookingSpecialtyDto> result = bookAppointmentPort.fetchSpecialties();
		log.debug("Get all practices => {}", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/specialty/{clinicalSpecialtyId}/medicalCoverages/{medicalCoverageId}/practices")
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

	@GetMapping("/professional/{healthcareProfessionalId}/specialty/{clinicalSpecialtyId}/medicalCoverages/{medicalCoverageId}/practices")
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

	@GetMapping("/professional/{healthcareProfessionalId}/specialties")
	public ResponseEntity<List<BookingSpecialtyDto>> getSpecialtiesByProfessional(
			@PathVariable(name = "healthcareProfessionalId") Integer healthcareProfessionalId
	) {
		List<BookingSpecialtyDto> result = bookAppointmentPort.fetchSpecialtiesByProfessional(healthcareProfessionalId);
		log.debug("Get all practices for professional {} => {}", healthcareProfessionalId, result);
		return ResponseEntity.ok(result);
	}
}
