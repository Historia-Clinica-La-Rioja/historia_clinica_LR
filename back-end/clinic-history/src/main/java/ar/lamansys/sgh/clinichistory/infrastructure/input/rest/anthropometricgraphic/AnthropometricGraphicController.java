package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.anthropometricgraphic;

import ar.lamansys.sgh.clinichistory.application.canshowanthropometricgraphic.CanShowAnthropometricGraphic;
import ar.lamansys.sgh.clinichistory.application.getavailableanthropometricgraphictypes.GetAvailableAnthropometricGraphicTypes;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.EAnthropometricGraphic;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.EAnthropometricGraphicType;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.percentiles.dto.EAnthropometricGraphicOption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/institution/{institutionId}/percentiles")
@RestController
public class AnthropometricGraphicController {

	private final CanShowAnthropometricGraphic canShowAnthropometricGraphic;
	private final GetAvailableAnthropometricGraphicTypes getAvailableAnthropometricGraphicTypes;

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

	@GetMapping("/patient/{patientId}/available-graphics")
	@ResponseStatus(HttpStatus.OK)
	public List<EAnthropometricGraphicType> getAvailableAnthropometricGraphicTypes (@PathVariable("institutionId") Integer institutionId,
																					@PathVariable("patientId") Integer patientId,
																					@RequestParam("chartOptionId")Short anthropometricGraphicOptionId)
	{
		log.debug("Input parameters -> institutionId {}, patientId {}, anthropometricGraphicOptionId {}", institutionId, patientId, anthropometricGraphicOptionId);
		EAnthropometricGraphicOption chartOption = EAnthropometricGraphicOption.map(anthropometricGraphicOptionId);
		EAnthropometricGraphic anthropometricGraphic = mapToAnthropometricGraphic(chartOption);
		List<EAnthropometricGraphicType> result = getAvailableAnthropometricGraphicTypes.run(patientId, anthropometricGraphic);
		log.debug("Output -> result {}", result);
		return result;
	}

	private EAnthropometricGraphic mapToAnthropometricGraphic(EAnthropometricGraphicOption chartOption){
		if (chartOption.equals(EAnthropometricGraphicOption.LENGHT_HEIGHT_FOR_AGE))
			return EAnthropometricGraphic.LENGTH_HEIGHT_FOR_AGE;
		return null;
	}

}
