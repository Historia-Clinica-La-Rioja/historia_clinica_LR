package ar.lamansys.sgh.publicapi.appointment.infrastructure.input.rest;

import java.util.List;

import ar.lamansys.sgh.publicapi.appointment.application.fetchBookingProfessionals.FetchBookingProfessionalsByInstitution;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingProfessionalDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@RequestMapping("/public-api/institution/{institutionId}/appointment/booking/professional/medicalCoverages/{medicalCoverageId}")
@Tag(name = "PublicApi Turnos", description = "Booking by professional")
@RestController
public class BookingProfessionalPublicController {

	private final FetchBookingProfessionalsByInstitution fetchBookingProfessionalsByInstitution;

	@GetMapping()
    public ResponseEntity<List<BookingProfessionalDto>> getAllBookingProfessionals(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "medicalCoverageId") Integer medicalCoverageId,
            @RequestParam(name = "all", required = false, defaultValue = "true") boolean all
    ) {
		var result = fetchBookingProfessionalsByInstitution.run(institutionId,medicalCoverageId,all);
        log.debug("Get all booking institutions => {}", result);
        return ResponseEntity.ok(result);
    }

}
