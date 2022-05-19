package ar.lamansys.online.infraestructure.input.rest.specialty;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.online.infraestructure.input.service.BookingExternalService;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingSpecialtyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.PracticeDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/booking")
@Tag(name = "Booking", description = "Booking practices")
public class BookingSpecialtyController {

    private final BookingExternalService bookingExternalService;

	public BookingSpecialtyController(BookingExternalService bookingExternalService) {
		this.bookingExternalService = bookingExternalService;
	}


	@GetMapping("/specialty/{clinicalSpecialtyId}/practices/healthinsurance/{healthInsuranceId}")
    public ResponseEntity<List<PracticeDto>> getAllPracticesBySpecialtyAndHealthInsurance(
            @PathVariable(name = "clinicalSpecialtyId") Integer clinicalSpecialtyId,
            @PathVariable(name = "healthInsuranceId") Integer medicalCoverageId,
            @RequestParam(name = "all", required = false, defaultValue = "true") boolean all
    ) {
        List<PracticeDto> result = bookingExternalService.fetchPracticesBySpecialtyAndHealthInsurance(clinicalSpecialtyId, medicalCoverageId, all);
        log.debug("Get all practices by Clinical Specialty {} and by HealthInsurance {} => {}",
                clinicalSpecialtyId, medicalCoverageId,
                result);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/practices/professional/{healthcareProfessionalId}/healthinsurance/{healthInsuranceId}/specialty/{clinicalSpecialtyId}")
    public ResponseEntity<List<PracticeDto>> getAllPracticesByProfessionalAndHealthInsurance(
            @PathVariable(name = "healthcareProfessionalId") Integer healthcareProfessionalId,
            @PathVariable(name = "healthInsuranceId") Integer medicalCoverageId,
            @PathVariable(name = "clinicalSpecialtyId") Integer clinicalSpecialtyId,
            @RequestParam(name = "all", required = false, defaultValue = "true") boolean all) {
        List<PracticeDto> result = bookingExternalService.fetchPracticesByProfessionalAndHealthInsurance(healthcareProfessionalId, medicalCoverageId, clinicalSpecialtyId, all);
		log.debug("Get all practices by healthcareProfessionalId {} and by HealthInsurance {} and by clinical specialty {} => {}", healthcareProfessionalId, medicalCoverageId, clinicalSpecialtyId,
                result);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/specialties")
    public ResponseEntity<List<BookingSpecialtyDto>> getAllSpecialties() {
        List<BookingSpecialtyDto> result = bookingExternalService.fetchSpecialties();
        log.debug("Get all practices => {}", result);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/specialties/professional/{healthcareProfessionalId}")
    public ResponseEntity<List<BookingSpecialtyDto>> getSpecialtiesByProfessional(
            @PathVariable(name = "healthcareProfessionalId") Integer healthcareProfessionalId
    ) {
        List<BookingSpecialtyDto> result = bookingExternalService.fetchSpecialtiesByProfessional(healthcareProfessionalId);
        log.debug("Get all practices for professional {} => {}", healthcareProfessionalId, result);
        return ResponseEntity.ok(result);
    }
}
