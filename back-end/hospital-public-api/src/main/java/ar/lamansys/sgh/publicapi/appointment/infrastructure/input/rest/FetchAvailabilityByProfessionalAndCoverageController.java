package ar.lamansys.sgh.publicapi.appointment.infrastructure.input.rest;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.publicapi.appointment.application.fetchavailabilitybyprofessionalandcoverage.FetchAvailabilityByProfessionalAndCoverage;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.ProfessionalAvailabilityDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Tag(name = "PublicApi Turnos", description = "Availability by Professional and Clinical Specialty and Coverage")
@RequestMapping("/public-api/institution/{institutionId}/appointment/booking/professional")
@RestController
public class FetchAvailabilityByProfessionalAndCoverageController {

	private final FetchAvailabilityByProfessionalAndCoverage fetchAvailabilityByProfessionalAndCoverage;

	@GetMapping("/{professionalId}/specialty/{clinicalSpecialtyId}/practice/{practiceId}/coverage/{coverageId}/availability")
	public ResponseEntity<ProfessionalAvailabilityDto> getProfessionalAvailability(
			@PathVariable(name="institutionId") Integer institutionId,
			@PathVariable(name="professionalId") Integer professionalId,
			@PathVariable(name="clinicalSpecialtyId") Integer clinicalSpecialtyId,
			@PathVariable(name="practiceId") Integer practiceId,
			@PathVariable(name="coverageId") Integer coverageId,
			@RequestParam(name="maxDate") String maxDateStr
	) {


		var result = fetchAvailabilityByProfessionalAndCoverage.run(
				institutionId,
				professionalId,
				clinicalSpecialtyId,
				practiceId,
				coverageId,
				maxDateStr
		);
		log.debug("Get availability by professionalId {} and clinicalSpecialtyId{} and practiceId{} and coverageId{} => {}",
				professionalId, clinicalSpecialtyId, practiceId, coverageId, result);
		return ResponseEntity.ok(result);
	}
}
