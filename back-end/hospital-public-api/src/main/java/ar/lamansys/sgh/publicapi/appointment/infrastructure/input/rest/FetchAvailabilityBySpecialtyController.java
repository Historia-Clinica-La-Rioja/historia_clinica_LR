package ar.lamansys.sgh.publicapi.appointment.infrastructure.input.rest;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ar.lamansys.sgh.publicapi.appointment.application.FetchAvailabilityBySpecialty;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.ProfessionalAvailabilityDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RequestMapping("/public-api/institution/{institutionId}/appointment/booking")
@Controller
public class FetchAvailabilityBySpecialtyController {

	private final FetchAvailabilityBySpecialty fetchAvailabilityBySpecialty;

	@GetMapping("/specialty/{clinicalSpecialtyId}/practice/{practiceId}/medicalCoverages/{medicalCoverageId}/availability")
	public ResponseEntity<List<ProfessionalAvailabilityDto>> getProfessionalsAvailability(
			@PathVariable(name="medicalCoverageId") Integer medicalCoverageId,
			@PathVariable(name="practiceId") Integer practiceId,
			@PathVariable(name="clinicalSpecialtyId") Integer clinicalSpecialtyId,
			@PathVariable(name="institutionId") Integer institutionId
	) {
		var result = fetchAvailabilityBySpecialty.run(institutionId, clinicalSpecialtyId, practiceId, medicalCoverageId);
		log.debug("Get availability by clinical specialty id {} and practice id {} => {}", clinicalSpecialtyId, practiceId, result);
		return ResponseEntity.ok(result);
	}
}