package ar.lamansys.sgh.publicapi.appointment.infrastructure.input.rest;


import java.util.List;

import ar.lamansys.sgh.publicapi.appointment.application.fetchBookingPracticesBySpecialtyAndHealthInsurance.FetchBookingPracticesBySpecialtyAndHealthInsurance;
import ar.lamansys.sgh.publicapi.appointment.application.fetchallbookinginstitutions.FetchAllBookingInstitutions;
import ar.lamansys.sgh.publicapi.appointment.application.fetchallbookinginstitutionsextended.FetchAllBookingInstitutionsExtended;
import ar.lamansys.sgh.publicapi.appointment.application.fetchbookingpracticesbyprofessionalandhealthinsurance.FetchBookingPracticesByProfessionalAndHealthInsurance;
import ar.lamansys.sgh.publicapi.appointment.application.fetchbookingspecialties.FetchBookingSpecialties;
import ar.lamansys.sgh.publicapi.appointment.application.fetchbookingspecialtiesbyprofessional.FetchBookingSpecialtiesByProfessional;
import ar.lamansys.sgh.publicapi.appointment.application.fetchmedicalcoverages.FetchMedicalCoverages;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingInstitutionExtendedDto;

import lombok.AllArgsConstructor;

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

@AllArgsConstructor
@Slf4j
@RequestMapping("/public-api/appointment/booking")
@Tag(name = "PublicApi Turnos", description = "Bookings")
@RestController
public class BookingPublicController {
	private final FetchAllBookingInstitutions fetchAllBookingInstitutions;
	private final FetchAllBookingInstitutionsExtended fetchAllBookingInstitutionsExtended;
	private final FetchMedicalCoverages fetchMedicalCoverages;
	private final FetchBookingSpecialties fetchBookingSpecialties;
	private final FetchBookingPracticesBySpecialtyAndHealthInsurance fetchBookingPracticesBySpecialtyAndHealthInsurance;
	private final FetchBookingPracticesByProfessionalAndHealthInsurance fetchBookingPracticesByProfessionalAndHealthInsurance;
	private final FetchBookingSpecialtiesByProfessional fetchBookingSpecialtiesByProfessional;

	@GetMapping("/institution")
	public List<BookingInstitutionDto> getAllBookingInstitutions() {
		return fetchAllBookingInstitutions.run();
	}

	@GetMapping("/institutionExtended")
	public List<BookingInstitutionExtendedDto> getAllBookingInstitutionsExtended() {
		return fetchAllBookingInstitutionsExtended.run();
	}

	@GetMapping("/medicalCoverages")
	public ResponseEntity<List<BookingHealthInsuranceDto>> fetchMedicalCoverages() {
		List<BookingHealthInsuranceDto> result = fetchMedicalCoverages.run();
		log.debug("Get all booking institutions => {}", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/specialties")
	public ResponseEntity<List<BookingSpecialtyDto>> getAllSpecialties() {
		List<BookingSpecialtyDto> result = fetchBookingSpecialties.run();
		log.debug("Get all practices => {}", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/specialty/{clinicalSpecialtyId}/medicalCoverages/{medicalCoverageId}/practices")
	public ResponseEntity<List<PracticeDto>> getAllPracticesBySpecialtyAndMedicalCoverage(
			@PathVariable(name = "clinicalSpecialtyId") Integer clinicalSpecialtyId,
			@PathVariable(name = "medicalCoverageId") Integer medicalCoverageId,
			@RequestParam(name = "all", required = false, defaultValue = "true") boolean all
	) {
		List<PracticeDto> result = fetchBookingPracticesBySpecialtyAndHealthInsurance.run(clinicalSpecialtyId, medicalCoverageId, all);
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
		List<PracticeDto> result = fetchBookingPracticesByProfessionalAndHealthInsurance.run(healthcareProfessionalId, medicalCoverageId, clinicalSpecialtyId, all);
		log.debug("Get all practices by healthcareProfessionalId {} and by HealthInsurance {} and by clinical specialty {} => {}", healthcareProfessionalId, medicalCoverageId, clinicalSpecialtyId,
				result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/professional/{healthcareProfessionalId}/specialties")
	public ResponseEntity<List<BookingSpecialtyDto>> getSpecialtiesByProfessional(
			@PathVariable(name = "healthcareProfessionalId") Integer healthcareProfessionalId
	) {
		List<BookingSpecialtyDto> result = fetchBookingSpecialtiesByProfessional.run(healthcareProfessionalId);
		log.debug("Get all practices for professional {} => {}", healthcareProfessionalId, result);
		return ResponseEntity.ok(result);
	}
}
