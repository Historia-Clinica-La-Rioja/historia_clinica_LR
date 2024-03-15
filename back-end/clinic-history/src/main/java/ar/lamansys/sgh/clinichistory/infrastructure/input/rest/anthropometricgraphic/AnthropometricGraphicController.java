package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.anthropometricgraphic;

import ar.lamansys.sgh.clinichistory.application.canshowanthropometricgraphic.CanShowAnthropometricGraphic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/institution/{institutionId}/percentiles")
@RestController
public class AnthropometricGraphicController {

	private final CanShowAnthropometricGraphic canShowAnthropometricGraphic;

	@GetMapping("/patient/{patientId}/can-show-graphic")
	@ResponseStatus(HttpStatus.OK)
	public Boolean canShowAnthropometricGraphic (@PathVariable("institutionId")Integer institutionId,
											  @PathVariable("patientId")Integer patientId)
	{
		log.debug("Input parameters -> patientId {}", patientId);
		Boolean result = canShowAnthropometricGraphic.run(patientId);
		log.debug("Output -> result {}", result);
		return result;
	}

}
