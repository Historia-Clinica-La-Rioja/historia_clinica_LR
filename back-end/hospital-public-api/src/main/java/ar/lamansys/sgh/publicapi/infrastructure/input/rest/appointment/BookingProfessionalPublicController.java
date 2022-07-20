package ar.lamansys.sgh.publicapi.infrastructure.input.rest.appointment;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingProfessionalDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.ProfessionalAvailabilityDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SharedBookingPort;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/public-api/institution/{institutionId}/appointment/booking/professional")
@Tag(name = "Public Api", description = "Booking by professional")
public class BookingProfessionalPublicController {

	private final SharedBookingPort bookAppointmentPort;

	public BookingProfessionalPublicController(SharedBookingPort bookAppointmentPort) {
		this.bookAppointmentPort = bookAppointmentPort;
	}


	@GetMapping("/medicalCoverages/{medicalCoverageId}")
    public ResponseEntity<List<BookingProfessionalDto>> getAllBookingProfessionals(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "medicalCoverageId") Integer medicalCoverageId,
            @RequestParam(name = "all", required = false, defaultValue = "true") boolean all
    ) {
        var result = bookAppointmentPort.fetchBookingProfessionals(institutionId,medicalCoverageId, all);
        log.debug("Get all booking institutions => {}", result);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{professionalId}/specialty/{clinicalSpecialtyId}/practice/{practiceId}/availability")
    public ResponseEntity<ProfessionalAvailabilityDto> getProfessionalAvailability(
            @PathVariable(name="institutionId") Integer institutionId,
            @PathVariable(name="professionalId") Integer professionalId,
            @PathVariable(name="clinicalSpecialtyId") Integer clinicalSpecialtyId,
            @PathVariable(name="practiceId") Integer practiceId
    ) {
        var result = bookAppointmentPort.fetchAvailabilityByPracticeAndProfessional(
				institutionId,
				professionalId,
				clinicalSpecialtyId,
				practiceId
		);
        log.debug("Get availability by professionalId {} and practiceId{} => {}", professionalId, practiceId, result);
        return ResponseEntity.ok(result);
    }



}
