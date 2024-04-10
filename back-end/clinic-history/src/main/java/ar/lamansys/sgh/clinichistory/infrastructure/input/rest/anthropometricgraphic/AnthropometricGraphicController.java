package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.anthropometricgraphic;

import ar.lamansys.sgh.clinichistory.application.canshowanthropometricgraphic.CanShowAnthropometricGraphic;
import ar.lamansys.sgh.clinichistory.application.getanthropometricgraphicdata.GetAnthropometricGraphicData;
import ar.lamansys.sgh.clinichistory.application.getavailableanthropometricgraphictypes.GetAvailableAnthropometricGraphicTypes;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.AnthropometricGraphicDataBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.AnthropometricGraphicEnablementBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.AnthropometricValueBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.EAnthropometricGraphic;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.EAnthropometricGraphicType;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.anthropometricgraphic.dto.AnthropometricGraphicDataDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.anthropometricgraphic.dto.AnthropometricGraphicEnablementDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.anthropometricgraphic.dto.EAnthropometricGraphicOption;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.anthropometricgraphic.mapper.AnthropometricGraphicMapper;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/institution/{institutionId}/percentiles")
@RestController
public class AnthropometricGraphicController {

	private final CanShowAnthropometricGraphic canShowAnthropometricGraphic;
	private final GetAvailableAnthropometricGraphicTypes getAvailableAnthropometricGraphicTypes;
	private final GetAnthropometricGraphicData getAnthropometricGraphicData;
	private final AnthropometricGraphicMapper anthropometricGraphicMapper;
	private final ObjectMapper jackson;

	@GetMapping("/patient/{patientId}/can-show-graphic")
	@ResponseStatus(HttpStatus.OK)
	public AnthropometricGraphicEnablementDto canShowAnthropometricGraphic (@PathVariable("institutionId")Integer institutionId,
																			@PathVariable("patientId")Integer patientId)
	{
		log.debug("Input parameters -> patientId {}", patientId);
		AnthropometricGraphicEnablementBo enablementBo = canShowAnthropometricGraphic.run(patientId);
		AnthropometricGraphicEnablementDto result = anthropometricGraphicMapper.toAnthropometricGraphicEnablementDto(enablementBo);
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

	@GetMapping("/patient/{patientId}/graphic-data")
	@ResponseStatus(HttpStatus.OK)
	public AnthropometricGraphicDataDto getAnthropometricGraphicData (@PathVariable("institutionId") Integer institutionId,
																	  @PathVariable("patientId") Integer patientId,
																	  @RequestParam("graphicOptionId") Short anthropometricGraphicOptionId,
																	  @RequestParam("graphicTypeId") Short anthropometricGraphicTypeId,
																	  @RequestParam(value = "actualValue", required = false) String actualValue)
	{
		log.debug("Input parameters -> institutionId {}, patientId {}, graphicOptionId {}, graphicTypeId {}", institutionId, patientId, anthropometricGraphicOptionId, anthropometricGraphicTypeId);
		EAnthropometricGraphicType anthropometricGraphicType = EAnthropometricGraphicType.map(anthropometricGraphicTypeId);
		EAnthropometricGraphic anthropometricGraphic = mapToAnthropometricGraphic(EAnthropometricGraphicOption.map(anthropometricGraphicOptionId));
		AnthropometricGraphicDataBo anthropometricGraphicDataBo = getAnthropometricGraphicData.run(patientId, anthropometricGraphic, anthropometricGraphicType, parseActualValue(actualValue));
		AnthropometricGraphicDataDto result = anthropometricGraphicMapper.anthropometricGraphicDataDto(anthropometricGraphicDataBo);
		log.debug("Output -> result {}", result);
		return result;
	}

	@GetMapping("/chart-options")
	@ResponseStatus(HttpStatus.OK)
	public List<EAnthropometricGraphicOption> getGraphicOptions(){
		log.debug("Get anthropometric graphics options");
		List<EAnthropometricGraphicOption> result = EAnthropometricGraphicOption.getAll();
		log.debug("Output -> result {}", result);
		return result;
	}

	private EAnthropometricGraphic mapToAnthropometricGraphic(EAnthropometricGraphicOption chartOption) {
		if (chartOption.equals(EAnthropometricGraphicOption.LENGHT_HEIGHT_FOR_AGE)) return EAnthropometricGraphic.LENGTH_HEIGHT_FOR_AGE;
		if (chartOption.equals(EAnthropometricGraphicOption.WEIGHT_FOR_AGE)) return EAnthropometricGraphic.WEIGHT_FOR_AGE;
		if (chartOption.equals(EAnthropometricGraphicOption.WEIGHT_FOR_LENGTH)) return EAnthropometricGraphic.WEIGHT_FOR_LENGTH;
		if (chartOption.equals(EAnthropometricGraphicOption.WEIGHT_FOR_HEIGHT)) return EAnthropometricGraphic.WEIGHT_FOR_HEIGHT;
		return null;
	}

	private AnthropometricValueBo parseActualValue(String actualValue) {
		if (actualValue == null)
			return null;
		AnthropometricValueBo anthropometricValue = null;
		try {
			anthropometricValue = jackson.readValue(actualValue, AnthropometricValueBo.class);
		} catch (IOException e) {
			log.error(String.format("Error mapping filter: %s", actualValue), e);
		}
		return anthropometricValue;
	}

}
