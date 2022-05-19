package ar.lamansys.online.infraestructure.input.rest.professional;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.online.infraestructure.input.service.BookingExternalService;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingProfessionalDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.ProfessionalAvailabilityDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/booking/professional")
@Tag(name = "Booking", description = "Booking by professional")
public class BookingProfessionalController {

	private final BookingExternalService bookingExternalService;

	public BookingProfessionalController(BookingExternalService bookingExternalService) {
		this.bookingExternalService = bookingExternalService;
	}


	@GetMapping("/institution/{institutionId}/healthinsurance/{healthInsuranceId}")
    public ResponseEntity<List<BookingProfessionalDto>> getAllBookingProfessionals(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "healthInsuranceId") Integer medicalCoverageId,
            @RequestParam(name = "all", required = false, defaultValue = "true") boolean all
    ) {
        var result = bookingExternalService.fetchBookingProfessionals(institutionId,medicalCoverageId, all);
        log.debug("Get all booking institutions => {}", result);
        return ResponseEntity.ok(result);
    }

    @GetMapping("{professionalId}/institution/{institutionId}/specialty/{clinicalSpecialtyId}/practice/{practiceId}/availability")
    public ResponseEntity<ProfessionalAvailabilityDto> getProfessionalAvailability(
            @PathVariable(name="institutionId") Integer institutionId,
            @PathVariable(name="professionalId") Integer professionalId,
            @PathVariable(name="clinicalSpecialtyId") Integer clinicalSpecialtyId,
            @PathVariable(name="practiceId") Integer practiceId
    ) {
        var result = bookingExternalService.fetchAvailabilityByPracticeAndProfessional(
				institutionId,
				professionalId,
				clinicalSpecialtyId,
				practiceId
		);
        log.debug("Get availability by professionalId {} and practiceId{} => {}", professionalId, practiceId, result);
        return ResponseEntity.ok(result);
    }

    @GetMapping("institution/{institutionId}/specialty/{clinicalSpecialtyId}/practice/{practiceId}/healthinsurance/{healthInsuranceId}/availability")
    public ResponseEntity<List<ProfessionalAvailabilityDto>> getProfessionalsAvailability(
            @PathVariable(name="healthInsuranceId") Integer medicalCoverageId,
            @PathVariable(name="practiceId") Integer practiceId,
            @PathVariable(name="clinicalSpecialtyId") Integer clinicalSpecialtyId,
            @PathVariable(name="institutionId") Integer institutionId
    ) {
        var result = bookingExternalService.fetchAvailabilityByPractice(institutionId,
				clinicalSpecialtyId, practiceId, medicalCoverageId);
        log.debug("Get availability by practiceId{} => {}", practiceId, result);
        return ResponseEntity.ok(result);
    }

}
