package ar.lamansys.sgh.publicapi.appointment.infrastructure.input.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ar.lamansys.sgh.publicapi.appointment.application.FetchAvailabilityByProfessional;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.ProfessionalAvailabilityDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RequestMapping("/public-api/institution/{institutionId}/appointment/booking/professional")
@Controller
public class FetchAvailabilityByProfessionalController {

	private final FetchAvailabilityByProfessional fetchAvailabilityByProfessional;

	@GetMapping("/{professionalId}/specialty/{clinicalSpecialtyId}/practice/{practiceId}/availability")
	public ResponseEntity<ProfessionalAvailabilityDto> getProfessionalAvailability(
			@PathVariable(name="institutionId") Integer institutionId,
			@PathVariable(name="professionalId") Integer professionalId,
			@PathVariable(name="clinicalSpecialtyId") Integer clinicalSpecialtyId,
			@PathVariable(name="practiceId") Integer practiceId
	) {
		var result = fetchAvailabilityByProfessional.run(institutionId, professionalId, clinicalSpecialtyId, practiceId);
		log.debug("Get availability by clinical specialty id {} and practice id {} => {}", clinicalSpecialtyId, practiceId, result);
		return ResponseEntity.ok(result);
	}

}